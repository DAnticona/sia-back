package pe.com.aldesa.aduanero.dto;

public class ErrorResponse {

	private String code;
	private String message;
	private String detailMessage;

	private ErrorResponse(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	private ErrorResponse(String code, String message, String detailMessage) {
		super();
		this.code = code;
		this.message = message;
		this.detailMessage = detailMessage;
	}
	
	public static ErrorResponse of(String code, String message, String detailMessage) {
		return new ErrorResponse(code, message, detailMessage);
	}
	
	public static ErrorResponse of(String code, String message) {
		return new ErrorResponse(code, message);
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

	public String getDetailMessage() {
		return detailMessage;
	}

	public void setDetailMessage(String detailMessage) {
		this.detailMessage = detailMessage;
	}

}
