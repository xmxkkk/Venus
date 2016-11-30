package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockinfoMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.Stockinfo;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class NameStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(NameStockFilter.class);
	@Autowired StockinfoMapper stockinfoMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			Stockinfo stock=stockinfoMapper.findStockinfo(code);
			if(stock==null)return false;
			result= CommonUtil.compareExpressionString(stock.getName(), params);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}
	
}
