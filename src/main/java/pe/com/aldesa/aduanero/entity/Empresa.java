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

@Entity
@Table(name = "empresa")
public class Empresa extends Auditable<String> implements Serializable {

	private static final long serialVersionUID = -8329389334956331955L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_empresa")
	private Long idEmpresa;

	@Column(name = "nu_ruc", nullable = false)
	private String ruc;

	@Column(name = "ra_social", nullable = false)
	private String razonSocial;

	@OneToOne
	@JoinColumn(name = "id_direccion")
	private Direccion direccion;

	@Column(name = "no_comercial")
	private String nombreComercial;

	public Long getIdEmpresa() {
		return idEmpresa;
	}

	public void setIdEmpresa(Long idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getRuc() {
		return ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	public Direccion getDireccion() {
		return direccion;
	}

	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}

	public String getNombreComercial() {
		return nombreComercial;
	}

	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	@Override
	public String toString() {
		return "Empresa [idEmpresa=" + idEmpresa + ", ruc=" + ruc + ", razonSocial=" + razonSocial + ", direccion="
				+ direccion + ", nombreComercial=" + nombreComercial + "]";
	}

}
