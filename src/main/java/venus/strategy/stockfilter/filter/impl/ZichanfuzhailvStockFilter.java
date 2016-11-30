package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHangyeDataMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanyHangyeData;
import venus.strategy.stockfilter.filter.StockFilter;

/**
 * 资产负债率
 * @author Administrator
 *
 */
@Component
public class ZichanfuzhailvStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(ZichanfuzhailvStockFilter.class);
	@Autowired StockCompanyHangyeDataMapper stockCompanyHangyeDataMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			StockCompanyHangyeData stockCompanyHangyeData=stockCompanyHangyeDataMapper.findNew(code);
			if(stockCompanyHangyeData==null)return false;
			result= CommonUtil.compareExpressionDouble(stockCompanyHangyeData.getZichanfuzhailv(), params);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}
}
