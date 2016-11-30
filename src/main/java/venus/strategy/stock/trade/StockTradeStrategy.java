package venus.strategy.stock.trade;

import venus.model.strategy.StockData;

public interface StockTradeStrategy {
	public boolean buy(StockData stockData);
	public boolean sell(StockData stockData);
	public void initParam(String param);
}
