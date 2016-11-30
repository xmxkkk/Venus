package venus.task.collect;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHoldingMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.CommonUtil;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyHolding;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyHoldingTask {
	Logger logger=Logger.getLogger(StockCompanyHoldingTask.class);
	@Autowired
	StockCompanyHoldingMapper stockCompanyHoldingMapper;
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
			List<StockCompanyHolding> insertAll=new ArrayList<StockCompanyHolding>();
			
			List<String> list=new ArrayList<String>();
			
			List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
			for(Stockinfo stock:stocks){
				//http://stockpage.10jqka.com.cn/000002/company/
				
				String str=null;
				try{
					str=URLUtil.url2str("http://stockpage.10jqka.com.cn/"+stock.getCode()+"/company/",cacheParam);
				}catch(Exception e){
					e.printStackTrace();
					logger.error("[except]"+e.getMessage());
					continue;
				}
				if(StringUtil.isBlank(str))continue;
				
				Document document=Jsoup.parse(str);
				
				Elements elements=document.select("#ckg_table tr");
				if(elements.size()==0)continue;
				
				String touziUnit="";
				String lirunUnit="";
				if(elements.get(0).children().get(4).outerHtml().contains("万元")){
					touziUnit="万元";
				}else if(elements.get(0).children().get(4).outerHtml().contains("亿元")){
					touziUnit="亿元";
				}
				if(elements.get(0).children().get(5).outerHtml().contains("万元")){
					lirunUnit="万元";
				}else if(elements.get(0).children().get(5).outerHtml().contains("亿元")){
					lirunUnit="亿元";
				}
				
				stockCompanyHoldingMapper.deletes(stock.getCode());
				
				for(int i=1;i<elements.size();i++){
					Element element=elements.get(i);
					Elements elements2=element.children();
					
					if(elements2.size()<8){
						continue;
					}
					
					String name=elements2.get(1).text().toUpperCase();
					String relation=elements2.get(2).text();
					double rate=NumUtil.text2num(elements2.get(3).text());
					double money=NumUtil.text2num(elements2.get(4).text()+touziUnit);
					double profit=NumUtil.text2num(elements2.get(5).text()+lirunUnit);
					String is_combine = elements2.get(6).text();
					String yewu=elements2.get(7).text();
					
					StockCompanyHolding stockCompanyHolding=new StockCompanyHolding();
					stockCompanyHolding.setCode(stock.getCode());
					stockCompanyHolding.setName(name);
					stockCompanyHolding.setRelation(relation);
					stockCompanyHolding.setRate(rate);
					stockCompanyHolding.setMoney(money);
					stockCompanyHolding.setProfit(profit);
					stockCompanyHolding.setIs_combine(is_combine);
					stockCompanyHolding.setYewu(yewu);
					stockCompanyHolding.setUpdate_time(DateUtil.datetime());
					
					StockCompanyHolding existStockCompanyHolding=stockCompanyHoldingMapper.find(stock.getCode(), name, relation,rate);
					if(existStockCompanyHolding!=null){
						stockCompanyHoldingMapper.delete(stock.getCode(), name, relation,rate);
					}
					
					String md5Str=stock.getCode()+ name+ relation+NumUtil.format(rate,2);
					String md5=CommonUtil.md5(md5Str);
					
					if(list.contains(md5)){
						continue;
					}
					
					insertAll.add(stockCompanyHolding);
					list.add(md5);
					if(insertAll.size()==1000){
						stockCompanyHoldingMapper.insertAll(insertAll);
						logger.info(stock.getCode()+":"+insertAll.size());
						insertAll.clear();
						list.clear();
					}
				}
			}
			if(insertAll.size()>0){
				stockCompanyHoldingMapper.insertAll(insertAll);
				insertAll.clear();
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]");
	}
}
