package venus.strategy.stock.trade.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import venus.model.dao.StockDayFu;
import venus.model.strategy.StockData;
import venus.strategy.stock.trade.StockTradeStrategy;

@Component
public class StockStrategy001 implements StockTradeStrategy{
	private int buyMaN;
	private int sellMaN;
	public void initParam(String param){
		String[] params=param.split("#");
		buyMaN=Integer.parseInt(params[0]);
		sellMaN=Integer.parseInt(params[1]);
	}
	public boolean buy(StockData stockData) {
		int N=buyMaN;
		List<StockDayFu> stockDays=stockData.getStockDays();
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
		int N=sellMaN;
		List<StockDayFu> stockDays=stockData.getStockDays();
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
