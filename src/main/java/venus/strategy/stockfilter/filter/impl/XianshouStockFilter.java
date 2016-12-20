package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHolderStructMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanyHolderStruct;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class XianshouStockFilter implements StockFilter {
	Logger logger=Logger.getLogger(XianshouStockFilter.class);
	@Autowired StockCompanyHolderStructMapper stockCompanyHolderStructMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		double xianshou=0;
		try{
			StockCompanyHolderStruct stockCompanyHolderStruct=stockCompanyHolderStructMapper.findCodeLast(code);
			if(stockCompanyHolderStruct==null)return false;
			
			if(stockCompanyHolderStruct.getZongguben()==null)return false;
			
			double xianshouagu=0;
			if(null!=stockCompanyHolderStruct.getXianshouagu()){
				xianshouagu=stockCompanyHolderStruct.getXianshouagu();
			}
			
			double xianshoubgu=0;
			if(null!=stockCompanyHolderStruct.getXianshoubgu()){
				xianshoubgu=stockCompanyHolderStruct.getXianshoubgu();
			}
			
			double xianshouhgu=0;
			if(null!=stockCompanyHolderStruct.getXianshouhgu()){
				xianshouhgu=stockCompanyHolderStruct.getXianshouhgu();
			}
			
			xianshou=xianshouagu+xianshoubgu+xianshouhgu;
			xianshou=xianshou*100/stockCompanyHolderStruct.getZongguben();
			
			result= CommonUtil.compareExpressionDouble(xianshou, params);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+xianshou+","+params+","+result);
		return result;
	}
}
