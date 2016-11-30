package venus.model.dao;

import java.io.Serializable;

public class Stockinfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8910950168659856950L;
	private String code;
	private String market;
	private String name;
	private double weight;
	private String ipo_start_time;
	private double ipo_price;
	private String create_time;
	private double beta;
	private int ipo_days;
	private int trade_days;
	private String first_trade_day;
	private int stop;
	private int flag;
	
	public int getStop() {
		return stop;
	}
	public void setStop(int stop) {
		this.stop = stop;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getFirst_trade_day() {
		return first_trade_day;
	}
	public void setFirst_trade_day(String first_trade_day) {
		this.first_trade_day = first_trade_day;
	}
	public int getTrade_days() {
		return trade_days;
	}
	public void setTrade_days(int trade_days) {
		this.trade_days = trade_days;
	}
	public int getIpo_days() {
		return ipo_days;
	}
	public void setIpo_days(int ipo_days) {
		this.ipo_days = ipo_days;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public String getIpo_start_time() {
		return ipo_start_time;
	}
	public void setIpo_start_time(String ipo_start_time) {
		this.ipo_start_time = ipo_start_time;
	}
	public double getIpo_price() {
		return ipo_price;
	}
	public void setIpo_price(double ipo_price) {
		this.ipo_price = ipo_price;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public double getBeta() {
		return beta;
	}
	public void setBeta(double beta) {
		this.beta = beta;
	}
	@Override
	public String toString() {
		return "Stockinfo [code=" + code + ", market=" + market + ", name=" + name + ", weight=" + weight
				+ ", ipo_start_time=" + ipo_start_time + ", ipo_price=" + ipo_price + ", create_time=" + create_time
				+ ", beta=" + beta + ", ipo_days=" + ipo_days + ", trade_days=" + trade_days + ", first_trade_day="
				+ first_trade_day + ", stop=" + stop + ", flag=" + flag + "]";
	}
	
}
