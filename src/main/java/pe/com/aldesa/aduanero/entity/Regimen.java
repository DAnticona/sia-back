package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "regimen")
public class Regimen extends Auditable<String> implements Serializable {

	private static final long serialVersionUID = -4099110453184306531L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_regimen")
	private Integer idRegimen;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "cod_aduana", nullable = false, length = 2)
	private Integer codigoAduana;

	public Integer getIdRegimen() {
		return idRegimen;
	}

	public void setIdRegimen(Integer idRegimen) {
		this.idRegimen = idRegimen;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getCodigoAduana() {
		return codigoAduana;
	}

	public void setCodigoAduana(Integer codigoAduana) {
		this.codigoAduana = codigoAduana;
	}

	@Override
	public String toString() {
		return "Regimen [idRegimen=" + idRegimen + ", nombre=" + nombre + ", codigoAduana=" + codigoAduana + "]";
	}

}
