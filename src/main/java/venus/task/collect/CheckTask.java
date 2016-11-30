package venus.task.collect;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyFinanceMapper;
import venus.dao.StockCompanyHangyeDataMapper;
import venus.dao.StockCompanyHangyeMapper;
import venus.dao.StockCompanyInfoMapper;
import venus.dao.StockCompanySummaryMapper;
import venus.dao.StockDayFuMapper;
import venus.dao.StockDayMapper;
import venus.dao.StockinfoMapper;
import venus.dao.TradeDayMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.model.dao.StockCompanyFinance;
import venus.model.dao.StockCompanyHangye;
import venus.model.dao.StockCompanyHangyeData;
import venus.model.dao.StockCompanyInfo;
import venus.model.dao.StockCompanySummary;
import venus.model.dao.StockDay;
import venus.model.dao.StockDayFu;
import venus.model.dao.Stockinfo;
import venus.model.dao.TradeDay;

@Component
public class CheckTask {
	Logger logger=Logger.getLogger(CheckTask.class);
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockCompanyFinanceMapper stockCompanyFinanceMapper;
	@Autowired StockCompanyFinanceTask stockCompanyFinanceTask;
	@Autowired StockDayFuTask stockDayFuTask;
	@Autowired StockCompanyHangyeDataTask stockCompanyHangyeDataTask;
	@Autowired StockDayTask stockDayTask;
	@Autowired TradeDayMapper tradeDayMapper;
	@Autowired StockDayMapper stockDayMapper;
	@Autowired StockDayFuMapper stockDayFuMapper;
	@Autowired StockCompanyHangyeDataMapper stockCompanyHangyeDataMapper;
	@Autowired StockCompanySummaryMapper stockCompanySummaryMapper;
	@Autowired StockCompanySummaryTask stockCompanySummaryTask;
	@Autowired StockCompanyHangyeMapper stockCompanyHangyeMapper;
	@Autowired StockCompanyHangyeTask stockCompanyHangyeTask;
	@Autowired StockCompanyInfoMapper stockCompanyInfoMapper;
	@Autowired StockCompanyInfoTask stockCompanyInfoTask;
	@Autowired TradeDayTask tradeDayTask;
	public void init(){
		logger.info("[start]");
		try{
			String currentYear=DateUtil.currentYear();
			int currentSeason=DateUtil.currentSeason();
			
			String newOne=null;
			String beforeNewOne=null;
			if(currentSeason==1){
				newOne=String.valueOf(Integer.parseInt(currentYear)-1)+"-12-31";
				beforeNewOne=String.valueOf(Integer.parseInt(currentYear)-1)+"-09-30";
			}else if(currentSeason==2){
				newOne=currentYear+"-03-31";
				beforeNewOne=String.valueOf(Integer.parseInt(currentYear)-1)+"-12-31";
			}else if(currentSeason==3){
				newOne=currentYear+"-06-30";
				beforeNewOne=currentYear+"-03-31";
			}else if(currentSeason==4){
				newOne=currentYear+"-09-30";
				beforeNewOne=currentYear+"-06-30";
			}
			
			tradeDayTask.init();
			
			TradeDay tradeDay=tradeDayMapper.findLastDay();
			
			String lastTradeDay=tradeDay.getDt();
//			String beforeLastTradeDay=DateUtil.date(lastTradeDay, -1);
			
			/*
			String hourI=DateUtil.datetime().substring(8, 10);
			int hour=Integer.parseInt(hourI);
			if(hour>=18&&hour<=23){
				
			}else{
				lastTradeDay=null;
			}*/
			
			
			List<Stockinfo> stocks=stockinfoMapper.findNotStop();
			for(Stockinfo stock:stocks){
				String code=stock.getCode();
				
				StockCompanyFinance stockCompanyFinance=stockCompanyFinanceMapper.findLastTime(code);
				if(stockCompanyFinance==null){
					logger.info("[message]"+code+",StockCompanyFinance");
					stockCompanyFinanceTask.init(code);
				}else{
					if(!stockCompanyFinance.getTime().equals(newOne)&&!stockCompanyFinance.getTime().equals(beforeNewOne)){
						logger.info("[message]"+code+",StockCompanyFinance");
						stockCompanyFinanceTask.init(code);
					}
				}
				
				if(stock.getStop()==0){
					StockDay stockDay=stockDayMapper.findLast(code);
					
					/*
					if(lastTradeDay==null){
						if(stockDay==null || (!stockDay.getDt().equals(beforeLastTradeDay))){
							logger.info("[message]"+code+",StockDayMapper");
							stockDayTask.init(0, true, true, code);
						}
					}else{
						if(stockDay==null || (!stockDay.getDt().equals(lastTradeDay))){
							logger.info("[message]"+code+",StockDayMapper");
							stockDayTask.init(0, true, true, code);
						}
					}*/
					if(stockDay==null || (!stockDay.getDt().equals(lastTradeDay))){
						logger.info("[message]"+code+",StockDayMapper");
						stockDayTask.init(0, true, true, code);
					}
					
					if(stockDay!=null && stockDay.getChange_rate()==null){
						logger.info("[message]"+code+",StockDayMapper.change_rate");
						stockDayTask.updateChange(0, code);
					}
				}
				if(stock.getStop()==0){
					StockDayFu stockDayFu=stockDayFuMapper.findLast(code);
					/*
					if(lastTradeDay==null){
						if(stockDayFu==null || (!stockDayFu.getDt().equals(beforeLastTradeDay))){
							logger.info("[message]"+code+",StockDayFuMapper");
							stockDayFuTask.init(0, true, true, code);
						}
					}else{
						if(stockDayFu==null || (!stockDayFu.getDt().equals(lastTradeDay))){
							logger.info("[message]"+code+",StockDayFuMapper");
							stockDayFuTask.init(0, true, true, code);
						}
					}*/
					if(stockDayFu==null || (!stockDayFu.getDt().equals(lastTradeDay))){
						logger.info("[message]"+code+",StockDayFuMapper");
						stockDayFuTask.init(0, true, true, code);
					}
					
					if(stockDayFu!=null && stockDayFu.getChange_rate()==null){
						logger.info("[message]"+code+",StockDayFuMapper.change_rate");
						stockDayFuTask.updateChange(0, code);
					}
				}
				
				if(stock.getStop()==0){
					StockCompanyHangyeData stockCompanyHangyeData=stockCompanyHangyeDataMapper.findLastTime(code);
					if(stockCompanyHangyeData==null){
						logger.info("[message]"+code+",StockCompanyHangyeData");
						stockCompanyHangyeDataTask.init(code);
					}else{
						if(stockCompanyHangyeData.getDate().equals(newOne)||stockCompanyHangyeData.getDate().equals(beforeNewOne)){
							
						}else{
							logger.info("[message]"+code+",StockCompanyHangyeData");
							stockCompanyHangyeDataTask.init(code);
						}
					}
				}
				if(stock.getStop()==0){
					StockCompanySummary stockCompanySummary=stockCompanySummaryMapper.findCode(code);
					if(stockCompanySummary==null||stockCompanySummary.getShangshiriqi()==null){
						logger.info("[message]"+code+",StockCompanySummary");
						stockCompanySummaryTask.init(code);
					}
				}
				if(stock.getStop()==0){
					StockCompanyHangye stockCompanyHangye=stockCompanyHangyeMapper.findCode(code);
					if(stockCompanyHangye==null){
						logger.info("[message]"+code+",StockCompanyHangye");
						stockCompanyHangyeTask.init(code);
					}
				}
				if(stock.getStop()==0){
					StockCompanyInfo stockCompanyInfo=stockCompanyInfoMapper.findCode(code);
					if(stockCompanyInfo==null){
						logger.info("[message]"+code+",StockCompanyInfo");
						stockCompanyInfoTask.init(code);
					}
				}
			}
			
			List<StockDay> stockDays=stockDayMapper.findChangeRateNull();
			for(int j=0;j<stockDays.size();j++){
				StockDay stockDay=stockDays.get(j);
				
				double change_rate=0;
				double change_price=0;
				
				StockDay leftDay=null;
				
				List<StockDay> ll=stockDayMapper.findStockDayLastN(stockDay.getCode(), stockDay.getDt(), 1);
				if(ll.size()==1){
					leftDay=ll.get(0);
				}
				
				if(leftDay==null){
					change_rate=(stockDay.getClose_price()-stockDay.getOpen_price())/stockDay.getOpen_price();
					if(Double.isNaN(change_rate) || Double.isInfinite(change_rate)){
						change_rate=0;
					}
					change_rate=NumUtil.format4(change_rate);
					change_price=NumUtil.format4(stockDay.getClose_price()-stockDay.getOpen_price());
				}else{
					change_rate=(stockDay.getClose_price()-leftDay.getClose_price())/leftDay.getClose_price();
					if(Double.isNaN(change_rate) || Double.isInfinite(change_rate)){
						change_rate=0;
					}
					change_rate=NumUtil.format4(change_rate);
					change_price=NumUtil.format4(stockDay.getClose_price()-leftDay.getClose_price());
				}
				
				stockDay.setChange_rate(change_rate);
				stockDay.setChange_price(change_price);
				
				stockDayMapper.updateChange(stockDay.getCode(), stockDay.getDt(), change_rate, change_price);
			}
			
			List<StockDayFu> stockDayFus=stockDayFuMapper.findChangeRateNull();
			for(int j=0;j<stockDayFus.size();j++){
				StockDayFu stockDay=stockDayFus.get(j);
				
				double change_rate=0;
				double change_price=0;
				
				StockDayFu leftDay=null;
				
				List<StockDayFu> ll=stockDayFuMapper.findStockDayFuLastN(stockDay.getCode(), stockDay.getDt(), 1);
				if(ll.size()==1){
					leftDay=ll.get(0);
				}
				
				if(leftDay==null){
					change_rate=(stockDay.getClose_price()-stockDay.getOpen_price())/stockDay.getOpen_price();
					if(Double.isNaN(change_rate) || Double.isInfinite(change_rate)){
						change_rate=0;
					}
					change_rate=NumUtil.format4(change_rate);
					change_price=NumUtil.format4(stockDay.getClose_price()-stockDay.getOpen_price());
				}else{
					change_rate=(stockDay.getClose_price()-leftDay.getClose_price())/leftDay.getClose_price();
					if(Double.isNaN(change_rate) || Double.isInfinite(change_rate)){
						change_rate=0;
					}
					change_rate=NumUtil.format4(change_rate);
					change_price=NumUtil.format4(stockDay.getClose_price()-leftDay.getClose_price());
				}
				
				stockDay.setChange_rate(change_rate);
				stockDay.setChange_price(change_price);
				
				stockDayFuMapper.updateChange(stockDay.getCode(), stockDay.getDt(), change_rate, change_price);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("[except]"+e.getMessage());
		}
		logger.info("[end]");
		
	}
}
