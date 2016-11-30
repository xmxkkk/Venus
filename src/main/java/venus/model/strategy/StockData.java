package venus.model.strategy;

import java.util.List;

import venus.model.dao.StockDayFu;
import venus.model.dao.Stockinfo;

public class StockData {
	private List<StockDayFu> stockDays;
	private Stockinfo stockinfo;

	public List<StockDayFu> getStockDays() {
		return stockDays;
	}

	public void setStockDays(List<StockDayFu> stockDays) {
		this.stockDays = stockDays;
	}

	public Stockinfo getStockinfo() {
		return stockinfo;
	}

	public void setStockinfo(Stockinfo stockinfo) {
		this.stockinfo = stockinfo;
	}

	
}
