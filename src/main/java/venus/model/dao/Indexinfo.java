package venus.model.dao;

import java.io.Serializable;

public class Indexinfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7501329091670270100L;
	String code;
	String name;
	int flag;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	@Override
	public String toString() {
		return "Indexinfo [code=" + code + ", name=" + name + ", flag=" + flag + "]";
	}
	
}
