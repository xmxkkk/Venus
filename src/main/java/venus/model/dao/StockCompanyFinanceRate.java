package venus.model.dao;

public class StockCompanyFinanceRate {
	String code;
	String time;
	String menu;
	Double rate;
	String update_time;
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
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "StockCompanyFinanceRate [code=" + code + ", time=" + time + ", menu=" + menu + ", rate=" + rate
				+ ", update_time=" + update_time + "]";
	}
	
}
