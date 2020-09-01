package pe.com.aldesa.aduanero.constant;

public enum ApiError {

	SUCCESS("0", "Ok"),
	NO_APPLICATION_PROCESSED("9000", "El sistema no pudo procesar su solicitud"),
	RESOURCE_NOT_FOUND("9001", "Recurso no encontrado"),
	EMPTY_OR_NULL_PARAMETER("9002", "Uno o más parámetros están vacíos o nulos"),
	ALREADY_EXISTS("9003", "Los datos ya han sido registrados anteriormente"),
	MULTIPLES_SIMILAR_ELEMENTS("9004", "Hay más de 1 coincidencia (múltiples elementos encontrados)"),
	ADUANA_NOT_FOUND("8000", "No se encontró Aduana"),
	AGENCIA_ADUANA_NOT_FOUND("8001", "No se encontró Agencia Aduana"),
	DEPOSITO_TEMPORAL_NOT_FOUND("8002", "No se encontró Depósito temporal"),
	REGIMEN_NOT_FOUND("8003", "No se encontró Régimen"),
	TIPO_BULTO_NOT_FOUND("8004", "No se encontró Tipo Bulto"),
	DAM_NOT_FOUND("8005", "No se encontró DAM");

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
