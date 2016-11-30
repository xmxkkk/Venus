package venus.model.dao;

public class TaskExecTime {
	String id;
	String name;
	String start_time;
	String end_time;
	int time;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "TaskExecTime [id=" + id + ", name=" + name + ", start_time=" + start_time + ", end_time=" + end_time
				+ ", time=" + time + "]";
	}
	
}
