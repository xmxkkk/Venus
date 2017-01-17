package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanySummaryMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanySummary;
import venus.strategy.stockfilter.filter.StockFilter;

/**
 * 滚动市盈率
 * @author Administrator
 *
 */
@Component
public class ShiyinglvttmStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(ShiyinglvttmStockFilter.class);
	@Autowired StockCompanySummaryMapper stockCompanySummaryMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			StockCompanySummary stockCompanySummary=stockCompanySummaryMapper.findCode(code);
			if(null==stockCompanySummary)return false;
			Double shiyinglvttm=stockCompanySummary.getShiyinglvttm();
			if(shiyinglvttm==null||shiyinglvttm==0)return false;
			result= CommonUtil.compareExpressionDouble(shiyinglvttm, params);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}

}
