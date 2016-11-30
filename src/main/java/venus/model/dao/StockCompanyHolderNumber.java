package venus.model.dao;

public class StockCompanyHolderNumber {
	private String code;
	private String time;
	private String menu;
	private double value;
	private String update_time;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	public double getValue() {
		return value;
	}
	public void setValue(double value) {
		this.value = value;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "StockCompanyHolderNumber [code=" + code + ", time=" + time + ", menu=" + menu + ", value=" + value
				+ ", update_time=" + update_time + "]";
	}
	
}
