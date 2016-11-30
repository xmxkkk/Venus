package venus.model.dao;

public class IndustryStock {
	private String type_code;
	private String subtype_code;
	private String code;
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "IndustryStock [type_code=" + type_code + ", subtype_code=" + subtype_code + ", code=" + code + "]";
	}
	
}
