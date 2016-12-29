package venus.task.analyse;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Component;

import venus.dao.StockDayFuMapper;
import venus.dao.StrategyProcessMapper;
import venus.dao.StrategyResultMapper;
import venus.helper.util.DateUtil;
import venus.helper.util.NumUtil;
import venus.model.dao.StockDayFu;
import venus.model.strategy.StockData;
import venus.model.strategy.StrategyProcess;
import venus.model.strategy.StrategyResult;
import venus.strategy.stock.trade.StockDataCollect;
import venus.strategy.stock.trade.StockTradeStrategy;

@Component
public class StrategySimulationTask extends ApplicationObjectSupport{
	Logger logger=Logger.getLogger(StrategySimulationTask.class);
	@Autowired
	StockDataCollect stockDataCollect;
	@Autowired
	StockDayFuMapper stockDayFuMapper;
	@Autowired
	StrategyResultMapper strategyResultMapper;
	@Autowired
	StrategyProcessMapper strategyProcessMapper;

	public StrategyResult simulate(String code,String strategy_class,String startTime,String endTime,String param){
		logger.info("[start]"+code+","+strategy_class+","+startTime+","+endTime+","+param);
		try{
			//如果结果已经存在就不进行模拟
			StrategyResult existStrategyResult=strategyResultMapper.find(code, strategy_class, startTime, endTime,param);
			if(existStrategyResult!=null && existStrategyResult.getState()==0){
				List<StrategyProcess> strategyProcesses=strategyProcessMapper.findById(existStrategyResult.getId());
				existStrategyResult.setStrategyProcesses(strategyProcesses);
				return existStrategyResult;
			}
			
			//如果没有模拟完就删除继续模拟
			if(existStrategyResult!=null&&existStrategyResult.getState()==1){
				strategyResultMapper.delete(existStrategyResult.getId());
				strategyProcessMapper.delete(existStrategyResult.getId());
			}
			
			double initMoney=1000000.0;
			double money=initMoney;
			double quty=0;
			double buyFee=0.0005;
			double sellFee=0.0005;
			
			StockTradeStrategy stockStrategy=(StockTradeStrategy)getApplicationContext().getBean(strategy_class);
			stockStrategy.initParam(param);
			
			StrategyResult strategyResult=new StrategyResult();
	
			synchronized (this) {
				int id=strategyResultMapper.findNextId();
				strategyResult.setId(id);
				strategyResult.setState(1);
				strategyResult.setStrategy_class(strategy_class);
				strategyResult.setStart_time(startTime);
				strategyResult.setEnd_time(endTime);
				strategyResult.setCode(code);
				strategyResult.setParam(param);
				strategyResultMapper.insert(strategyResult);
			}
			
			List<StrategyProcess> strategyProcesses=new ArrayList<StrategyProcess>();
			List<StockDayFu> stages=stockDayFuMapper.findStockDayFuStageLR(code, startTime, endTime);
	
			double maxDown=Double.MIN_VALUE;
			StockDayFu buyStockDay=null;
			for(int i=0;i<stages.size();i++){
				StockDayFu stockDay=stages.get(i);
				
				StockData stockData=stockDataCollect.collect(stockDay.getCode(), stockDay.getDt());
				
				if(buyStockDay==null){
					if(stockStrategy.buy(stockData)){
						buyStockDay=stages.get(i);
						strategyResult.setBuy_times(strategyResult.getBuy_times()+1);
						//buy
	//					strategyProcessMapper.insert(id,Constant.TRADE$BUY,stockDay.getDt(),stockDay.getOpen_price());
						
						//计算费用和筹码
						quty=NumUtil.format2((money-money*buyFee)/buyStockDay.getOpen_price());
						money=0;
					}
				}else{
					//最大跌落
					double currMaxDown=(stockDay.getOpen_price()-buyStockDay.getOpen_price())/stockDay.getOpen_price();
					if(currMaxDown>maxDown){
						maxDown=currMaxDown;
					}
					
					if(stockStrategy.sell(stockData)){
						strategyResult.setSell_times(strategyResult.getSell_times()+1);
						//sell
	//					strategyProcessMapper.insert(id,Constant.TRADE$SELL,stockDay.getDt(),stockDay.getOpen_price());
						
						money=NumUtil.format2(quty*stockDay.getOpen_price()*(1-sellFee));
						quty=0;
						
						logger.info("sell:code="+stockDay.getCode()+"	,dt="+stockDay.getDt()+"	,buy_price="+buyStockDay.getOpen_price()+"	,sell_price="+stockDay.getOpen_price()+"	,money="+money);
						buyStockDay=null;
					}
				}
			}
			
			if(buyStockDay!=null){
				StockDayFu stockDay=stages.get(stages.size()-1);
				
				//最后一笔交易卖出
				strategyResult.setSell_times(strategyResult.getSell_times()+1);
				//sell
	//			strategyProcessMapper.insert(id,Constant.TRADE$SELL,stockDay.getDt(),stockDay.getOpen_price());
					
				money=NumUtil.format2(quty*stockDay.getOpen_price()*(1-sellFee));
				quty=0;
					
				logger.info("sell:code="+stockDay.getCode()+"	,dt="+stockDay.getDt()+"	,buy_price="+buyStockDay.getOpen_price()+"	,sell_price="+stockDay.getOpen_price()+"	,money="+money);
				buyStockDay=null;
			}
			
			int tradeDay=DateUtil.datediff(startTime, endTime);
			
			StockDayFu startStockDay=stockDayFuMapper.findDtNear(code, startTime);
			StockDayFu endStockDay=stockDayFuMapper.findDtNear(code, endTime);
			double profit_rate_standard=0;
			double profit_rate_standard_year=0;
			if(startStockDay!=null && endStockDay!=null){
				double startPrice=startStockDay.getOpen_price();
				double endPrice=endStockDay.getOpen_price();
				profit_rate_standard=(endPrice-startPrice)/startPrice;
				if(Double.isNaN(profit_rate_standard) || Double.isInfinite(profit_rate_standard)){
					profit_rate_standard=0;
				}
				profit_rate_standard_year=(profit_rate_standard/tradeDay*365);
				if(Double.isNaN(profit_rate_standard_year) || Double.isInfinite(profit_rate_standard_year)){
					profit_rate_standard_year=0;
				}
			}
			
			double profit_rate=(money-initMoney)/initMoney;
			double profit_rate_year=(profit_rate/tradeDay*365);
			double mar=profit_rate_year/maxDown;
			if(Double.isNaN(mar) || Double.isInfinite(mar)){
				mar=0;
			}
			
			strategyResult.setMax_down(maxDown);
			strategyResult.setMar(mar);
			strategyResult.setStrategyProcesses(strategyProcesses);
			strategyResult.setState(0);
			strategyResult.setProfit_rate(profit_rate);
			strategyResult.setProfit_rate_year(profit_rate_year);
			strategyResult.setProfit_rate_standard(profit_rate_standard);
			strategyResult.setProfit_rate_standard_year(profit_rate_standard_year);
			
			strategyResultMapper.update(strategyResult);
			logger.info("[end]"+strategyResult);
			
			return strategyResult;
		}catch(Exception e){
			logger.error("[except]",e);
		}
		return null;
	}
	
