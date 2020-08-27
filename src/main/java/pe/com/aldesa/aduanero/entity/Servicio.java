package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "servicio", uniqueConstraints = {@UniqueConstraint(columnNames = "nombre")})
public class Servicio extends Auditable<String> implements Serializable {

	private static final long serialVersionUID = 7712878983649738669L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_servicio")
	private Integer idServicio;

	@OneToOne
	@JoinColumn(name = "id_grservicio", nullable = false)
	private GrupoServicio grupoServicio;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "fg_pct", length = 1, nullable = false)
	private String pct;

	@Column(name = "precio_mn", precision = 12, scale = 2)
	private Double precioMonedaNacional;

	@Column(name = "precio_me", precision = 12, scale = 2)
	private Double precioMonedaExtrangera;

	public Integer getIdServicio() {
		return idServicio;
	}

	public void setIdServicio(Integer idServicio) {
		this.idServicio = idServicio;
	}

	public GrupoServicio getGrupoServicio() {
		return grupoServicio;
	}

	public void setGrupoServicio(GrupoServicio grupoServicio) {
		this.grupoServicio = grupoServicio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPct() {
		return pct;
	}

	public void setPct(String pct) {
		this.pct = pct;
	}

	public Double getPrecioMonedaNacional() {
		return precioMonedaNacional;
	}

	public void setPrecioMonedaNacional(Double precioMonedaNacional) {
		this.precioMonedaNacional = precioMonedaNacional;
	}

	public Double getPrecioMonedaExtrangera() {
		return precioMonedaExtrangera;
	}

	public void setPrecioMonedaExtrangera(Double precioMonedaExtrangera) {
		this.precioMonedaExtrangera = precioMonedaExtrangera;
	}

	@Override
	public String toString() {
		return "Servicio [idServicio=" + idServicio + ", grupoServicio=" + grupoServicio + ", nombre=" + nombre
				+ ", pct=" + pct + ", precioMonedaNacional=" + precioMonedaNacional + ", precioMonedaExtrangera="
				+ precioMonedaExtrangera + "]";
	}

}
