package pe.com.aldesa.aduanero.util;

/**
 * Operaciones en {@link Number} que son nulas
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
public class NumberUtils {
	
	private NumberUtils() {
        super();
    }
	
	public static boolean isNotZero(Integer number) {
		return number != 0;
	}
	
	public static boolean isNotZero(Long number) {
		return number != 0;
	}
	
	public static boolean isZero(Long number) {
		return number == 0;
	}
	
	public static boolean isZero(Integer number) {
		return number == 0;
	}
	
	/**
	 * Verifica si la instancia de tipo {@link Integer} es <tt>null</tt> y/o de cero
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isNull(Integer number) {
		return isEqualsNull(number) && isZero(number);
	}
	
	/**
	 * Verifica si la instancia de tipo {@link Long} es <tt>null</tt> y/o de cero
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isNull(Long number) {
		return isEqualsNull(number) && isZero(number);
	}
	
	/**
	 * Verifica si la instancia de tipo {@link Integer} es diferente de <tt>null</tt> y diferente de cero
	 * <pre>
	 * NumberUtils.isNotNull(null)		= false
	 * NumberUtils.isNotNull(0)		= false
	 * </pre>
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isNotNull(Integer number) {
		return isDistinctNull(number) && isNotZero(number);
	}
	
	/**
	 * Verifica si la instancia de tipo {@link Long} es diferente de <tt>null</tt> y diferente de cero
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isNotNull(Long number) {
		return isDistinctNull(number) && isNotZero(number);
	}
	
	private static boolean isDistinctNull(Integer number) {
		return number != null;
	}
	
	private static boolean isEqualsNull(Integer number) {
		return number == null;
	}
	
	private static boolean isEqualsNull(Long number) {
		return number == null;
	}
	
	private static boolean isDistinctNull(Long number) {
		return number != null;
	}
}
