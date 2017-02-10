package venus.strategy.stockfilter.filter.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyFinanceMapper;
import venus.dao.StockCompanySummaryMapper;
import venus.helper.util.CommonUtil;
import venus.helper.util.NumUtil;
import venus.model.dao.StockCompanyFinance;
import venus.model.dao.StockCompanySummary;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class PegStockFilter implements StockFilter {
	Logger logger=Logger.getLogger(PegStockFilter.class);
	@Autowired StockCompanyFinanceMapper stockCompanyFinanceMapper;
	@Autowired StockCompanySummaryMapper stockCompanySummaryMapper;
	@Override
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		double peg=0;
		try{
			//select * from stock_company_finance where code='000001' and type='report' and menu = '净利润同比增长率' order by time desc limit 1

			String type="report";
			String menu="净利润同比增长率";
			
			
			List<StockCompanyFinance> lastOne=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, type, menu, 1);
			if(lastOne==null||lastOne.size()==0)return false;
			
			StockCompanyFinance finance=lastOne.get(0);
			
			
			StockCompanySummary stockCompanySummary=stockCompanySummaryMapper.findCode(code);
			if(null==stockCompanySummary)return false;
			Double shiyinglvttm=stockCompanySummary.getShiyinglvttm();
			if(shiyinglvttm==null||shiyinglvttm==0)return false;
			
			if(finance.getValue()==0){
				return false;
			}
			
			peg=NumUtil.format2(stockCompanySummary.getShiyinglvttm()/finance.getValue());
			
			result= CommonUtil.compareExpressionDouble(peg, params);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+peg+","+result);
		return result;
	}

}
