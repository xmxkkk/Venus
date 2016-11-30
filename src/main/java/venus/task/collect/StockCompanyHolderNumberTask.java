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

import venus.dao.StockCompanyHolderNumberMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyHolderNumber;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyHolderNumberTask {
	Logger logger=Logger.getLogger(StockCompanyHolderNumberTask.class);
	@Autowired
	StockCompanyHolderNumberMapper stockCompanyHolderNumberMapper;
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
			List<StockCompanyHolderNumber> allList=new ArrayList<StockCompanyHolderNumber>();
			
			List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
			for(Stockinfo stock:stocks){
				String str=null;
				try{
					str=URLUtil.url2str("http://stockpage.10jqka.com.cn/"+stock.getCode()+"/holder/", cacheParam);
				}catch(Exception e){
					e.printStackTrace();
					logger.error("[except]"+e.getMessage());
					continue;
				}
				if(StringUtil.isBlank(str))continue;
				Document document=Jsoup.parse(str);
				
				Elements elements=document.select(".table_data .tbody th");
	
				List<String> menus=new ArrayList<String>();
				for(int i=0;i<elements.size();i++){
					menus.add(elements.get(i).text());
				}
	
				List<String> times=new ArrayList<String>();
				elements = document.select(".data_tbody .top_thead th");
				for(int i=0;i<elements.size();i++){
					times.add(elements.get(i).text());
				}
				
				elements=document.select(".data_tbody .tbody tr");
				for(int i=0;i<elements.size();i++){
					String menu=menus.get(i);
					Element element=elements.get(i);
					Elements dataEles=element.select("td");
					for(int j=0;j<dataEles.size();j++){
						String value=dataEles.get(j).text();
						double dbValue=NumUtil.text2num(value);
						
						StockCompanyHolderNumber stockCompanyHolderNumber=new StockCompanyHolderNumber();
						stockCompanyHolderNumber.setCode(stock.getCode());
						stockCompanyHolderNumber.setMenu(menu);
						stockCompanyHolderNumber.setTime(times.get(j));
						stockCompanyHolderNumber.setValue(dbValue);
						stockCompanyHolderNumber.setUpdate_time(DateUtil.datetime());
	
						StockCompanyHolderNumber existStockCompanyHolderNumber=stockCompanyHolderNumberMapper.findCodeMenuTime(stock.getCode(), menu,times.get(j));
						if(existStockCompanyHolderNumber==null){
							allList.add(stockCompanyHolderNumber);
						}
						
						if(allList.size()==1000){
							stockCompanyHolderNumberMapper.insertAll(allList);
							logger.info(stock.getCode()+"/"+allList.size());
							allList.clear();
						}
					}
					
				}
			}
			if(allList.size()>0){
				stockCompanyHolderNumberMapper.insertAll(allList);
				logger.info(allList.size());
				allList.clear();
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]"+cacheParam);
	}
}
