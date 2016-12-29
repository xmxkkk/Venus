package venus.strategy.stockfilter.filter.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyFinanceMapper;
import venus.helper.exception.BizException;
import venus.helper.util.CommonUtil;
import venus.helper.util.NumUtil;
import venus.model.dao.StockCompanyFinance;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class FinancerateStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(FinancerateStockFilter.class);
	@Autowired StockCompanyFinanceMapper stockCompanyFinanceMapper;
	
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			String[] paramss=params.split(",");
			if(paramss.length!=2){
				throw new BizException("0002", "参数错误");
			}
			List<StockCompanyFinance> stockCompanyFinances=stockCompanyFinanceMapper.findCodeTypeMenu(code, "simple", paramss[0]);
			if(stockCompanyFinances.size()==0)return false;
			
			StockCompanyFinance lastStockCompanyFinance=stockCompanyFinances.get(stockCompanyFinances.size()-1);
			
			String endTime=lastStockCompanyFinance.getTime();
			String startTime=endTime.substring(0, 4)+"-01-01";
			List<StockCompanyFinance> current=stockCompanyFinanceMapper.findCodeTypeMenuTime(code, "simple", paramss[0], startTime, endTime);
			if(current.size()==0)return false;
			
			String year=endTime.substring(0, 4);
			
			String endTime2=endTime.replace(year, String.valueOf(Integer.parseInt(year)-1));
			String startTime2=startTime.replace(year, String.valueOf(Integer.parseInt(year)-1));
			
			List<StockCompanyFinance> before=stockCompanyFinanceMapper.findCodeTypeMenuTime(code, "simple", paramss[0], startTime2, endTime2);
			if(before.size()==0)return false;
			
			double currentValue=0;
			for(int i=0;i<current.size();i++){
				currentValue+=current.get(i).getValue();
			}
			
			double beforeValue=0;
			for(int i=0;i<before.size();i++){
				beforeValue+=before.get(i).getValue();
			}
			
			double rate=NumUtil.calcRate(beforeValue, currentValue);
			
			result= CommonUtil.compareExpressionDouble(rate, paramss[1]);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}
	
}
