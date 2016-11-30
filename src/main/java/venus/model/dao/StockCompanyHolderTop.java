package venus.model.dao;

public class StockCompanyHolderTop {
	String code;
	String type;
	String name;
	String time;
	double stock_number;
	double stock_number_change;
	double stock_rate;
	double stock_rate_change;
	String category;
	String update_time;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public double getStock_number() {
		return stock_number;
	}
	public void setStock_number(double stock_number) {
		this.stock_number = stock_number;
	}
	public double getStock_number_change() {
		return stock_number_change;
	}
	public void setStock_number_change(double stock_number_change) {
		this.stock_number_change = stock_number_change;
	}
	public double getStock_rate() {
		return stock_rate;
	}
	public void setStock_rate(double stock_rate) {
		this.stock_rate = stock_rate;
	}
	public double getStock_rate_change() {
		return stock_rate_change;
	}
	public void setStock_rate_change(double stock_rate_change) {
		this.stock_rate_change = stock_rate_change;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "StockCompanyHolderOrg [code=" + code + ", type=" + type + ", name=" + name + ", time=" + time
				+ ", stock_number=" + stock_number + ", stock_number_change=" + stock_number_change + ", stock_rate="
				+ stock_rate + ", stock_rate_change=" + stock_rate_change + ", category=" + category + ", update_time="
				+ update_time + "]";
	}
	
}
