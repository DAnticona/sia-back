package pe.com.aldesa.aduanero.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "grupo_servicio")
public class GrupoServicio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_grservicio")
	private Integer idGrupoServicio;

	@Column(name = "nombre")
	private String nombre;

	public Integer getIdGrupoServicio() {
		return idGrupoServicio;
	}

	public void setIdGrupoServicio(Integer idGrupoServicio) {
		this.idGrupoServicio = idGrupoServicio;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public String toString() {
		return "GrupoServicio [idGrupoServicio=" + idGrupoServicio + ", nombre=" + nombre + "]";
	}

}
