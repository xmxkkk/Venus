package venus.model.dao;

public class RunStatus {
	String id;
	int status;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "RunStatus [id=" + id + ", status=" + status + "]";
	}
	
}
