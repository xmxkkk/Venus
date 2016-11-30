package venus.strategy.stock.choose;

import java.util.List;

import venus.model.dao.Stockinfo;

public interface StockChooseStrategy {
	public List<Stockinfo> choose();
	public void initParam(String param);
}
