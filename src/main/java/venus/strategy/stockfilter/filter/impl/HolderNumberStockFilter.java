package venus.strategy.stockfilter.filter.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyHolderNumberMapper;
import venus.dao.StockCompanyHolderStructMapper;
import venus.helper.util.CommonUtil;
import venus.helper.util.NumUtil;
import venus.model.dao.StockCompanyHolderNumber;
import venus.model.dao.StockCompanyHolderStruct;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class HolderNumberStockFilter implements StockFilter {
	Logger logger=Logger.getLogger(HolderNumberStockFilter.class);
	@Autowired StockCompanyHolderNumberMapper stockCompanyHolderNumberMapper;
	@Autowired StockCompanyHolderStructMapper stockCompanyHolderStructMapper;
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
			if(paramss[0].equals("人均流通A股变化(季度)")){
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
			/*
			if(paramss[0].equals("流通股本数")){
				StockCompanyHolderStruct stockCompanyHolderStruct=stockCompanyHolderStructMapper.findCodeLast(code);
				double liutongagu=0;
				if(stockCompanyHolderStruct.getLiutongagu()!=null){
					liutongagu=stockCompanyHolderStruct.getLiutongagu();
				}
				double liutongbgu=0;
				if(stockCompanyHolderStruct.getLiutongbgu()!=null){
					liutongbgu=stockCompanyHolderStruct.getLiutongbgu();
				}
				double liutonghgu=0;
				if(stockCompanyHolderStruct.getLiutonghgu()!=null){
					liutonghgu=stockCompanyHolderStruct.getLiutonghgu();
				}
				
				val=liutongagu+liutongbgu+liutonghgu;
				
			}else if(paramss[0].equals("限售股合计")){
				StockCompanyHolderStruct stockCompanyHolderStruct=stockCompanyHolderStructMapper.findCodeLast(code);
				double xianshouagu=0;
				if(stockCompanyHolderStruct.getXianshouagu()!=null){
					xianshouagu=stockCompanyHolderStruct.getXianshouagu();
				}
				double xianshoubgu=0;
				if(stockCompanyHolderStruct.getXianshoubgu()!=null){
					xianshoubgu=stockCompanyHolderStruct.getXianshoubgu();
				}
				double xianshouhgu=0;
				if(stockCompanyHolderStruct.getXianshouhgu()!=null){
					xianshouhgu=stockCompanyHolderStruct.getXianshouhgu();
				}
				
				val=xianshouagu+xianshoubgu+xianshouhgu;
			}else 
			*/	
			if(paramss[0].equals("人均流通A股变化(半年)")){
				
				StockCompanyHolderNumber stockCompanyHolderNumberx=stockCompanyHolderNumberMapper.findCodeLast(code);
				if(stockCompanyHolderNumberx==null)return false;
				
				String oneTime=null;
				String twoTime=stockCompanyHolderNumberx.getTime();
				int year=Integer.parseInt(twoTime.substring(0, 4));
				int month=Integer.parseInt(twoTime.substring(5, 7));
				if(month==3){
					oneTime=(year-1)+"-09-30";
				}else if(month==6){
					oneTime=(year-1)+"-12-31";
				}else if(month==9){
					oneTime=year+"-03-31";
				}else if(month==12){
					oneTime=year+"-06-30";
				}
				
//				String oneTime=(Integer.parseInt(twoTime.substring(0, 4))+1)+twoTime.substring(4);
				//人均流通%股(%股)
				String menux="人均流通%股(%股)";
				StockCompanyHolderNumber oneSCHN=stockCompanyHolderNumberMapper.findCodeTimeMenuLike(code, oneTime, menux);
				StockCompanyHolderNumber twoSCHN=stockCompanyHolderNumberMapper.findCodeTimeMenuLike(code, twoTime, menux);
				if(oneSCHN==null||twoSCHN==null)return false;
				
				double one=0.,two=0.;
				if(oneSCHN.getMenu().contains("(万股)")){
					one=oneSCHN.getValue()*10000;
				}else{
					one=oneSCHN.getValue();
				}
				
				if(twoSCHN.getMenu().contains("(万股)")){
					two=twoSCHN.getValue()*10000;
				}else{
					two=twoSCHN.getValue();
				}
				
				val=NumUtil.calcRate(one, two);
				
			}else{
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
