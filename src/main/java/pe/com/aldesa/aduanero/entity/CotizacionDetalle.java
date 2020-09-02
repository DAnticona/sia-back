package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cotizacion_detalle")
public class CotizacionDetalle extends Auditable<String> implements Serializable {

	private static final long serialVersionUID = -6224104135349703307L;

	@EmbeddedId
	private CotizacionDetalleId cotizacionDetalleId;

	@ManyToOne
	@JoinColumn(name = "id_servicio", nullable = false)
	private Servicio servicio;

	@Column(name = "precio", nullable = false, precision = 12, scale = 2)
	private Double precio;

	public CotizacionDetalleId getDetalleCotizacionId() {
		return cotizacionDetalleId;
	}

	public void setDetalleCotizacionId(CotizacionDetalleId cotizacionDetalleId) {
		this.cotizacionDetalleId = cotizacionDetalleId;
	}

	public Servicio getServicio() {
		return servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public Double getPrecio() {
		return precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}

	@Override
	public String toString() {
		return "DetalleCotizacion [detalleCotizacionId=" + cotizacionDetalleId + ", servicio=" + servicio + ", precio="
				+ precio + "]";
	}

}
