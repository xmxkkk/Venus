package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyInfoMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanyInfo;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class YewuStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(YewuStockFilter.class);
	@Autowired StockCompanyInfoMapper stockCompanyInfoMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			StockCompanyInfo stock=stockCompanyInfoMapper.findCode(code);
			if(stock==null)return false;
			result= CommonUtil.compareExpressionString(stock.getZhuyingyewu()+","+stock.getChanpinmingcheng(), params);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}
	
}
