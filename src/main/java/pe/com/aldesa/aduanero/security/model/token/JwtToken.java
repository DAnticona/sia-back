package pe.com.aldesa.aduanero.security.model.token;

/**
 * Esta interfaz debe ser implementada por aquellas clases que gestionarán la creación de los token de acceso y refreshToken 
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
public interface JwtToken {

	/** 
	 * Retorna el token creado por la aplicación
	 * 
	 * @return
	 */
	String getToken();
	
}
