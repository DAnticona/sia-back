package pe.com.aldesa.aduanero.dto;

import java.util.List;

import pe.com.aldesa.aduanero.entity.Cotizacion;
import pe.com.aldesa.aduanero.entity.CotizacionDetalle;

public class CotizacionContext {

	private Cotizacion cotizacion;

	private List<CotizacionDetalle> lineas;

	public CotizacionContext(Cotizacion cotizacion, List<CotizacionDetalle> lineas) {
		super();
		this.cotizacion = cotizacion;
		this.lineas = lineas;
	}

	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public List<CotizacionDetalle> getLineas() {
		return lineas;
	}

}
