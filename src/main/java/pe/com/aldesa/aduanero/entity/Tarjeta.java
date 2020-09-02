package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tarjeta")
public class Tarjeta implements Serializable {

	private static final long serialVersionUID = -4115263633408002233L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tarjeta")
	private Long idTarjeta;

	@ManyToOne
	@JoinColumn(name = "id_cliente", nullable = false)
	private Cliente cliente;

	@OneToOne
	@JoinColumn(name = "id_cotizacion", nullable = false)
	private Cotizacion cotizacion;

	@OneToOne
	@JoinColumn(name = "id_dam", nullable = false)
	private Dam dam;

	@ManyToOne
	@JoinColumn(name = "id_ubicacion", nullable = false)
	private Ubicacion ubicacion;

	@ManyToOne
	@JoinColumn(name = "id_timercancia", nullable = false)
	private TipoMercancia tipoMercancia;

	@Column(name = "fecha", nullable = false)
	private Date fecha;

	@Column(name = "vap_avi", length = 45)
	private String vapAvi;

	@Column(name = "observacion")
	private String observacion;

	@Column(name = "fg_levante", length = 1)
	private String levante;

	@Column(name = "nu_cont20_suelta")
	private Integer nuCont20Suelta;

	@Column(name = "nu_cont40")
	private Integer nuCont40;

	public Long getIdTarjeta() {
		return idTarjeta;
	}

	public void setIdTarjeta(Long idTarjeta) {
		this.idTarjeta = idTarjeta;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cotizacion getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(Cotizacion cotizacion) {
		this.cotizacion = cotizacion;
	}

	public Dam getDam() {
		return dam;
	}

	public void setDam(Dam dam) {
		this.dam = dam;
	}

	public Ubicacion getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Ubicacion ubicacion) {
		this.ubicacion = ubicacion;
	}

	public TipoMercancia getTipoMercancia() {
		return tipoMercancia;
	}

	public void setTipoMercancia(TipoMercancia tipoMercancia) {
		this.tipoMercancia = tipoMercancia;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getVapAvi() {
		return vapAvi;
	}

	public void setVapAvi(String vapAvi) {
		this.vapAvi = vapAvi;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getLevante() {
		return levante;
	}

	public void setLevante(String levante) {
		this.levante = levante;
	}

	public Integer getNuCont20Suelta() {
		return nuCont20Suelta;
	}

	public void setNuCont20Suelta(Integer nuCont20Suelta) {
		this.nuCont20Suelta = nuCont20Suelta;
	}

	public Integer getNuCont40() {
		return nuCont40;
	}

	public void setNuCont40(Integer nuCont40) {
		this.nuCont40 = nuCont40;
	}

	@Override
	public String toString() {
		return "Tarjeta [idTarjeta=" + idTarjeta + ", cliente=" + cliente + ", cotizacion=" + cotizacion + ", dam="
				+ dam + ", ubicacion=" + ubicacion + ", tipoMercancia=" + tipoMercancia + ", fecha=" + fecha
				+ ", vapAvi=" + vapAvi + ", observacion=" + observacion + ", levante=" + levante + ", nuCont20Suelta="
				+ nuCont20Suelta + ", nuCont40=" + nuCont40 + "]";
	}

}
