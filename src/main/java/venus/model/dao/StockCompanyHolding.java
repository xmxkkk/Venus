package venus.model.dao;

public class StockCompanyHolding {
	String code;
	String name;
	String relation;
	double rate;
	double money;
	double profit;
	String is_combine;
	String yewu;
	String update_time;
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
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public double getProfit() {
		return profit;
	}
	public void setProfit(double profit) {
		this.profit = profit;
	}
	public String getIs_combine() {
		return is_combine;
	}
	public void setIs_combine(String is_combine) {
		this.is_combine = is_combine;
	}
	public String getYewu() {
		return yewu;
	}
	public void setYewu(String yewu) {
		this.yewu = yewu;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "StockCompanyHolding [code=" + code + ", name=" + name + ", relation=" + relation + ", rate=" + rate
				+ ", money=" + money + ", profit=" + profit + ", is_combine=" + is_combine + ", yewu=" + yewu
				+ ", update_time=" + update_time + "]";
	}
	
}
