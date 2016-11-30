package venus.task.collect;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHolderOrgMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyHolderOrg;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyHolderOrgTask {
	Logger logger=Logger.getLogger(StockCompanyHolderOrgTask.class);
	@Autowired
	StockCompanyHolderOrgMapper stockCompanyHolderOrgMapper;
	@Autowired
	StockinfoMapper stockinfoMapper;
	@Autowired URLUtil URLUtil;
	public void init(){
		init(false);
	}
	public void initCache(){
		init(true);
	}
	private void init(boolean cacheParam){
		logger.info("[start]"+cacheParam);
		try{
			List<StockCompanyHolderOrg> insertAll=new ArrayList<StockCompanyHolderOrg>();
			
			List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
			for(Stockinfo stock:stocks){
				//http://stockpage.10jqka.com.cn/000001/position/
				String str=null;
				try{
					str=URLUtil.url2str("http://stockpage.10jqka.com.cn/"+stock.getCode()+"/position/", cacheParam);
				}catch(Exception e){
					e.printStackTrace();
					logger.error("[except]"+e.getMessage());
					continue;
				}
				if(StringUtil.isBlank(str))continue;
				Document document=Jsoup.parse(str);
				
				List<String> ids=new ArrayList<String>();
				List<String> times=new ArrayList<String>();
				Elements timesElement=document.select("#holdetail .m_tab ul li");
				for(int i=0;i<timesElement.size();i++){
					times.add(timesElement.get(i).text());
					ids.add(timesElement.get(i).attr("name"));
				}
				
				for(int i=0;i<times.size();i++){
					Elements elements=document.select("#"+ids.get(i));
					
					for(int j=0;j<elements.size();j++){
						Elements elements2=elements.get(j).select(".organ_item");
	
						for(int k=0;k<elements2.size();k++){
							Elements elements3=elements2.get(k).children();
							
							String name=elements3.get(0).text();
							String type=elements3.get(1).text();
							double num=NumUtil.text2num(elements3.get(2).text());
							double total_price=NumUtil.text2num(elements3.get(3).text());
							double rate=NumUtil.text2num(elements3.get(4).text());
							double num_change=NumUtil.text2num(elements3.get(5).text());
							
							StockCompanyHolderOrg stockCompanyHolderOrg=new StockCompanyHolderOrg();
							stockCompanyHolderOrg.setCode(stock.getCode());
							stockCompanyHolderOrg.setName(name);
							stockCompanyHolderOrg.setType(type);
							stockCompanyHolderOrg.setNum(num);
							stockCompanyHolderOrg.setTotal_price(total_price);
							stockCompanyHolderOrg.setRate(rate);
							stockCompanyHolderOrg.setNum_change(num_change);
							stockCompanyHolderOrg.setUpdate_time(DateUtil.datetime());
							stockCompanyHolderOrg.setDate(times.get(i));
							
							StockCompanyHolderOrg existStockCompanyHolderOrg=stockCompanyHolderOrgMapper.findCodeDtNameType(stock.getCode(), times.get(i), name, type);
							if(existStockCompanyHolderOrg==null){
								insertAll.add(stockCompanyHolderOrg);
							}
							
							if(insertAll.size()==1000){
								stockCompanyHolderOrgMapper.insertAll(insertAll);
								logger.info(stock.getCode()+":"+insertAll.size());
								insertAll.clear();
							}
						}
					}
				}
			}
			if(insertAll.size()>0){
				stockCompanyHolderOrgMapper.insertAll(insertAll);
				logger.info(insertAll.size());
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]"+cacheParam);
	}
}
