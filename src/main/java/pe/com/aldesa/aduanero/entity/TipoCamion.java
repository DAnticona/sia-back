package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "tipo_vehiculo", uniqueConstraints = { @UniqueConstraint(columnNames = "abrev") })
public class TipoCamion extends Auditable<String> implements Serializable {

	private static final long serialVersionUID = 187378428064186040L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tivehiculo")
	private Integer idTipoCamion;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "abrev", nullable = false, length = 10)
	private String abreviatura;

	public Integer getIdTipoCamion() {
		return idTipoCamion;
	}

	public void setIdTipoCamion(Integer idTipoCamion) {
		this.idTipoCamion = idTipoCamion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	@Override
	public String toString() {
		return "TipoCamion [idTipoCamion=" + idTipoCamion + ", nombre=" + nombre + ", abreviatura=" + abreviatura + "]";
	}

}
