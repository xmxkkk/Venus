package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class CodeStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(CodeStockFilter.class);

	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{

//			if(params.startsWith("IN")&&params.replace("IN", "").equals(code)){
//				result= true;
//			}else if(params.startsWith("!IN")&&!params.replace("!IN", "").equals(code)){
//				result= true;
//			}
			
			if(params.equals(code)){
				return true;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}

}
