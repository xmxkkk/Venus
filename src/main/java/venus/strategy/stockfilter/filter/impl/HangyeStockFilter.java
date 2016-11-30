package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHangyeMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanyHangye;
import venus.strategy.stockfilter.filter.StockFilter;

/**
 * 行业
 * @author Administrator
 *
 */
@Component
public class HangyeStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(HangyeStockFilter.class);
	@Autowired StockCompanyHangyeMapper stockCompanyHangyeMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			StockCompanyHangye stockCompanyHangye=stockCompanyHangyeMapper.findCode(code);
			if(stockCompanyHangye==null)return false;
			
			if(params.split(",")[0].equals("1")){
				result= CommonUtil.compareExpressionString(stockCompanyHangye.getLevel1(), params.split(",")[1]);
			}else if(params.split(",")[0].equals("2")){
				result= CommonUtil.compareExpressionString(stockCompanyHangye.getLevel2(), params.split(",")[1]);
			}else if(params.split(",")[0].equals("3")){
				result= CommonUtil.compareExpressionString(stockCompanyHangye.getLevel3(), params.split(",")[1]);
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}
	
}
