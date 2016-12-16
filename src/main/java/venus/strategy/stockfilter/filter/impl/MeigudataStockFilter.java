package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHangyeDataMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanyHangyeData;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class MeigudataStockFilter implements StockFilter {
	Logger logger=Logger.getLogger(MeigudataStockFilter.class);
	@Autowired StockCompanyHangyeDataMapper stockCompanyHangyeDataMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			
			String paramss[]=params.split(",");
			
			
			StockCompanyHangyeData stockCompanyHangyeData=stockCompanyHangyeDataMapper.findLastTime(code);
			if(null==stockCompanyHangyeData)return false;
			
			Double data=null;
			if(paramss[0].equals("每股收益")){
				data=stockCompanyHangyeData.getMeigushouyi();
			}else if(paramss[0].equals("每股净资产")){
				data=stockCompanyHangyeData.getMeigujingzichan();
			}else if(paramss[0].equals("每股现金流")){
				data=stockCompanyHangyeData.getMeiguxianjinliu();
			}
			
			result= CommonUtil.compareExpressionDouble(data, paramss[1]);
			
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}

}
