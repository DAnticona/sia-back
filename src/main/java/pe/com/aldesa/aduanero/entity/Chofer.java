package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "chofer", uniqueConstraints = { @UniqueConstraint(columnNames = "no_licencia") })
@PrimaryKeyJoinColumn(name = "id_persona")
public class Chofer extends Persona implements Serializable {

	private static final long serialVersionUID = 301744906097714574L;
	
	@Column(name = "no_licencia")
	private String numeroLicencia;

	public String getNumeroLicencia() {
		return numeroLicencia;
	}

	public void setNumeroLicencia(String numeroLicencia) {
		this.numeroLicencia = numeroLicencia;
	}

}
