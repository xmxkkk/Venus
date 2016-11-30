package venus.strategy.stock.trade.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import venus.dao.StockDayFuMapper;
import venus.dao.StrategyResultMapper;
import venus.model.dao.StockDayFu;
import venus.model.strategy.StockData;
import venus.strategy.stock.trade.StockTradeStrategy;
/**
 * 动态长期均线
 * @author Administrator
 *
 */
@Component
public class StockStrategy011 implements StockTradeStrategy{
	@Autowired
	StrategyResultMapper strategyResultMapper;
	@Autowired
	StockDayFuMapper stockDayFuMapper;
	
	private double buyLimit;
	private double sellLimit;
	
	public void initParam(String param){
		buyLimit=new BigDecimal(param.split("#")[0]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		sellLimit=new BigDecimal(param.split("#")[1]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	public boolean buy(StockData stockData) {
		int N1=1;
		List<StockDayFu> stockDays=stockData.getStockDays();
		if(stockDays.size()<N1){
			return false;
		}
		
		StockDayFu stockDay=stockDays.get(stockDays.size()-1);
		
		int total=stockDayFuMapper.findCountTotal(stockDay.getDt());
		int add=stockDayFuMapper.findCountAdd(stockDay.getDt());
		
		if(1.0*add/total>buyLimit){
			return true;
		}
		
		return false;
	}

	public boolean sell(StockData stockData) {
		int N1=1;
		List<StockDayFu> stockDays=stockData.getStockDays();
		if(stockDays.size()<N1){
			return false;
		}
		StockDayFu stockDay=stockDays.get(stockDays.size()-1);
		
		int total=stockDayFuMapper.findCountTotal(stockDay.getDt());
		int reduce=stockDayFuMapper.findCountReduce(stockDay.getDt());
		
		if(1.0*reduce/total>sellLimit){
			return true;
		}
		
		return false;
	}
}
