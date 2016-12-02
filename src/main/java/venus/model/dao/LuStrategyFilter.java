package venus.model.dao;

import org.springframework.stereotype.Component;

@Component
public class LuStrategyFilter implements Comparable<LuStrategyFilter>{
	int id;
	String filter;
	String condition;
	String update_time;
	int ord;
	
	public int getOrd() {
		return ord;
	}
	public void setOrd(int ord) {
		this.ord = ord;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public String getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(String update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "LuStrategyFilter [id=" + id + ", filter=" + filter + ", condition=" + condition + ", update_time="
				+ update_time + ", ord=" + ord + "]";
	}
	
	@Override
	public int compareTo(LuStrategyFilter o) {
		return this.ord-o.getOrd();
	}
	
}
