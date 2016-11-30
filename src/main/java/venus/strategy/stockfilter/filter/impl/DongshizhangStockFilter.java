package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyInfoMapper;
import venus.model.dao.StockCompanyInfo;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class DongshizhangStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(DongshizhangStockFilter.class);
	@Autowired StockCompanyInfoMapper stockCompanyInfoMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			StockCompanyInfo stockCompanyInfo=stockCompanyInfoMapper.findCode(code);
			if(stockCompanyInfo==null){
				result= false;
			}else{
				if(!stockCompanyInfo.getDongshizhang().equals("")&&stockCompanyInfo.getDongshizhang().equals(stockCompanyInfo.getZongjingli()) && stockCompanyInfo.getDongshizhang().equals(stockCompanyInfo.getFarendaibiao())){
					result= true;
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
