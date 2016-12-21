package venus.strategy.stockfilter.filter.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyFinanceMapper;
import venus.dao.StockCompanyHangyeDataMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanyFinance;
import venus.model.dao.StockCompanyHangyeData;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class MeigudataStockFilter implements StockFilter {
	Logger logger=Logger.getLogger(MeigudataStockFilter.class);
	@Autowired StockCompanyFinanceMapper stockCompanyFinanceMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		try{
			
			String paramss[]=params.split(",");
			
			//基本每股收益,每股净资产,每股经营现金流,每股资本公积金,每股未分配利润
			
			String menu=paramss[0];
//			if(paramss[0].equals("基本每股收益")){
//				menu="基本每股收益";
//			}else if(paramss[0].equals("每股净资产")){
//				menu="每股净资产";
//			}else if(paramss[0].equals("每股经营现金流")){
//				menu="每股经营现金流";
//			}else if(paramss[0].equals("每股资本公积金")){
//				menu="每股资本公积金";
//			}else if(paramss[0].equals("每股未分配利润")){
//				menu="每股未分配利润";
//			}
			List<StockCompanyFinance> list=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, "simple", menu, 1);
			if(list==null||list.size()==0)return false;
			StockCompanyFinance stockCompanyFinance=list.get(0);
			
			Double data=stockCompanyFinance.getValue();
			result= CommonUtil.compareExpressionDouble(data, paramss[1]);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}

}
