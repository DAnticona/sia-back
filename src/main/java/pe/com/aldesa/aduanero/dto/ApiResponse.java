package pe.com.aldesa.aduanero.dto;

public class ApiResponse {

	private String code;
	private String message;
	private Object object;
	private Integer count;

	public ApiResponse(String code, String message) {
		this(code, message, null);
	}

	public ApiResponse(String code, String message, Object object) {
		this(code, message, object, null);
	}

	public ApiResponse(String code, String message, Object object, Integer count) {
		this.code = code;
		this.message = message;
		this.object = object;
		this.count = count;
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

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
