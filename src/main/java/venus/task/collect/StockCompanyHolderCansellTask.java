package venus.task.collect;

import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHolderCansellMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockCompanyHolderCansell;
import venus.model.dao.Stockinfo;

@Component
public class StockCompanyHolderCansellTask {
	Logger logger=Logger.getLogger(StockCompanyHolderCansellTask.class);
	@Value("${stock-company-holder-cansell-threadnum}")
	public int threadNum;
	@Autowired
	StockCompanyHolderCansellMapper stockCompanyHolderCansellMapper;
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
				if (stock.getCode().hashCode() % threadNum != threadId) {
					continue;
				}
				
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
				
				Elements elements=document.select("#liftban tr");
				for(int i=1;i<elements.size();i++){
					Elements tds=elements.get(i).children();
					String date=tds.get(0).text();
					double num=NumUtil.text2num(tds.get(1).text());
					double price=NumUtil.text2num(tds.get(2).text());
					String type=tds.get(5).text();
					double total_price=NumUtil.text2num(tds.get(3).text());
					double rate=NumUtil.text2num(tds.get(4).text());
					
					StockCompanyHolderCansell stockCompanyHolderCansell=new StockCompanyHolderCansell();
					stockCompanyHolderCansell.setCode(stock.getCode());
					stockCompanyHolderCansell.setDate(date);
					stockCompanyHolderCansell.setNum(num);
					stockCompanyHolderCansell.setRate(rate);
					stockCompanyHolderCansell.setType(type);
					stockCompanyHolderCansell.setPrice(price);
					stockCompanyHolderCansell.setTotal_price(total_price);
					stockCompanyHolderCansell.setUpdate_time(DateUtil.datetime());
					
					StockCompanyHolderCansell existStockCompanyHolderCansell=stockCompanyHolderCansellMapper.findCodeDate(stock.getCode(), date);
					if(existStockCompanyHolderCansell!=null){
						stockCompanyHolderCansellMapper.delete(stock.getCode(), date);
					}
					stockCompanyHolderCansellMapper.insert(stockCompanyHolderCansell);
					logger.info(stockCompanyHolderCansell);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]"+cacheParam);
	}
}
