package venus.model.dao;

public class StockCompanyHolderChange {
	String code;
	String time;
	Double aguzongguben;
	Double liutongagu;
	Double xianshouagu;
	String biandongyuanyin;
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
	public Double getAguzongguben() {
		return aguzongguben;
	}
	public void setAguzongguben(Double aguzongguben) {
		this.aguzongguben = aguzongguben;
	}
	public Double getLiutongagu() {
		return liutongagu;
	}
	public void setLiutongagu(Double liutongagu) {
		this.liutongagu = liutongagu;
	}
	public Double getXianshouagu() {
		return xianshouagu;
	}
	public void setXianshouagu(Double xianshouagu) {
		this.xianshouagu = xianshouagu;
	}
	public String getBiandongyuanyin() {
		return biandongyuanyin;
	}
	public void setBiandongyuanyin(String biandongyuanyin) {
		this.biandongyuanyin = biandongyuanyin;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "StockCompanyHolderStruct [code=" + code + ", time=" + time 
				+ ", aguzongguben=" + aguzongguben + ", liutongagu=" + liutongagu + ", xianshouagu=" + xianshouagu
				+ ", biandongyuanyin=" + biandongyuanyin + ", update_time=" + update_time + "]";
	}
	
}
