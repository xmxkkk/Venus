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

import venus.dao.StockDayFuMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.Constant;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockDayFu;
import venus.model.dao.Stockinfo;

@Component
public class StockDayFuTask {
	Logger logger=Logger.getLogger(StockDayFuTask.class);
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockDayFuMapper stockDayFuMapper;
	@Autowired StockinfoTask stockinfoTask;
	@Autowired URLUtil URLUtil;
	@Value("${stock-day-fu-task-threadnum}")
	public int threadNum;
	
	public void init(int threadId,boolean clearCache,boolean updateNew,String stockCode){
		logger.info("[start]threadId="+threadId+",clearCache="+clearCache+",updateNew="+updateNew);
		try{
			List<Stockinfo> stocks=null;
			if(stockCode==null){
				if(updateNew){
					stocks=stockinfoMapper.findStockinfos();
				}else{
					stocks=stockinfoMapper.findFlag(0);
				}
			}else{
				stocks=new ArrayList<Stockinfo>();
				Stockinfo stock=stockinfoMapper.findStockinfo(stockCode);
				stocks.add(stock);
			}
			
			int currentSeason=DateUtil.currentSeason();
			String currentYear=DateUtil.currentYear();
			
			for(int i=0;i<stocks.size();i++){
				if(stockCode==null){
					if (i % threadNum != threadId) {
						continue;
					}
				}
				
				Stockinfo stock = stocks.get(i);
				String code=stock.getCode();

				StockDayFu lastStockDayFu=stockDayFuMapper.findLast(code);
				
				List<StockDayFu> insertAll=new ArrayList<StockDayFu>();
				
				String str=null;
				try{
					str=URLUtil.url2str("http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_FuQuanMarketHistory/stockid/"+code+".phtml",Constant.CHARSET$GB2312, false);
				}catch(Exception e){
					logger.error("[except]",e);
					continue;
				}
				if(StringUtil.isBlank(str)){
					logger.error("[message]"+stock);
					continue;
				}
				Document document=Jsoup.parse(str);
				
				boolean conti=true;

				List<String> yearsList=new ArrayList<String>();
				Elements yearsElements=document.select("#center select[name=year] option");
				for(int j=yearsElements.size()-1;j>=0;j--){
					yearsList.add(yearsElements.get(j).text());
				}
				
				for(int j=0;j<yearsList.size();j++){
					if(!conti)break;
					
					String year=yearsList.get(j);
					for(int k=1;k<=4;k++){
						
						if(lastStockDayFu!=null){
							String lastYear=DateUtil.currentYear(lastStockDayFu.getDt());
							int lastSeason=DateUtil.currentSeason(lastStockDayFu.getDt());
							
							if((year+k).compareTo(lastYear+lastSeason)<0){
								continue;
							}
						}
						
						boolean cache=true;
						if(year.equals(currentYear)&&k==currentSeason){
							cache=false;
						}
						String html=null;
						try{
							String url="http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_FuQuanMarketHistory/stockid/"+stock.getCode()+".phtml?year="+year+"&jidu="+k;
							if(clearCache){
								URLUtil.clearCache(url);
							}
							html=URLUtil.url2str(url,Constant.CHARSET$GB2312, cache);
							if(StringUtil.isBlank(html)){
								URLUtil.clearCache(url);
								html=URLUtil.url2str(url,Constant.CHARSET$GB2312, cache);
							}
						}catch(Exception e){
							logger.error("[message]"+stock.getCode()+","+year+","+k);
							logger.error("[message]",e);
							conti=false;
							break;
						}
						
						if(StringUtil.isBlank(html)){
							conti=false;
							break;
						}
						if(k==1){
							stockDayFuMapper.deleteCodeDt(code, year+"0101", year+"0331");
						}else if(k==2){
							stockDayFuMapper.deleteCodeDt(code, year+"0401", year+"0630");
						}else if(k==3){
							stockDayFuMapper.deleteCodeDt(code, year+"0701", year+"0930");
						}else if(k==4){
							stockDayFuMapper.deleteCodeDt(code, year+"1001", year+"1231");
						}
						
						Document doc=Jsoup.parse(html);
						Elements rows=doc.select("#FundHoldSharesTable tr");
						for(int m=2;m<rows.size();m++){
							Elements cells=rows.get(m).children();
							if(cells.size()!=8){
								continue;
							}
							String dt=cells.get(0).text().replaceAll("-", "");
							double open_price=NumUtil.format4(Double.parseDouble(cells.get(1).text()));
							double high_price=NumUtil.format4(Double.parseDouble(cells.get(2).text()));
							double close_price=NumUtil.format4(Double.parseDouble(cells.get(3).text()));
							double low_price=NumUtil.format4(Double.parseDouble(cells.get(4).text()));
							double trade_quty=NumUtil.format4(Double.parseDouble(cells.get(5).text()));
							double trade_amt=NumUtil.format4(Double.parseDouble(cells.get(6).text()));
							double weight=NumUtil.format4(Double.parseDouble(cells.get(7).text()));
							
							StockDayFu stockDayFu=new StockDayFu();
							stockDayFu.setCode(stock.getCode());
							stockDayFu.setDt(dt);
							stockDayFu.setWeek(DateUtil.date2week(dt));
							stockDayFu.setOpen_price(open_price);
							stockDayFu.setClose_price(close_price);
							stockDayFu.setHigh_price(high_price);
							stockDayFu.setLow_price(low_price);
							stockDayFu.setTrade_amt(trade_amt);
							stockDayFu.setTrade_quty(trade_quty);
							stockDayFu.setWeight(weight);
							
							stockDayFu.setChange_rate(null);
							stockDayFu.setChange_price(null);
							
							insertAll.add(stockDayFu);
							
							if(insertAll.size()==1000){
								stockDayFuMapper.insertAll(insertAll);
								logger.info(code+":"+insertAll.size());
								insertAll.clear();
							}
						}
					}
				}
				if(insertAll.size()>0){
					stockDayFuMapper.insertAll(insertAll);
					logger.info(code+":"+insertAll.size());
					insertAll.clear();
				}
//				updateChange(threadId,code);
//				stockinfoTask.updateWeight(code);
				
				stockinfoMapper.updateFlag(code, 1);
				
			}
			
			
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]threadId="+threadId+",clearCache="+clearCache+",updateNew="+updateNew);
	}
	
