package pe.com.aldesa.aduanero.constant;

public enum ApiError {

	SUCCESS("0", "Ok"), 
	NO_APPLICATION_PROCESSED("9000", "El sistema no pudo procesar su solicitid"),
	RESOURCE_NOT_FOUND("9001", "Recurso no encontrado")
	;

	private final String code;
	private final String message;

	private ApiError(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
