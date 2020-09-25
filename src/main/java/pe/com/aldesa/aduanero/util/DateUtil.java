package pe.com.aldesa.aduanero.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

	private DateUtil() {
		throw new UnsupportedOperationException();
	}

	public static Date of(String contentDate) {
		return Date.from(LocalDate.parse(contentDate).atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

}
