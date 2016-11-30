package venus.model.dao;

public class FeatureSimilar {
	String code_a;
	String code_b;
	String feature_type;
	String start_time;
	String end_time;
	String similar_type;
	double similar;
	
	public String getSimilar_type() {
		return similar_type;
	}
	public void setSimilar_type(String similar_type) {
		this.similar_type = similar_type;
	}
	public String getCode_a() {
		return code_a;
	}
	public void setCode_a(String code_a) {
		this.code_a = code_a;
	}
	public String getCode_b() {
		return code_b;
	}
	public void setCode_b(String code_b) {
		this.code_b = code_b;
	}
	public double getSimilar() {
		return similar;
	}
	public void setSimilar(double similar) {
		this.similar = similar;
	}
	public String getFeature_type() {
		return feature_type;
	}
	public void setFeature_type(String feature_type) {
		this.feature_type = feature_type;
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
	
}
