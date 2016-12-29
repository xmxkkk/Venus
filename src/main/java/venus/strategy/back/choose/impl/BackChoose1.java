package venus.strategy.back.choose.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import venus.helper.util.NumUtil;
import venus.model.dao.StockCompanyFinance;
import venus.strategy.back.StockData;
import venus.strategy.back.choose.BackChoose;

@Component
public class BackChoose1 implements BackChoose{
	Logger logger=Logger.getLogger(BackChoose1.class);
	@Override
	public boolean choose(StockData stockData) {
		logger.info("[start]");
		boolean result=false;
		double jingyingRate=0;
		double touziRate=0;
		double chouziRate=0;
		try{
			//投资现金流量净额
			//筹资现金流量净额
			//经营现金流量净额
			List<StockCompanyFinance> stockCompanyFinances=stockData.getStockCompanyFinances();
			jingyingRate=find(stockCompanyFinances, "经营现金流量净额");
			/*
			stockCompanyFinances=stockData.getStockCompanyFinances();
			touziRate=find(stockCompanyFinances, "投资现金流量净额");
			
			stockCompanyFinances=stockData.getStockCompanyFinances();
			chouziRate=find(stockCompanyFinances, "筹资现金流量净额");*/
			
			if(jingyingRate>-10&&jingyingRate<0){
				result= true;
			}
		}catch(Exception e){
			logger.error("[except]",e);
		}
		
		logger.info("[end]"+jingyingRate+"	,"+touziRate+"	,"+"	,"+chouziRate+"	,"+result);
		return result;
	}
	private double find(List<StockCompanyFinance> stockCompanyFinances,String menu){
		//menu:筹资现金流量净额
		List<StockCompanyFinance> list=new ArrayList<StockCompanyFinance>();
		for(int i=0;i<stockCompanyFinances.size();i++){
			if(stockCompanyFinances.get(i).getMenu().equals(menu)&&stockCompanyFinances.get(i).getType().equals("simple")){
				list.add(stockCompanyFinances.get(i));
			}
		}
		stockCompanyFinances=list;
		
		if(stockCompanyFinances.size()<8)return 0;
		
		List<StockCompanyFinance> last4=stockCompanyFinances.subList(stockCompanyFinances.size()-4, stockCompanyFinances.size());
		List<StockCompanyFinance> last8=stockCompanyFinances.subList(stockCompanyFinances.size()-8, stockCompanyFinances.size()-4);
		
		double val8=0;
		for(int i=0;i<last8.size();i++){
			val8+=last8.get(0).getValue();
		}
		
		double val4=0;
		for(int i=0;i<last4.size();i++){
			val4+=last4.get(0).getValue();
		}
		
		if(val8==0)return 0;
		
		double rate=NumUtil.format2(100.0*(val4-val8)/val8);
		
		return rate;
	}
}
