package venus.strategy.stockfilter.filter.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHolderCansellMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanyHolderCansell;
import venus.strategy.stockfilter.filter.StockFilter;

/**
 * 解禁股票占有率
 * @author Administrator
 *
 */
@Component
public class CansellStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(CansellStockFilter.class);
	@Autowired StockCompanyHolderCansellMapper stockCompanyHolderCansellMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			String[] paramss=params.split(",");
			if(paramss.length!=3){
				throw new RuntimeException("参数不正确");
			}
			int day1=Integer.parseInt(paramss[0]);
			int day2=Integer.parseInt(paramss[1]);
			
			List<StockCompanyHolderCansell> stockCompanyHolderCansells=stockCompanyHolderCansellMapper.findCodeDay(code, day1,day2);
			double rate=0;
			for(int i=0;i<stockCompanyHolderCansells.size();i++){
				rate+=stockCompanyHolderCansells.get(i).getRate();
			}
			result= CommonUtil.compareExpressionDouble(rate, paramss[2]);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}
	
}
