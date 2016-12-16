package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyInfoMapper;
import venus.model.dao.StockCompanyInfo;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class DiyuStockFilter implements StockFilter {
	Logger logger=Logger.getLogger(DiyuStockFilter.class);
	@Autowired StockCompanyInfoMapper stockCompanyInfoMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			StockCompanyInfo stockCompanyInfo=stockCompanyInfoMapper.findCode(code);
			if(stockCompanyInfo==null || stockCompanyInfo.getSuoshudiyu()==null){
				result= false;
			}else{
				String[] paramss=params.split(",");
				
				if(stockCompanyInfo.getSuoshudiyu().equals(paramss[1])){
					result=true;
				}
				
				if(paramss[0].equals("false")){
					result=!result;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}

}
