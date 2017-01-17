package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanySummaryMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanySummary;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class ShijinglvStockFilter implements StockFilter {
	Logger logger=Logger.getLogger(ShijinglvStockFilter.class);
	@Autowired StockCompanySummaryMapper stockCompanySummaryMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			StockCompanySummary stockCompanySummary=stockCompanySummaryMapper.findCode(code);
			if(null==stockCompanySummary)return false;
			if (stockCompanySummary.getShijinglv()==null||stockCompanySummary.getShijinglv()==0.0) {
				return false;
			}
			Double shijinglv=stockCompanySummary.getShijinglv();
			if(shijinglv==null||shijinglv==0.0)return false;
			result= CommonUtil.compareExpressionDouble(shijinglv, params);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}

}
