package venus.model.dao;

public class LuStrategyStockQuit {
	int id;
	String code;
	String calc_date;
	String join_date;
	String quit_date;
	Double join_price;
	Double quit_price;
	Double join_price_fu;
	Double quit_price_fu;
	String update_time;
	Double change_rate;
	
	public Double getChange_rate() {
		return change_rate;
	}
	public void setChange_rate(Double change_rate) {
		this.change_rate = change_rate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCalc_date() {
		return calc_date;
	}
	public void setCalc_date(String calc_date) {
		this.calc_date = calc_date;
	}
	public String getJoin_date() {
		return join_date;
	}
	public void setJoin_date(String join_date) {
		this.join_date = join_date;
	}
	public String getQuit_date() {
		return quit_date;
	}
	public void setQuit_date(String quit_date) {
		this.quit_date = quit_date;
	}
	public Double getJoin_price() {
		return join_price;
	}
	public void setJoin_price(Double join_price) {
		this.join_price = join_price;
	}
	public Double getQuit_price() {
		return quit_price;
	}
	public void setQuit_price(Double quit_price) {
		this.quit_price = quit_price;
	}
	public Double getJoin_price_fu() {
		return join_price_fu;
	}
	public void setJoin_price_fu(Double join_price_fu) {
		this.join_price_fu = join_price_fu;
	}
	public Double getQuit_price_fu() {
		return quit_price_fu;
	}
	public void setQuit_price_fu(Double quit_price_fu) {
		this.quit_price_fu = quit_price_fu;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "LuStrategyStockQuit [id=" + id + ", code=" + code + ", calc_date=" + calc_date + ", join_date="
				+ join_date + ", quit_date=" + quit_date + ", join_price=" + join_price + ", quit_price=" + quit_price
				+ ", join_price_fu=" + join_price_fu + ", quit_price_fu=" + quit_price_fu + ", update_time="
				+ update_time + ", change_rate=" + change_rate + "]";
	}
	
}
