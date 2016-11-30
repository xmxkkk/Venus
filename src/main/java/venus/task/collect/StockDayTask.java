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

import venus.dao.StockDayMapper;
import venus.dao.StockinfoMapper;
import venus.helper.exception.BizException;
import venus.helper.util.Constant;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.helper.util.StringUtil;
import venus.helper.util.URLUtil;
import venus.model.dao.StockDay;
import venus.model.dao.Stockinfo;

@Component
public class StockDayTask {
	Logger logger=Logger.getLogger(StockDayTask.class);
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockDayMapper stockDayMapper;
	@Autowired StockinfoTask stockinfoTask;
	@Autowired URLUtil URLUtil;
	@Value("${stock-day-task-threadnum}")
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

				StockDay lastStockDay=stockDayMapper.findLast(code);
				
				List<StockDay> insertAll=new ArrayList<StockDay>();
				
				String str=null;
				try{
					str=URLUtil.url2str("http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/"+code+".phtml",Constant.CHARSET$GB2312, false);
				}catch(Exception e){
					e.printStackTrace();
					logger.error("[except]"+e.getMessage());
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
						
						if(lastStockDay!=null){
							String lastYear=DateUtil.currentYear(lastStockDay.getDt());
							int lastSeason=DateUtil.currentSeason(lastStockDay.getDt());
							
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
							String url="http://vip.stock.finance.sina.com.cn/corp/go.php/vMS_MarketHistory/stockid/"+stock.getCode()+".phtml?year="+year+"&jidu="+k;
							if(clearCache){
								URLUtil.clearCache(url);
							}
							html=URLUtil.url2str(url,Constant.CHARSET$GB2312, cache);
							if(StringUtil.isBlank(html)){
								URLUtil.clearCache(url);
								html=URLUtil.url2str(url,Constant.CHARSET$GB2312, cache);
							}
						}catch(Exception e){
							logger.error("[message]"+code+","+year+","+k);
							logger.error("[message]"+e.getMessage());
							conti=false;
							break;
						}
						if(StringUtil.isBlank(html)){
							conti=false;
							break;
						}
						if(k==1){
							stockDayMapper.deleteCodeDt(code, year+"0101", year+"0331");
						}else if(k==2){
							stockDayMapper.deleteCodeDt(code, year+"0401", year+"0630");
						}else if(k==3){
							stockDayMapper.deleteCodeDt(code, year+"0701", year+"0930");
						}else if(k==4){
							stockDayMapper.deleteCodeDt(code, year+"1001", year+"1231");
						}
						
						Document doc=Jsoup.parse(html);
						Elements rows=doc.select("#FundHoldSharesTable tr");
						for(int m=2;m<rows.size();m++){
							Elements cells=rows.get(m).children();
							if(cells.size()!=7){
								continue;
							}
							String dt=cells.get(0).text().replaceAll("-", "");
							double open_price=NumUtil.format4(Double.parseDouble(cells.get(1).text()));
							double high_price=NumUtil.format4(Double.parseDouble(cells.get(2).text()));
							double close_price=NumUtil.format4(Double.parseDouble(cells.get(3).text()));
							double low_price=NumUtil.format4(Double.parseDouble(cells.get(4).text()));
							double trade_quty=NumUtil.format4(Double.parseDouble(cells.get(5).text()));
							double trade_amt=NumUtil.format4(Double.parseDouble(cells.get(6).text()));
							
							StockDay stockDay=new StockDay();
							stockDay.setCode(code);
							stockDay.setDt(dt);
							stockDay.setWeek(DateUtil.date2week(dt));
							stockDay.setOpen_price(open_price);
							stockDay.setClose_price(close_price);
							stockDay.setHigh_price(high_price);
							stockDay.setLow_price(low_price);
							stockDay.setTrade_amt(trade_amt);
							stockDay.setTrade_quty(trade_quty);
							stockDay.setChange_rate(null);
							stockDay.setChange_price(null);
							
							insertAll.add(stockDay);
							
							if(insertAll.size()==1000){
								stockDayMapper.insertAll(insertAll);
								logger.info(code+":"+insertAll.size());
								insertAll.clear();
							}
						}
					}
				}
				if(insertAll.size()>0){
					stockDayMapper.insertAll(insertAll);
					logger.info(code+":"+insertAll.size());
					insertAll.clear();
				}
				
//				updateChange(threadId,code);
//				stockinfoTask.updateBeta(code);
//				stockinfoTask.updateTradeDays(code);
//				stockinfoTask.updateFirstTradeDay(code);
				
				stockinfoMapper.updateFlag(code, 1);
			}
			
			
			
		}catch(BizException e){
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
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
				
				StockDay lastOne=stockDayMapper.findLast(stock.getCode());
				
				/**/
				if(lastOne!=null){
					if(lastOne.getChange_rate()!=null){
						logger.info(stock);
						continue;
					}
				}
				
				List<StockDay> updateChangeAll=new ArrayList<StockDay>();
				
				List<StockDay> stockDays=stockDayMapper.findCode(stock.getCode());
				for(int j=0;j<stockDays.size();j++){
					StockDay stockDay=stockDays.get(j);
					
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
						StockDay leftDay=stockDays.get(j-1);
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
					
					if(stockDays.size()==1000){
						stockDayMapper.updateChangeAll(updateChangeAll);
						updateChangeAll.clear();
					}
				}
				if(stockDays.size()>0){
					stockDayMapper.updateChangeAll(updateChangeAll);
					updateChangeAll.clear();
				}
				
				logger.info(stock);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]");
	}
}
