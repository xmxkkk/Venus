package venus.model.dao;

import java.io.Serializable;

public class Price implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6682400756837121664L;
	protected String code;
	protected String dt;
	protected Double open_price;
	protected Double close_price;
	protected Double high_price;
	protected Double low_price;
	protected Double change_rate;
	protected Double change_price;
	protected Double trade_quty;
	protected Double trade_amt;
	protected Double weight;
	protected String week;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

	public Double getOpen_price() {
		return open_price;
	}

	public void setOpen_price(Double open_price) {
		this.open_price = open_price;
	}

	public Double getClose_price() {
		return close_price;
	}

	public void setClose_price(Double close_price) {
		this.close_price = close_price;
	}

	public Double getHigh_price() {
		return high_price;
	}

	public void setHigh_price(Double high_price) {
		this.high_price = high_price;
	}

	public Double getLow_price() {
		return low_price;
	}

	public void setLow_price(Double low_price) {
		this.low_price = low_price;
	}

	public Double getChange_rate() {
		return change_rate;
	}

	public void setChange_rate(Double change_rate) {
		this.change_rate = change_rate;
	}

	public Double getChange_price() {
		return change_price;
	}

	public void setChange_price(Double change_price) {
		this.change_price = change_price;
	}

	public Double getTrade_quty() {
		return trade_quty;
	}

	public void setTrade_quty(Double trade_quty) {
		this.trade_quty = trade_quty;
	}

	public Double getTrade_amt() {
		return trade_amt;
	}

	public void setTrade_amt(Double trade_amt) {
		this.trade_amt = trade_amt;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	
}