	public void clearInvalidSimulation(){
		
		List<StrategyResult> strategyResults=strategyResultMapper.findState(1);
		for(int i=0;i<strategyResults.size();i++){
			StrategyResult strategyResult=strategyResults.get(i);
			simulate(strategyResult.getCode(), strategyResult.getStrategy_class(), strategyResult.getStart_time(), strategyResult.getEnd_time(), strategyResult.getParam());
			strategyResultMapper.delete(strategyResult.getId());
			strategyProcessMapper.delete(strategyResult.getId());
		}
		
	}
	
	public void updateProfitRateStandard(){
		List<StrategyResult> list=strategyResultMapper.findAll();
		for(int i=0;i<list.size();i++){
			double profit_rate_standard=0;
			double profit_rate_standard_year=0;
			
			StrategyResult x=list.get(i);
			StockDayFu startStockDay=stockDayFuMapper.findDtNear(x.getCode(), x.getStart_time());
			StockDayFu endStockDay=stockDayFuMapper.findDtNear(x.getCode(), x.getEnd_time());
			
			if(startStockDay!=null && endStockDay!=null){
				double startPrice=startStockDay.getOpen_price();
				double endPrice=endStockDay.getOpen_price();
				int tradeDay=DateUtil.datediff(startStockDay.getDt(), endStockDay.getDt());
				
				profit_rate_standard=(endPrice-startPrice)/startPrice;
				if(Double.isNaN(profit_rate_standard) || Double.isInfinite(profit_rate_standard)){
					profit_rate_standard=0;
				}
				profit_rate_standard_year=(profit_rate_standard/tradeDay*365);
				if(Double.isNaN(profit_rate_standard_year) || Double.isInfinite(profit_rate_standard_year)){
					profit_rate_standard_year=0;
				}
			}
			
			strategyResultMapper.updateProfitRateStandard(x.getId(), profit_rate_standard,profit_rate_standard_year);
		}
	}
	
}
