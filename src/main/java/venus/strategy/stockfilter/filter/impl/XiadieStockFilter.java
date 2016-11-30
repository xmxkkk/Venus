package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.IndexDayMapper;
import venus.dao.StockDayFuMapper;
import venus.helper.util.CommonUtil;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.model.dao.IndexDay;
import venus.model.dao.StockDayFu;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class XiadieStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(XiadieStockFilter.class);
	@Autowired StockDayFuMapper stockDayFuMapper;
	@Autowired IndexDayMapper indexDayMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		double rate=0;
		try{
			String[] paramss=params.split(",");

			String endDate=DateUtil.date();
			String startDate=DateUtil.date(endDate, -Integer.parseInt(paramss[0]));
			
			String indexCode="sh000300";
			IndexDay endIndexDay=indexDayMapper.findDtNear(indexCode, endDate);
			IndexDay startIndexDay=indexDayMapper.findDtNear(indexCode, startDate);
			
			double indexRate=(endIndexDay.getClose_price()-startIndexDay.getClose_price())/startIndexDay.getClose_price();
			indexRate=NumUtil.format2(indexRate*100);
			
			StockDayFu endStockDayFu=stockDayFuMapper.findDtNear(code, endDate);
			StockDayFu startStockDayFu=stockDayFuMapper.findDtNear(code, startDate);
			
			rate=(endStockDayFu.getClose_price()-startStockDayFu.getClose_price())/startStockDayFu.getClose_price();
			rate=NumUtil.format2(rate*100);
			
			rate-=indexRate;
			
			result=CommonUtil.compareExpressionDouble(rate, paramss[1]);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result+","+rate);
		return result;
	}

}
