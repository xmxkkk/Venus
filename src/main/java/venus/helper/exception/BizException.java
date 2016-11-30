package venus.helper.exception;

public class BizException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8789072494669689792L;
	private String code;
	private String message;
	public BizException(String code,String message){
		this.code=code;
		this.message=message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "BizException [code=" + code + ", message=" + message + "]";
	}
	
}
