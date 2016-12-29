package venus.task.collect;

import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyEventMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.CommonUtil;
import venus.helper.util.DateUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyEvent;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyEventTask {
	Logger logger=Logger.getLogger(StockCompanyEventTask.class);
	@Value("${stock-company-event-task-threadnum}")
	public int threadNum;
	@Autowired
	StockCompanyEventMapper stockCompanyEventMapper;
	@Autowired
	StockinfoMapper stockinfoMapper;
	@Autowired URLUtil URLUtil;
	public void init(int threadId){
		init(false,threadId);
	}
	public void initCache(int threadId){
		init(true,threadId);
	}
	private void init(boolean cacheParam,int threadId){
		logger.info("[start]"+cacheParam);
		try{
			List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
			for(Stockinfo stock:stocks){
				//http://stockpage.10jqka.com.cn/000001/event/
				String code=stock.getCode();
				if (code.hashCode() % threadNum != threadId) {
					continue;
				}
				
				String str=null;
				try{
					str=URLUtil.url2str("http://stockpage.10jqka.com.cn/"+stock.getCode()+"/event/", cacheParam);
				}catch(Exception e){
					logger.error("[except]",e);
					continue;
				}
				if(StringUtil.isBlank(str))continue;
				Document document=Jsoup.parse(str);
				
				logger.info(stock.getCode());
				
				String date=DateUtil.date2();
				
				Elements xElements=document.select("#remind table");
				
				if(xElements.size()>1){
					Elements elements=xElements.get(0).select("tr");
					for (int i = 0; i < elements.size(); i++) {
						String type=elements.get(i).select(".hltip").get(0).text();
						type=type.substring(0, type.length()-1);
	
						String title=null;
						if(elements.get(i).select(".today").size()>0){
							title=elements.get(i).select("td").get(1).text();
						}else{
							title=elements.get(i).select("td").get(0).text();
						}
	
						title=title.replaceAll("详情>> ", "").replaceAll("更多>> ", "").replaceAll("查看全部", "").replaceAll("▼收起▲", "").replaceAll(type+"：", "");
						if(title.indexOf("▼▲")>0){
							title=title.substring(0, title.indexOf("▼▲")-5);
						}
						
						String md5=CommonUtil.md5(stock.getCode()+"/"+date+"/"+type+"/"+title);
						
						StockCompanyEvent dbStockCompanyEvent=stockCompanyEventMapper.find(md5);
						if(dbStockCompanyEvent==null){
							StockCompanyEvent stockCompanyEvent=new StockCompanyEvent();
							stockCompanyEvent.setCode(stock.getCode());
							stockCompanyEvent.setDate(date);
							stockCompanyEvent.setTitle(title);
							stockCompanyEvent.setType(type);
							stockCompanyEvent.setUpdate_time(DateUtil.datetime());
							stockCompanyEvent.setMd5(md5);
							stockCompanyEventMapper.insert(stockCompanyEvent);
							logger.info(stockCompanyEvent);
						}
						
					}
				}
				
				Elements temp=document.select("#remind table");
				if(temp.size()==0)continue;
				
				Elements elements=null;
				if(xElements.size()>1){
					elements=temp.get(1).select("tr");
				}else{
					elements=temp.get(0).select("tr");
				}
				
				for(int i=0;i<elements.size();i++){
					date=elements.get(i).select("td").get(0).text();
					String type=elements.get(i).select("td strong").get(0).text();
					type=type.substring(0, type.length()-1);
					
					String title=elements.get(i).select("td").get(1).text();
					title=title.replaceAll("详情>> ", "").replaceAll("更多>> ", "").replaceAll("查看全部", "").replaceAll("▼收起▲", "").replaceAll(type+"：", "");
					if(title.indexOf("▼▲")>0){
						title=title.substring(0, title.indexOf("▼▲")-5);
					}
					
					String md5=CommonUtil.md5(stock.getCode()+"/"+date+"/"+type+"/"+title);
					
					StockCompanyEvent dbStockCompanyEvent=stockCompanyEventMapper.find(md5);
					if(dbStockCompanyEvent==null){
						StockCompanyEvent stockCompanyEvent=new StockCompanyEvent();
						stockCompanyEvent.setCode(stock.getCode());
						stockCompanyEvent.setDate(date);
						stockCompanyEvent.setTitle(title);
						stockCompanyEvent.setType(type);
						stockCompanyEvent.setUpdate_time(DateUtil.datetime());
						stockCompanyEvent.setMd5(md5);
						stockCompanyEventMapper.insert(stockCompanyEvent);
						logger.info(stockCompanyEvent);
					}
					
				}
				
			}
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]"+cacheParam);
	}
}
