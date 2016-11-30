package venus.model.strategy;

public class StrategyProcess {
	private int id;
	private String type;
	private String dt;
	private double price;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDt() {
		return dt;
	}
	public void setDt(String dt) {
		this.dt = dt;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	@Override
	public String toString() {
		return "StrategyProcess [id=" + id + ", type=" + type + ", dt=" + dt + ", price=" + price + "]";
	}

	
	
}
