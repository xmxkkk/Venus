package venus.model.dao;

public class LuStrategyStock {
	int id;
	String code;
	String market;
	String name;
	String addtime;
	String quittime;
	int status;
	int score;
	Double change_rate;
	Double curr_price;
	Double zongshizhi;
	Double shiyinglvttm;
	String update_time;
	String join_date;
	Double join_price;
	Double join_price_fu;
	Double curr_price_fu;
	Double total_change_rate;
	Double weight;
	Double shijinglv;
	Double roe;
	
	public Double getShijinglv() {
		return shijinglv;
	}
	public void setShijinglv(Double shijinglv) {
		this.shijinglv = shijinglv;
	}
	public Double getRoe() {
		return roe;
	}
	public void setRoe(Double roe) {
		this.roe = roe;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public Double getTotal_change_rate() {
		return total_change_rate;
	}
	public void setTotal_change_rate(Double total_change_rate) {
		this.total_change_rate = total_change_rate;
	}
	public String getJoin_date() {
		return join_date;
	}
	public void setJoin_date(String join_date) {
		this.join_date = join_date;
	}
	public Double getJoin_price() {
		return join_price;
	}
	public void setJoin_price(Double join_price) {
		this.join_price = join_price;
	}
	public Double getJoin_price_fu() {
		return join_price_fu;
	}
	public void setJoin_price_fu(Double join_price_fu) {
		this.join_price_fu = join_price_fu;
	}
	public Double getCurr_price_fu() {
		return curr_price_fu;
	}
	public void setCurr_price_fu(Double curr_price_fu) {
		this.curr_price_fu = curr_price_fu;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMarket() {
		return market;
	}
	public void setMarket(String market) {
		this.market = market;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public Double getChange_rate() {
		return change_rate;
	}
	public void setChange_rate(Double change_rate) {
		this.change_rate = change_rate;
	}
	public Double getCurr_price() {
		return curr_price;
	}
	public void setCurr_price(Double curr_price) {
		this.curr_price = curr_price;
	}
	public Double getZongshizhi() {
		return zongshizhi;
	}
	public void setZongshizhi(Double zongshizhi) {
		this.zongshizhi = zongshizhi;
	}
	public Double getShiyinglvttm() {
		return shiyinglvttm;
	}
	public void setShiyinglvttm(Double shiyinglvttm) {
		this.shiyinglvttm = shiyinglvttm;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public String getQuittime() {
		return quittime;
	}
	public void setQuittime(String quittime) {
		this.quittime = quittime;
	}
	@Override
	public String toString() {
		return "LuStrategyStock [id=" + id + ", code=" + code + ", market=" + market + ", name=" + name + ", addtime="
				+ addtime + ", quittime=" + quittime + ", status=" + status + ", score=" + score + ", change_rate="
				+ change_rate + ", curr_price=" + curr_price + ", zongshizhi=" + zongshizhi + ", shiyinglvttm="
				+ shiyinglvttm + ", update_time=" + update_time + ", join_date=" + join_date + ", join_price="
				+ join_price + ", join_price_fu=" + join_price_fu + ", curr_price_fu=" + curr_price_fu
				+ ", total_change_rate=" + total_change_rate + ", weight=" + weight + ", shijinglv=" + shijinglv
				+ ", roe=" + roe + "]";
	}
	
}
