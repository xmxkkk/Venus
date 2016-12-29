package venus.task.analyse;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import venus.dao.IndexDayMapper;
import venus.dao.StockCompanyFinanceRateMapper;
import venus.dao.StockDayFuMapper;
import venus.dao.StockinfoMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.model.dao.IndexDay;
import venus.model.dao.StockCompanyFinanceRate;
import venus.model.dao.StockDayFu;
import venus.model.dao.Stockinfo;
import venus.strategy.back.StockData;

@Component
public class StrategySimulation2Task extends ApplicationObjectSupport{
	Logger logger=Logger.getLogger(StrategySimulation2Task.class);
	@Autowired StockData stockData;
	@Autowired StockinfoMapper stockinfoMapper;
	@Autowired StockDayFuMapper stockDayFuMapper;
	@Autowired IndexDayMapper indexDayMapper;
	@Autowired StockCompanyFinanceRateMapper stockCompanyFinanceRateMapper;
	public void simulate(){
		logger.info("[start]");
		try{
//			BackChoose backChoose=(BackChoose)getApplicationContext().getBean("backChoose1");
			
			String[] years={"2000","2001","2002","2003","2004","2005","2006","2007","2008","2009","2010","2011","2012","2013","2014","2015","2016"};
			
			for(int m=0;m<years.length;m++){
				String startTime=(Integer.parseInt(years[m])-1)+"1231";
				String endTime=(Integer.parseInt(years[m]))+"0630";
				
				String buyTime=startTime;//(Integer.parseInt(years[m]))+"0630";
				
				double avgRateRelative=0;
				double avgRateAbsolute=0;
				int cnt=0;
				
				List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
				for(Stockinfo stock:stocks){
					String code=stock.getCode();
					
					StockCompanyFinanceRate stockCompanyFinanceRate=stockCompanyFinanceRateMapper.find(code, DateUtil.convert(startTime), "经营现金流量净额");
					if(stockCompanyFinanceRate!=null&&stockCompanyFinanceRate.getRate()>-10 && stockCompanyFinanceRate.getRate()<0){
						avgRateRelative+=calcChangeRateRelative(stock.getCode(), buyTime, endTime);
						avgRateAbsolute+=calcChangeRateAbsolute(stock.getCode(), buyTime, endTime);
						cnt++;
					}
				}
				if(cnt>0){
					avgRateRelative=NumUtil.format2(avgRateRelative/cnt);
					avgRateAbsolute=NumUtil.format2(avgRateAbsolute/cnt);
				}
				
				//logger.info("[message]"+years[m]+"	,"+cnt+"	,"+avgRateRelative+"	,"+avgRateAbsolute);
				
				System.out.println(years[m]+"	,"+cnt+"	,"+avgRateRelative+"	,"+avgRateAbsolute);
			}
			
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]");
	}
	/*
	public void simulate(){
		logger.info("[start]");
		try{
			String endTime="20110930";
			String startTime="20110331";
			
			BackChoose backChoose=(BackChoose)getApplicationContext().getBean("backChoose1");
			
			double avgRate=0;
			int cnt=0;
			
			List<Stockinfo> stocks=stockinfoMapper.findStockinfos();
			for(Stockinfo stock:stocks){
				stockData.collect(stock.getCode(), startTime);
				System.out.println(stock.getCode());
				if(backChoose.choose(stockData)){
					logger.info("[message]"+stock.getCode());
					avgRate+=calcChangeRate(stock.getCode(), startTime, endTime);
					cnt++;
				}
			}
			
			logger.info("[message]"+avgRate+","+cnt);
			avgRate=NumUtil.format2(avgRate/cnt);
			
			logger.info("[message]"+avgRate);
			
		}catch(Exception e){
			logger.error("[except]",e);
		}
		logger.info("[end]");
	}*/
	
	private double calcChangeRateRelative(String code,String startTime,String endTime){
		StockDayFu startStockDayFu=stockDayFuMapper.findDtNear(code, startTime);
		StockDayFu endStockDayFu=stockDayFuMapper.findDtNear(code, endTime);
		if(endStockDayFu.getClose_price()==0)return 0;
		double stockRate=NumUtil.format2(100.0*(endStockDayFu.getClose_price()-startStockDayFu.getClose_price())/startStockDayFu.getClose_price());

		String indexCode="sh000300";
		IndexDay startIndexDay=indexDayMapper.findDtNear(indexCode, startTime);
		IndexDay endIndexDay=indexDayMapper.findDtNear(indexCode, endTime);
		if(endIndexDay.getClose_price()==0)return 0;
		double indexRate=NumUtil.format2(100.0*(endIndexDay.getClose_price()-startIndexDay.getClose_price())/startIndexDay.getClose_price());
		
		return stockRate-indexRate;
	}
	private double calcChangeRateAbsolute(String code,String startTime,String endTime){
		StockDayFu startStockDayFu=stockDayFuMapper.findDtNear(code, startTime);
		StockDayFu endStockDayFu=stockDayFuMapper.findDtNear(code, endTime);
		if(endStockDayFu.getClose_price()==0)return 0;
		double stockRate=NumUtil.format2(100.0*(endStockDayFu.getClose_price()-startStockDayFu.getClose_price())/startStockDayFu.getClose_price());

		
		return stockRate;
	}
	
	
}
