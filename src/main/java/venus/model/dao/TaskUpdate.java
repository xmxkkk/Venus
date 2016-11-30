package venus.model.dao;

public class TaskUpdate {
	private String name;
	private String update_time;
	private int interval_time;
	
	public int getInterval_time() {
		return interval_time;
	}
	public void setInterval_time(int interval_time) {
		this.interval_time = interval_time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "TaskUpdate [name=" + name + ", update_time=" + update_time + "]";
	}
	
}