	public void updateChange(int threadId,String stockCode){
		logger.info("[start]");
		try{
			List<Stockinfo> stocks=null;
			if(stockCode==null){
				stocks=stockinfoMapper.findStockinfos();
			}else{
				Stockinfo stock=stockinfoMapper.findStockinfo(stockCode);
				stocks=new ArrayList<Stockinfo>();
				stocks.add(stock);
			}
			
			for (int i=0;i<stocks.size();i++) {
				if(stockCode==null){
					if (i % threadNum != threadId) {
						continue;
					}
				}
				
				Stockinfo stock=stocks.get(i);
				
				StockDayFu lastOne=stockDayFuMapper.findLast(stock.getCode());
				
				/**/
				if(lastOne!=null){
					if(lastOne.getChange_rate()!=null){
						logger.info(stock);
						continue;
					}
				}
				
				List<StockDayFu> updateChangeAll=new ArrayList<StockDayFu>();
				
				List<StockDayFu> stockDays=stockDayFuMapper.findCode(stock.getCode());
				for(int j=0;j<stockDays.size();j++){
					StockDayFu stockDay=stockDays.get(j);
					
					if(stockDay.getChange_rate()!=null){
						continue;
					}
					
					double change_rate=0;
					double change_price=0;
					if(j==0){
						change_rate=(stockDay.getClose_price()-stockDay.getOpen_price())/stockDay.getOpen_price();
						if(Double.isNaN(change_rate) || Double.isInfinite(change_rate)){
							change_rate=0;
						}
						change_rate=NumUtil.format4(change_rate);
						change_price=NumUtil.format4(stockDay.getClose_price()-stockDay.getOpen_price());
					}else{
						StockDayFu leftDay=stockDays.get(j-1);
						change_rate=(stockDay.getClose_price()-leftDay.getClose_price())/leftDay.getClose_price();
						if(Double.isNaN(change_rate) || Double.isInfinite(change_rate)){
							change_rate=0;
						}
						change_rate=NumUtil.format4(change_rate);
						change_price=NumUtil.format4(stockDay.getClose_price()-leftDay.getClose_price());
					}
					stockDay.setChange_rate(change_rate);
					stockDay.setChange_price(change_price);
					
					updateChangeAll.add(stockDay);
					
					if(updateChangeAll.size()==1000){
						stockDayFuMapper.updateChangeAll(updateChangeAll);
						updateChangeAll.clear();
					}
				}
				if(updateChangeAll.size()>0){
					stockDayFuMapper.updateChangeAll(updateChangeAll);
					updateChangeAll.clear();
				}
				
				logger.info(stock);
			}
		} catch (Exception e) {
			logger.error("[except]",e);
		}
		logger.info("[end]");
	}
}
