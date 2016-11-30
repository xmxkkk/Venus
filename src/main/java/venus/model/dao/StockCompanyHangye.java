package venus.model.dao;

public class StockCompanyHangye {
	String code;
	String level1;
	String level2;
	String level3;
	int level_num;
	String update_time;
	
	public int getLevel_num() {
		return level_num;
	}
	public void setLevel_num(int level_num) {
		this.level_num = level_num;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getLevel1() {
		return level1;
	}
	public void setLevel1(String level1) {
		this.level1 = level1;
	}
	public String getLevel2() {
		return level2;
	}
	public void setLevel2(String level2) {
		this.level2 = level2;
	}
	public String getLevel3() {
		return level3;
	}
	public void setLevel3(String level3) {
		this.level3 = level3;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "StockCompanyHangye [code=" + code + ", level1=" + level1 + ", level2=" + level2 + ", level3=" + level3
				+ ", update_time=" + update_time + "]";
	}
	
}
