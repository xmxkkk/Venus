package venus.model.dao;

public class StockCompanyHolderCansell {
	String code;
	String date;
	double num;
	double rate;
	String type;
	double price;
	double total_price;
	String update_time;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public double getNum() {
		return num;
	}
	public void setNum(double num) {
		this.num = num;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getTotal_price() {
		return total_price;
	}
	public void setTotal_price(double total_price) {
		this.total_price = total_price;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "StockCompanyHolderCansell [code=" + code + ", date=" + date + ", num=" + num + ", rate=" + rate
				+ ", type=" + type + ", price=" + price + ", total_price=" + total_price + ", update_time="
				+ update_time + "]";
	}
	
}
