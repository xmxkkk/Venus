package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockDayMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockDay;
import venus.strategy.stockfilter.filter.StockFilter;

/**
 * 价格
 * @author Administrator
 *
 */
@Component
public class PriceStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(PriceStockFilter.class);
	@Autowired StockDayMapper stockDayMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			StockDay stockDay=stockDayMapper.findLast(code);
			if(stockDay==null)return false;
			result= CommonUtil.compareExpressionDouble(stockDay.getClose_price(), params);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}
	
}
