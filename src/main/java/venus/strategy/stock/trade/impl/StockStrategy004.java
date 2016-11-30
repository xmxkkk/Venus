package venus.strategy.stock.trade.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import venus.model.dao.StockDayFu;
import venus.model.strategy.StockData;
import venus.strategy.stock.trade.StockTradeStrategy;

/**
 * 四个参数(无效)
 * @author Administrator
 *
 */
@Component
public class StockStrategy004 implements StockTradeStrategy{
	private int buyMinMaN;
	private int buyMaxMaN;
	private int sellMinMaN;
	private int sellMaxMaN;
	public void initParam(String param){
		String[] params=param.split("#");
		buyMinMaN=Integer.parseInt(params[0]);
		buyMaxMaN=Integer.parseInt(params[1]);
		sellMinMaN=Integer.parseInt(params[2]);
		sellMaxMaN=Integer.parseInt(params[3]);
	}
	public boolean buy(StockData stockData) {

		List<StockDayFu> stockDays=stockData.getStockDays();
		if(stockDays.size()<buyMinMaN || stockDays.size()<buyMaxMaN){
			return false;
		}
		
		List<StockDayFu> last=stockDays.subList(stockDays.size()-buyMinMaN, stockDays.size());
		double maMin=0;
		for(int i=0;i<last.size();i++){
			maMin+=last.get(i).getOpen_price();
		}
		maMin=maMin/last.size();
		
		last=stockDays.subList(stockDays.size()-buyMaxMaN, stockDays.size());
		double maMax=0;
		for(int i=0;i<last.size();i++){
			maMax+=last.get(i).getOpen_price();
		}
		maMax=maMax/last.size();
		
		if(stockDays.get(stockDays.size()-1).getOpen_price()<maMin || stockDays.get(stockDays.size()-1).getOpen_price()>maMax){
			return true;
		}
		
		
		return false;
	}

	public boolean sell(StockData stockData) {
		List<StockDayFu> stockDays=stockData.getStockDays();
		if(stockDays.size()<sellMinMaN || stockDays.size()<sellMaxMaN){
			return false;
		}
		
		List<StockDayFu> last=stockDays.subList(stockDays.size()-sellMinMaN, stockDays.size());
		double maMin=0;
		for(int i=0;i<last.size();i++){
			maMin+=last.get(i).getOpen_price();
		}
		maMin=maMin/last.size();
		
		last=stockDays.subList(stockDays.size()-sellMaxMaN, stockDays.size());
		double maMax=0;
		for(int i=0;i<last.size();i++){
			maMax+=last.get(i).getOpen_price();
		}
		maMax=maMax/last.size();
		
		if(stockDays.get(stockDays.size()-1).getOpen_price()>maMin || stockDays.get(stockDays.size()-1).getOpen_price()<maMax){
			return true;
		}
		
		return false;
	}

}
