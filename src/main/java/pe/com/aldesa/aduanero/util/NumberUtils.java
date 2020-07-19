package pe.com.aldesa.aduanero.util;

/**
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
	
	public static boolean isNotNull(Integer number) {
		return isDistinctNull(number) && isNotZero(number);
	}
	
	private static boolean isDistinctNull(Integer number) {
		return number != 0;
	}
}
