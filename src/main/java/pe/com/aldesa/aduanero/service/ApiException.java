package pe.com.aldesa.aduanero.service;

public class ApiException extends Exception {

	private static final long serialVersionUID = -7821958228183810886L;

	private final String code;
	private final String message;
	private final String detailMessage;

	public ApiException(String code, String message) {
		this.code = code;
		this.message = message;
		this.detailMessage = null;
	}

	public ApiException(String code, String message, String detailMessage) {
		this.code = code;
		this.message = message;
		this.detailMessage = detailMessage;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getDetailMessage() {
		return detailMessage;
	}

}
