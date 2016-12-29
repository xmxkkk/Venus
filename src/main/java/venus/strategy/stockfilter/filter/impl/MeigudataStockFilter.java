package venus.strategy.stockfilter.filter.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyFinanceMapper;
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
			Double data=0.;
			if(paramss[0].equals("每股收益同比增长率")){
				String type="report";
				List<StockCompanyFinance> list=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, type, "基本每股收益", 1);
				if(list==null||list.size()!=1)return  false;
				
				StockCompanyFinance twoSCF=list.get(0);
				
				String oneTime=(Integer.parseInt(twoSCF.getTime().substring(0, 4))-1)+twoSCF.getTime().substring(4);
				
				StockCompanyFinance oneSCF=stockCompanyFinanceMapper.find(code, oneTime, "基本每股收益", type);
				if(oneSCF==null)return false;
				
				double one=oneSCF.getValue(),two=twoSCF.getValue();
				
				data=NumUtil.calcRate(one, two);
				
			}else if(paramss[0].equals("每股收益同比增长")){
				String type="report";
				List<StockCompanyFinance> list=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, type, "基本每股收益", 1);
				if(list==null||list.size()!=1)return  false;
				
				StockCompanyFinance twoSCF=list.get(0);
				
				String oneTime=(Integer.parseInt(twoSCF.getTime().substring(0, 4))-1)+twoSCF.getTime().substring(4);
				
				StockCompanyFinance oneSCF=stockCompanyFinanceMapper.find(code, oneTime, "基本每股收益", type);
				if(oneSCF==null)return false;
				
				double one=oneSCF.getValue(),two=twoSCF.getValue();
				
				data=two-one;
				
			}else if(paramss[0].equals("每股收益增长率")){
				
				List<StockCompanyFinance> list=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, "simple", "基本每股收益", 1);
				if(list==null||list.size()!=1)return false;

				List<String> days=DateUtil.financeDay(null, list.get(0).getTime(), 5);
				
				List<StockCompanyFinance> oneList=stockCompanyFinanceMapper.findCodeTypeMenuTime(code, "simple", "基本每股收益", days.get(4), days.get(1));
				List<StockCompanyFinance> twoList=stockCompanyFinanceMapper.findCodeTypeMenuTime(code, "simple", "基本每股收益", days.get(3), days.get(0));
				
				if(!(oneList.size()==4&&twoList.size()==4)){
					return false;
				}
				
				double one=0.,two=0.;
				for(int i=0;i<oneList.size();i++){
					one+=oneList.get(i).getValue();
				}
				for(int i=0;i<twoList.size();i++){
					two+=twoList.get(i).getValue();
				}
				
				data=NumUtil.calcRate(one, two);
				
			}else{
				List<StockCompanyFinance> list=stockCompanyFinanceMapper.findCodeTypeMenuLastLimit(code, "simple", menu, 1);
				if(list==null||list.size()==0)return false;
				StockCompanyFinance stockCompanyFinance=list.get(0);
				
				data=stockCompanyFinance.getValue();
			}
			result= CommonUtil.compareExpressionDouble(data, paramss[1]);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result);
		return result;
	}

}
