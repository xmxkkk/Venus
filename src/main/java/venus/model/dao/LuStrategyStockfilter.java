package venus.model.dao;

public class LuStrategyStockfilter {
	String name;
	String type;
	String title;
	String attr;
	String use_type;
	int ord;
	
	public int getOrd() {
		return ord;
	}
	public void setOrd(int ord) {
		this.ord = ord;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAttr() {
		return attr;
	}
	public void setAttr(String attr) {
		this.attr = attr;
	}
	public String getUse_type() {
		return use_type;
	}
	public void setUse_type(String use_type) {
		this.use_type = use_type;
	}
	@Override
	public String toString() {
		return "LuStrategyStockfilter [name=" + name + ", type=" + type + ", title=" + title + ", attr=" + attr
				+ ", use_type=" + use_type + ", ord=" + ord + "]";
	}
	
}
