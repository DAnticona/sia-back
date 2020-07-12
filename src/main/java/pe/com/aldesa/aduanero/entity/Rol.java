package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "rol")
public class Rol implements Serializable {

	private static final long serialVersionUID = -8967563214360660914L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_rol")
	private Integer idRol;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@CreatedBy
	@Column(name = "us_crea")
	private String usuarioCreador;

	@LastModifiedBy
	@Column(name = "us_modi")
	private String usuarioModificador;

	@CreatedDate
	@Column(name = "fe_crea")
	private LocalDateTime fechaCreacion;

	@LastModifiedDate
	@Column(name = "fe_modi")
	private LocalDateTime fechaModificacion;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "idRol")
	private List<Usuario> usuarios = new ArrayList<>();

	public Integer getIdRol() {
		return idRol;
	}

	public void setIdRol(Integer idRol) {
		this.idRol = idRol;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getUsuarioCreador() {
		return usuarioCreador;
	}

	public void setUsuarioCreador(String usuarioCreador) {
		this.usuarioCreador = usuarioCreador;
	}

	public String getUsuarioModificador() {
		return usuarioModificador;
	}

	public void setUsuarioModificador(String usuarioModificador) {
		this.usuarioModificador = usuarioModificador;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}

	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

}
