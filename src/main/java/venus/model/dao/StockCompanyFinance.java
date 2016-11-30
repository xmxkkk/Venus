package venus.model.dao;

public class StockCompanyFinance {
	private String code;//股票代码
	private String time;//时间
	private String menu;//栏目
	private String type;//统计类型
	private double value;//数值
	private String update_time;//更新时间
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
		return "StockCompanyFinance [code=" + code + ", time=" + time + ", menu=" + menu + ", type=" + type + ", value="
				+ value + ", update_time=" + update_time + "]";
	}
	
}
