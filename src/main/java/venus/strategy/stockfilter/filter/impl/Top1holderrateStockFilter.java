package venus.strategy.stockfilter.filter.impl;

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
public class Top1holderrateStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(Top1holderrateStockFilter.class);
	@Autowired StockCompanyHolderTopMapper stockCompanyHolderTopMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			StockCompanyHolderTop stockCompanyHolderTop=stockCompanyHolderTopMapper.findTop1(code, "top10_gudong_in");
			if(stockCompanyHolderTop==null)return false;
			result= CommonUtil.compareExpressionDouble(stockCompanyHolderTop.getStock_rate(), params);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}
}
