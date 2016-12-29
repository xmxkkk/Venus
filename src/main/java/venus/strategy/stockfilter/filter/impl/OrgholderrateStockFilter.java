package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHolderOrgMapper;
import venus.helper.util.CommonUtil;
import venus.strategy.stockfilter.filter.StockFilter;

/**
 * 机构持有率
 * @author Administrator
 *
 */
@Component
public class OrgholderrateStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(OrgholderrateStockFilter.class);
	@Autowired StockCompanyHolderOrgMapper stockCompanyHolderOrgMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			String date=stockCompanyHolderOrgMapper.findLastTime(code);
			Double rate=stockCompanyHolderOrgMapper.findCodeTotalRate(code,date);
			if(rate==null)return false;
			result= CommonUtil.compareExpressionDouble(rate, params);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}
}
