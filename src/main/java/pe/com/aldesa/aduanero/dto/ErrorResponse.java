package pe.com.aldesa.aduanero.dto;

public class ErrorResponse {

	private String code;
	private String message;
	private String detailMessage;

	public ErrorResponse(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public ErrorResponse(String code, String message, String detailMessage) {
		super();
		this.code = code;
		this.message = message;
		this.detailMessage = detailMessage;
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
