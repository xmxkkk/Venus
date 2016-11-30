package venus.strategy.stock.trade.impl;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockDayFuMapper;
import venus.dao.StrategyResultMapper;
import venus.model.dao.StockDayFu;
import venus.model.strategy.StockData;
import venus.model.strategy.StrategyResult;
import venus.strategy.stock.trade.StockTradeStrategy;
import venus.task.analyse.StrategySimulationTask;
/**
 * 动态长期均线
 * @author Administrator
 *
 */
@Component
public class StockStrategy010 implements StockTradeStrategy{
	@Autowired
	StrategyResultMapper strategyResultMapper;
	@Autowired
	StockDayFuMapper stockDayFuMapper;
	@Autowired
	StrategySimulationTask strategySimulationTask;
	
	//1profitRateYear>0   2not limit
	private int profitLevel=0;
	
	private int buyMaN;
	private int sellMaN;
	public void initParam(String param){
		profitLevel=Integer.parseInt(param);
	}
	
	private boolean pri(StockData stockData){
		int N20=90;
		List<StockDayFu> stockDays=stockData.getStockDays();
		if(stockDays.size()<N20){
			return false;
		}
		
		final StockDayFu stockDay=stockDays.get(stockDays.size()-1);
		
		List<StockDayFu> tempList=stockDayFuMapper.findStockDayFuLastN(stockDay.getCode(), stockDay.getDt(), N20);
		final StockDayFu startStockDay=tempList.get(0);
		
		int n=strategyResultMapper.findCount(stockDay.getCode(), "stockStrategy001", startStockDay.getDt(), stockDay.getDt());
		
		ExecutorService fixedThreadPool=null;
		if(n<64){
			fixedThreadPool = Executors.newFixedThreadPool(30); 
			for(int x=20;x<=90;x+=10){
				for(int y=20;y<=90;y+=10){
					final int ii=x;
					final int jj=y;
					fixedThreadPool.execute(new Runnable() {
						public void run() {
							strategySimulationTask.simulate(stockDay.getCode(), "stockStrategy001", startStockDay.getDt(), stockDay.getDt(), ii+"#"+jj);
						}
					});
				}
			}
		}
		while(true){
			n=strategyResultMapper.findCount(stockDay.getCode(), "stockStrategy001", startStockDay.getDt(), stockDay.getDt());
			if(n==64){
				break;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(fixedThreadPool!=null){
			fixedThreadPool.shutdown();
		}
		
		
		StrategyResult strategyResult=strategyResultMapper.findMaxProfit(stockDay.getCode(), "stockStrategy001", startStockDay.getDt(), stockDay.getDt());
		
		if(profitLevel==1 && strategyResult.getProfit_rate_year()<0){
			return false;
		}
		
		buyMaN=Integer.parseInt(strategyResult.getParam().split("#")[0]);
		sellMaN=Integer.parseInt(strategyResult.getParam().split("#")[1]);
		
		return true;
	}
	public boolean buy(StockData stockData) {
		if(!pri(stockData)){
			return false;
		}
		List<StockDayFu> stockDays=stockData.getStockDays();
		
		int N=buyMaN;
		if(stockDays.size()<N){
			return false;
		}
		
		List<StockDayFu> last=stockDays.subList(stockDays.size()-N, stockDays.size());
		double ma10=0;
		for(int i=0;i<last.size();i++){
			ma10+=last.get(i).getOpen_price();
		}
		double ma=ma10/last.size();
		if(stockDays.get(stockDays.size()-1).getOpen_price()>ma){
			return true;
		}
		
		return false;
	}

	public boolean sell(StockData stockData) {
		if(!pri(stockData)){
			return false;
		}
		List<StockDayFu> stockDays=stockData.getStockDays();
		
		int N=sellMaN;
		if(stockDays.size()<N){
			return false;
		}
		
		List<StockDayFu> last=stockDays.subList(stockDays.size()-N, stockDays.size());
		double ma10=0;
		for(int i=0;i<last.size();i++){
			ma10+=last.get(i).getOpen_price();
		}
		double ma=ma10/last.size();
		if(stockDays.get(stockDays.size()-1).getOpen_price()<ma){
			return true;
		}
		
		return false;
	}
}
