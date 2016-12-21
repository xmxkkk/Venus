package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHolderNumberMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanyHolderNumber;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class HolderNumberStockFilter implements StockFilter {
	Logger logger=Logger.getLogger(HolderNumberStockFilter.class);
	@Autowired StockCompanyHolderNumberMapper stockCompanyHolderNumberMapper;
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		double val=0;
		try{
			
//			人均流通变化,人均流通股,股东总人数,较上期变化,股东较上季度变化
//			人均流通变化
//			人均流通股(万股)
//			股东总人数(万户)
//			行业平均(万户)
//			较上期变化
			
			StockCompanyHolderNumber stockCompanyHolderNumber=null;
			
			String[] paramss=params.split(",");
			
			String menu=null;
			if(paramss[0].equals("人均流通A股变化")){
				menu="人均流通%变化";
			}else if(paramss[0].equals("人均流通A股")){
				menu="人均流通%股(%股)";//
			}else if(paramss[0].equals("股东总人数")){
				menu="股东总人数(%户)";//(万户)
			}else if(paramss[0].equals("股东人数较上期变化")){
				menu="较上期变化";//A股股东数变化
				
				stockCompanyHolderNumber=stockCompanyHolderNumberMapper.findCodeLast(code);
				
				String lastTime=stockCompanyHolderNumber.getTime();
				
				stockCompanyHolderNumber=stockCompanyHolderNumberMapper.findCodeMenuTime(code, menu, lastTime);
				if(stockCompanyHolderNumber==null){
					stockCompanyHolderNumber=stockCompanyHolderNumberMapper.findCodeMenuTime(code, "A股股东数变化", lastTime);
				}
			}
			if(stockCompanyHolderNumber==null){
				stockCompanyHolderNumber=stockCompanyHolderNumberMapper.findCodeMenuLast(code,menu);
			}
			if(stockCompanyHolderNumber==null)return false;
			
			
			if(paramss[0].equals("人均流通A股变化")){
				val=stockCompanyHolderNumber.getValue();
			}else if(paramss[0].equals("人均流通A股")){
				if(stockCompanyHolderNumber.getMenu().contains("(万股)")){
					val=stockCompanyHolderNumber.getValue()*10000;
				}else{
					val=stockCompanyHolderNumber.getValue();
				}
			}else if(paramss[0].equals("股东总人数")){
				if(stockCompanyHolderNumber.getMenu().contains("(万户)")){
					val=stockCompanyHolderNumber.getValue()*10000;
				}else{
					val=stockCompanyHolderNumber.getValue();
				}
			}else if(paramss[0].equals("股东人数较上期变化")){
				val=stockCompanyHolderNumber.getValue();
			}
			result= CommonUtil.compareExpressionDouble(val, paramss[1]);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+val+","+params+","+result);
		return result;
	}
	
}
