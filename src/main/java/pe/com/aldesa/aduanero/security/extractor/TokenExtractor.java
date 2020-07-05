package pe.com.aldesa.aduanero.security.extractor;

/**
 * Esta interfaz debe ser implementada por aquellas clases que desean extraer el token desde el encabezado
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
public interface TokenExtractor {
	
	/**
	 * Extrae el token del encabezado y lo retornan como un objeto tipo String
	 * 
	 * @param header
	 * @return Token como objeto tipo String
	 */
	String extract(String header);

}
