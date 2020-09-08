package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cotizacion_detalle")
public class CotizacionDetalle extends Auditable<String> implements Serializable {

	private static final long serialVersionUID = -6224104135349703307L;

	@Id
	@Column(name = "id_detalle", nullable = false)
	private String idDetalle;

	@Column(name = "item", nullable = false)
	private Integer item;

	@ManyToOne
	@JoinColumn(name = "id_servicio", nullable = false)
	private Servicio servicio;

	@Column(name = "precio", nullable = false, precision = 12, scale = 2)
	private Double precio;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_cotizacion", nullable = false)
	private Cotizacion cotizacion;

	public String getIdDetalle() {
		return idDetalle;
	}

	public void setIdDetalle(String idDetalle) {
		this.idDetalle = idDetalle;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(Integer item) {
		this.item = item;
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

	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
	}

	@Override
	public String toString() {
		return "CotizacionDetalle [idDetalle=" + idDetalle + ", item=" + item
				+ ", servicio=" + servicio + ", precio=" + precio + ", cotizacion=" + cotizacion + "]";
	}

}
