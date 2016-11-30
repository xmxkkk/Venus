package venus.model.dao;

import java.io.Serializable;

public class Industry implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2869248256653590241L;
	private String type;
	private String subtype;
	private String type_code;
	private String subtype_code;
	
	public String getType_code() {
		return type_code;
	}
	public void setType_code(String type_code) {
		this.type_code = type_code;
	}
	public String getSubtype_code() {
		return subtype_code;
	}
	public void setSubtype_code(String subtype_code) {
		this.subtype_code = subtype_code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	@Override
	public String toString() {
		return "Industry [type=" + type + ", subtype=" + subtype + ", type_code=" + type_code + ", subtype_code="
				+ subtype_code + "]";
	}
	
}
