package venus.task.collect;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHolderChangeMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyHolderChange;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyHolderChangeTask {
	Logger logger=Logger.getLogger(StockCompanyHolderChangeTask.class);
	@Value("${stock-company-holder-change-threadnum}")
	public int threadNum;
	
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockCompanyHolderChangeMapper stockCompanyHolderChangeMapper;
	
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
			List<StockCompanyHolderChange> insertAll=new ArrayList<StockCompanyHolderChange>();
			
			List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
			for(Stockinfo stock:stocks){
				//http://stockpage.10jqka.com.cn/000001/position/
				if (stock.getCode().hashCode() % threadNum != threadId) {
					continue;
				}
				
				String code=stock.getCode();
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
				
				Elements elements=document.select("#astockchange tbody tr");
				if(elements==null||elements.size()==0){
					continue;
				}
				for(int i=0;i<elements.size();i++){
					String time=elements.get(i).child(0).text();
					String biandongyuanyin=elements.get(i).child(1).text();
					String aguzonggubenStr=elements.get(i).child(2).text();
					String liutongaguStr=elements.get(i).child(3).text();
					String xianshouaguStr=elements.get(i).child(4).text();
					
					double aguzongguben=0;
					if(!aguzonggubenStr.equals("-")&&!aguzonggubenStr.equals("")){
						aguzongguben=Double.parseDouble(aguzonggubenStr)*10000;
					}
					double liutongagu=0;
					if(!liutongaguStr.equals("-")&&!liutongaguStr.equals("")){
						liutongagu=Double.parseDouble(liutongaguStr)*10000;
					}
					double xianshouagu=0;
					if(!xianshouaguStr.equals("-")&&!xianshouaguStr.equals("")){
						xianshouagu=Double.parseDouble(xianshouaguStr)*10000;
					}
					
					StockCompanyHolderChange stockCompanyHolderChange=new StockCompanyHolderChange();
					stockCompanyHolderChange.setCode(code);
					stockCompanyHolderChange.setTime(time);
					stockCompanyHolderChange.setBiandongyuanyin(biandongyuanyin);
					stockCompanyHolderChange.setAguzongguben(aguzongguben);
					stockCompanyHolderChange.setLiutongagu(liutongagu);
					stockCompanyHolderChange.setXianshouagu(xianshouagu);
					
					StockCompanyHolderChange existStockCompanyHolderChange=stockCompanyHolderChangeMapper.findCodeTime(code, time);
					if(existStockCompanyHolderChange==null){
						insertAll.add(stockCompanyHolderChange);
					}
					
					if(insertAll.size()==1000){
						stockCompanyHolderChangeMapper.insertAll(insertAll);
						logger.info(stock.getCode()+":"+insertAll.size());
						insertAll.clear();
					}
					
				}
			}
			if(insertAll.size()>0){
				stockCompanyHolderChangeMapper.insertAll(insertAll);
				logger.info(insertAll.size());
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]"+cacheParam);
	}
	
	
}
