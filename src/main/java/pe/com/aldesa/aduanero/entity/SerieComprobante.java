package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "serie_comprobante", uniqueConstraints = { @UniqueConstraint(columnNames = "serie") })
public class SerieComprobante implements Serializable {

	private static final long serialVersionUID = -8231161635338902508L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_serie")
	private Integer idSerieComprobante;

	@ManyToOne
	@JoinColumn(name = "id_ticomprobante", nullable = false)
	private TipoComprobante tipoComprobante;

	@Column(name = "serie", nullable = false, length = 4)
	private String serie;

	@Column(name = "fg_acti", nullable = false, length = 1)
	private Character activado;

	@Column(name = "nu_min", nullable = false)
	private Integer numeroMinimo;

	@Column(name = "nu_max", nullable = false)
	private Integer numeroMaximo;

	@Column(name = "descripcion")
	private String descripcion;

	public Integer getIdSerieComprobante() {
		return idSerieComprobante;
	}

	public void setIdSerieComprobante(Integer idSerieComprobante) {
		this.idSerieComprobante = idSerieComprobante;
	}

	public TipoComprobante getTipoComprobante() {
		return tipoComprobante;
	}

	public void setTipoComprobante(TipoComprobante tipoComprobante) {
		this.tipoComprobante = tipoComprobante;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Character getActivado() {
		return activado;
	}

	public void setActivado(Character activado) {
		this.activado = activado;
	}

	public Integer getNumeroMinimo() {
		return numeroMinimo;
	}

	public void setNumeroMinimo(Integer numeroMinimo) {
		this.numeroMinimo = numeroMinimo;
	}

	public Integer getNumeroMaximo() {
		return numeroMaximo;
	}

	public void setNumeroMaximo(Integer numeroMaximo) {
		this.numeroMaximo = numeroMaximo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

}
