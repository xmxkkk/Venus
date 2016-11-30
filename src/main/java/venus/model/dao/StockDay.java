package venus.model.dao;

import java.io.Serializable;

public class StockDay extends Price implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5081932496031576464L;

	@Override
	public String toString() {
		return "StockDay [code=" + code + ", dt=" + dt + ", open_price=" + open_price + ", close_price=" + close_price
				+ ", high_price=" + high_price + ", low_price=" + low_price + ", change_rate=" + change_rate
				+ ", change_price=" + change_price + ", trade_quty=" + trade_quty + ", trade_amt=" + trade_amt
				+ ", weight=" + weight + ", week=" + week + "]";
	}
	
}
