package venus.model.dao;

public class StockCompanyHolderOrg {

	String code;
	String date;
	String name;
	String type;
	double num;
	double total_price;
	double rate;
	double num_change;
	String update_time;
	
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getNum() {
		return num;
	}
	public void setNum(double num) {
		this.num = num;
	}
	public double getTotal_price() {
		return total_price;
	}
	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getNum_change() {
		return num_change;
	}
	public void setNum_change(double num_change) {
		this.num_change = num_change;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "StockCompanyHolderOrg [code=" + code + ", date=" + date + ", name=" + name + ", type=" + type + ", num="
				+ num + ", total_price=" + total_price + ", rate=" + rate + ", num_change=" + num_change
				+ ", update_time=" + update_time + "]";
	}
	
}
