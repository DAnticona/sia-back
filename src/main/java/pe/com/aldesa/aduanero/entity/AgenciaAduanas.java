package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "agencia_aduanas")
@PrimaryKeyJoinColumn(name = "id_empresa")
public class AgenciaAduanas extends Empresa implements Serializable {

	private static final long serialVersionUID = 6128029804277526206L;

	@Column(name = "cod_aduana", nullable = false)
	private Integer codigoAduana;

	public Integer getCodigoAduana() {
		return codigoAduana;
	}

	public void setCodigoAduana(Integer codigoAduana) {
		this.codigoAduana = codigoAduana;
	}

	@Override
	public String toString() {
		return "AgenciaAduanas [codigoAduana=" + codigoAduana + "]";
	}

}
