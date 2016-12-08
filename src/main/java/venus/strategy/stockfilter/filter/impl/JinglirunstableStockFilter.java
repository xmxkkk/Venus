package venus.strategy.stockfilter.filter.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyFinanceMapper;
import venus.model.dao.StockCompanyFinance;
import venus.strategy.stockfilter.filter.StockFilter;

/**
 * 净利润率稳定
 * @author Administrator
 *
 */
@Component
public class JinglirunstableStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(JinglirunstableStockFilter.class);
	@Autowired StockCompanyFinanceMapper stockCompanyFinanceMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=true;
		try{
			List<StockCompanyFinance> stockCompanyFinances=stockCompanyFinanceMapper.findCodeTypeMenu(code, "year", "净利润");
			if(stockCompanyFinances.size()<=1){
				result= false;
			}else{
				for(int i=1;i<stockCompanyFinances.size();i++){
					if(stockCompanyFinances.get(i).getValue()<stockCompanyFinances.get(i-1).getValue()){
						result= false;
						break;
					}
				}
			}
			if(params.equals("false")){
				result=!result;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}
	
}
