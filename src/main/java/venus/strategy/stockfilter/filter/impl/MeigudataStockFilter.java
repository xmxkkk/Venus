package venus.strategy.stockfilter.filter.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyFinanceMapper;
import venus.helper.exception.BizException;
import venus.helper.util.CommonUtil;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.model.dao.StockCompanyFinance;
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
			Double val=0.;
			
			if(paramss[0].endsWith("同比增长")){
				val=calc(code, paramss[0].substring(0,paramss[0].length()-4), "simple", false);
			}else if(paramss[0].endsWith("同比增长率")){
				val=calc(code, paramss[0].substring(0,paramss[0].length()-5), "simple", true);
			}else{
				List<StockCompanyFinance> list=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, "simple", menu, 1);
				if(list==null||list.size()==0)return false;
				StockCompanyFinance stockCompanyFinance=list.get(0);
				
				val=stockCompanyFinance.getValue();
			}
			/*
			if(paramss[0].equals("基本每股收益同比增长")){
				val=calc(code, "基本每股收益", "simple", false);
			}else if(paramss[0].equals("基本每股收益同比增长率")){
				val=calc(code, "基本每股收益", "simple", true);
			}else if(paramss[0].equals("每股净资产同比增长")){
				val=calc(code, "每股净资产", "simple", false);
			}else if(paramss[0].equals("每股净资产同比增长率")){
				val=calc(code, "每股净资产", "simple", true);
			}else if(paramss[0].equals("每股经营现金流同比增长")){
				val=calc(code, "每股经营现金流", "simple", false);
			}else if(paramss[0].equals("每股经营现金流同比增长率")){
				val=calc(code, "每股经营现金流", "simple", true);
			}else if(paramss[0].equals("每股资本公积金同比增长")){
				val=calc(code, "每股资本公积金", "simple", false);
			}else if(paramss[0].equals("每股资本公积金同比增长率")){
				val=calc(code, "每股资本公积金", "simple", true);
			}else if(paramss[0].equals("每股未分配利润同比增长")){
				val=calc(code, "每股未分配利润", "simple", false);
			}else if(paramss[0].equals("每股未分配利润同比增长率")){
				val=calc(code, "每股未分配利润", "simple", true);
			}else {
				List<StockCompanyFinance> list=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, "simple", menu, 1);
				if(list==null||list.size()==0)return false;
				StockCompanyFinance stockCompanyFinance=list.get(0);
				
				val=stockCompanyFinance.getValue();
			}*/
			
			
			result= CommonUtil.compareExpressionDouble(val, paramss[1]);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}
	private double calc(String code,String menu1,String type,boolean rate){
//		String type="simple";
//		String menu1="基本每股收益";
		List<StockCompanyFinance> list=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, type, menu1, 1);
		if(list==null||list.size()!=1){
			throw new BizException(code, "中断");
		}
		
		StockCompanyFinance twoSCF=list.get(0);
		
		String oneTime=(Integer.parseInt(twoSCF.getTime().substring(0, 4))-1)+twoSCF.getTime().substring(4);
		
		StockCompanyFinance oneSCF=stockCompanyFinanceMapper.find(code, oneTime, menu1, type);
		if(oneSCF==null){
			throw new BizException(code, "中断");
		}
		
		double one=oneSCF.getValue(),two=twoSCF.getValue();
		
		double data=0;
		if(rate){
			data=NumUtil.calcRate(one, two);
		}else{
			data=two-one;
		}
		
		return data;
	}

}
