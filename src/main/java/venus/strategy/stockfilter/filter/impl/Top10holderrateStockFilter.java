package venus.strategy.stockfilter.filter.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHolderTopMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanyHolderTop;
import venus.strategy.stockfilter.filter.StockFilter;

/**
 * 控股股东持有量
 * @author Administrator
 *
 */
@Component
public class Top10holderrateStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(Top10holderrateStockFilter.class);
	@Autowired StockCompanyHolderTopMapper stockCompanyHolderTopMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		double rate=0;
		try{
			List<StockCompanyHolderTop> stockCompanyHolderTops=stockCompanyHolderTopMapper.findTop(code, "top10_gudong_in",10);
			if(stockCompanyHolderTops==null||stockCompanyHolderTops.size()==0)return false;
			
			for (StockCompanyHolderTop stockCompanyHolderTop2 : stockCompanyHolderTops) {
				rate+=stockCompanyHolderTop2.getStock_rate();
			}
			result= CommonUtil.compareExpressionDouble(rate, params);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+rate+","+params+","+result);
		return result;
	}
}
