package venus.model.dao;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class IndexDay extends Price implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3164874419702276007L;

	@Override
	public String toString() {
		return "MarketDay [code=" + code + ", dt=" + dt + ", open_price=" + open_price + ", close_price="
				+ close_price + ", high_price=" + high_price + ", low_price=" + low_price + ", change_rate="
				+ change_rate + ", change_price=" + change_price + ", trade_quty=" + trade_quty + ", trade_amt="
				+ trade_amt + ", weight=" + weight + ", week=" + week + "]";
	}

}
