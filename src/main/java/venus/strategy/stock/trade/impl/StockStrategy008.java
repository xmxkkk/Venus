package venus.strategy.stock.trade.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import venus.model.dao.StockDayFu;
import venus.model.strategy.StockData;
import venus.strategy.stock.trade.StockTradeStrategy;

@Component
public class StockStrategy008 implements StockTradeStrategy{
	private int buyMaN;
	private int sellMaN;
	private int buyRejectMaN;
	private int sellRejectMaN;
	
	public void initParam(String param){
		String[] params=param.split("#");
		buyMaN=Integer.parseInt(params[0]);
		sellMaN=Integer.parseInt(params[1]);
		buyRejectMaN=Integer.parseInt(params[2]);
		sellRejectMaN=Integer.parseInt(params[3]);
	}
	public boolean buy(StockData stockData) {
		List<StockDayFu> stockDays=stockData.getStockDays();
		if(stockDays.size()<buyMaN||stockDays.size()<buyRejectMaN){
			return false;
		}
		
		List<StockDayFu> last=stockDays.subList(stockDays.size()-buyRejectMaN, stockDays.size());
		double ma10=0;
		for(int i=0;i<last.size();i++){
			ma10+=last.get(i).getOpen_price();
		}
		double ma=ma10/last.size();
		if(stockDays.get(stockDays.size()-1).getOpen_price()<ma){
			return false;
		}
		
		last=stockDays.subList(stockDays.size()-buyMaN, stockDays.size());
		ma10=0;
		for(int i=0;i<last.size();i++){
			ma10+=last.get(i).getOpen_price();
		}
		ma=ma10/last.size();
		if(stockDays.get(stockDays.size()-1).getOpen_price()>ma){
			return true;
		}
		
		return false;
	}

	public boolean sell(StockData stockData) {
		List<StockDayFu> stockDays=stockData.getStockDays();
		if(stockDays.size()<sellMaN||stockDays.size()<sellRejectMaN){
			return false;
		}
		
		List<StockDayFu> last=stockDays.subList(stockDays.size()-sellRejectMaN, stockDays.size());
		double ma10=0;
		for(int i=0;i<last.size();i++){
			ma10+=last.get(i).getOpen_price();
		}
		double ma=ma10/last.size();
		if(stockDays.get(stockDays.size()-1).getOpen_price()>ma){
			return false;
		}
		
		last=stockDays.subList(stockDays.size()-sellMaN, stockDays.size());
		ma10=0;
		for(int i=0;i<last.size();i++){
			ma10+=last.get(i).getOpen_price();
		}
		ma=ma10/last.size();
		if(stockDays.get(stockDays.size()-1).getOpen_price()<ma){
			return true;
		}
		
		return false;
	}

}
