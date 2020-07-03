package pe.com.aldesa.aduanero.security.common;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Códigos de error al solicitar acceso a recursos vía Token
 * 
 * @author Juan Pablo Cánepa Alvarez
 *
 */
public enum ErrorCode {

	GLOBAL(2),

    AUTHENTICATION(10), JWT_TOKEN_EXPIRED(11);
    
    private int errorCode;

    private ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @JsonValue
    public int getErrorCode() {
        return errorCode;
    }
}
