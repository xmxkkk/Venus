package venus.model.dao;

import java.io.Serializable;

public class StageRate implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -638319291146401851L;
	private String stage;
	private int up;
	private int down;
	private int total;
	private int level;

	public int getDown() {
		return down;
	}

	public void setDown(int down) {
		this.down = down;
	}

	public String getStage() {
		return stage;
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public int getUp() {
		return up;
	}

	public void setUp(int up) {
		this.up = up;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "StageRate [stage=" + stage + ", up=" + up + ", total=" + total + ", level=" + level + "]";
	}

}
