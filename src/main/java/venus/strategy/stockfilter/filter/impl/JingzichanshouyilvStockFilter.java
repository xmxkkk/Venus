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

/**
 * 净资产收益率
 * @author Administrator
 *
 */
@Component
public class JingzichanshouyilvStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(JingzichanshouyilvStockFilter.class);
	@Autowired StockCompanySummaryMapper stockCompanySummaryMapper;
	@Autowired StockCompanyFinanceMapper stockCompanyFinanceMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			String[] paramss=params.split(",");

			double val=0;
			if(paramss[0].equals("净资产收益率")){
				StockCompanySummary stockCompanySummary=stockCompanySummaryMapper.findCode(code);
				if(null==stockCompanySummary)return false;
				val=stockCompanySummary.getJingzichanshouyilv();
			}else if(paramss[0].equals("净资产收益率同比增长")){
				String type="report";
				String menu="净资产收益率";
				List<StockCompanyFinance> list=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, type, menu, 1);
				if(list==null||list.size()!=1)return  false;
				
				StockCompanyFinance twoSCF=list.get(0);
				
				String oneTime=(Integer.parseInt(twoSCF.getTime().substring(0, 4))-1)+twoSCF.getTime().substring(4);
				
				StockCompanyFinance oneSCF=stockCompanyFinanceMapper.find(code, oneTime, menu, type);
				if(oneSCF==null)return false;
				
				double one=oneSCF.getValue(),two=twoSCF.getValue();
				
				val=two-one;
			}else if(paramss[0].equals("净资产收益率同比增长率")){
				String type="report";
				String menu="净资产收益率";
				List<StockCompanyFinance> list=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, type, menu, 1);
				if(list==null||list.size()!=1)return  false;
				
				StockCompanyFinance twoSCF=list.get(0);
				
				String oneTime=(Integer.parseInt(twoSCF.getTime().substring(0, 4))-1)+twoSCF.getTime().substring(4);
				
				StockCompanyFinance oneSCF=stockCompanyFinanceMapper.find(code, oneTime, menu, type);
				if(oneSCF==null)return false;
				
				double one=oneSCF.getValue(),two=twoSCF.getValue();
				
				val=NumUtil.calcRate(one, two);
			}
			
			result= CommonUtil.compareExpressionDouble(val, paramss[1]);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}
	
}
