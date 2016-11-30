package venus.strategy.stockfilter.filter.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockCompanyFinanceMapper;
import venus.helper.util.CommonUtil;
import venus.model.dao.StockCompanyFinance;
import venus.strategy.stockfilter.filter.StockFilter;

@Component
public class XianjinliuStockFilter implements StockFilter{
	Logger logger=Logger.getLogger(XianjinliuStockFilter.class);
	@Autowired StockCompanyFinanceMapper stockCompanyFinanceMapper;
	@Override
	public boolean filter(String code, String params) {
		logger.info("[start]"+code+","+params);
		boolean result=false;
		double liuruVal=0;
		try{
			String[] paramss=params.split(",");
			
			if(paramss[0].equals("现金")){
				double jingying=calc(code, "经营现金");
				double chouzi=calc(code, "筹资现金");
				double touzi=calc(code, "投资现金");
				
				liuruVal=chouzi+jingying-touzi;
			}else{
				liuruVal=calc(code, paramss[0]);
			}
			
			result=CommonUtil.compareExpressionDouble(liuruVal, paramss[1]);
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[except]"+e.getMessage());
		}
		logger.info("[start]"+code+","+params+","+result+","+liuruVal);
		return result;
	}
	
	private double calc(String code,String param){
		double liuruVal=0;
		List<StockCompanyFinance> liuru=stockCompanyFinanceMapper.findCodeTypeMenu(code, "simple", param+"流入");
		List<StockCompanyFinance> liuchu=stockCompanyFinanceMapper.findCodeTypeMenu(code, "simple", param+"流出");
		if(liuru.size()==0 || liuchu.size()==0)return 0;

		if(liuru.size()>=4){
			liuru=liuru.subList(liuru.size()-4, liuru.size());
		}
		if(liuchu.size()>=4){
			liuchu=liuchu.subList(liuchu.size()-4, liuchu.size());
		}
		
		for(int i=0;i<liuru.size();i++){
			liuruVal+=liuru.get(i).getValue();
		}
		
		double liuchuVal=0;
		for(int i=0;i<liuchu.size();i++){
			liuchuVal+=liuchu.get(i).getValue();
		}
		
		liuruVal-=liuchuVal;
		
		return liuruVal;
	}
}
