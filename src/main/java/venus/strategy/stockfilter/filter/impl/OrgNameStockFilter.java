package venus.strategy.stockfilter.filter.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHolderOrgMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanyHolderOrg;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class OrgNameStockFilter implements StockFilter {
	Logger logger=Logger.getLogger(OrgNameStockFilter.class);
	@Autowired StockCompanyHolderOrgMapper stockCompanyHolderOrgMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			List<StockCompanyHolderOrg> list=stockCompanyHolderOrgMapper.findCode(code);
			
			StringBuffer sb=new StringBuffer();
			for (StockCompanyHolderOrg stockCompanyHolderOrg : list) {
				sb.append("[!!"+stockCompanyHolderOrg.getName()+"!!]");
			}
			
			result= CommonUtil.compareExpressionString(sb.toString(), params);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}
}
