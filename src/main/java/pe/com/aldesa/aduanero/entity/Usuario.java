package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "usuario", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
//@JsonIgnoreProperties(ignoreUnknown = true)
@EntityListeners(AuditingEntityListener.class)
public class Usuario extends Persona implements Serializable {

	private static final long serialVersionUID = -5566869717098648176L;

	@ManyToOne
	@JoinColumn(name = "id_rol", nullable = false)
	private Rol idRol;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "passwd", nullable = false)
	private String password;

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

	public Rol getIdRol() {
		return idRol;
	}

	public void setIdRol(Rol idRol) {
		this.idRol = idRol;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsuarioCreador() {
		return usuarioCreador;
	}

	@Override
	public void setUsuarioCreador(String usuarioCreador) {
		this.usuarioCreador = usuarioCreador;
	}

	@Override
	public String getUsuarioModificador() {
		return usuarioModificador;
	}

	@Override
	public void setUsuarioModificador(String usuarioModificador) {
		this.usuarioModificador = usuarioModificador;
	}

	@Override
	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	@Override
	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Override
	public LocalDateTime getFechaModificacion() {
		return fechaModificacion;
	}

	@Override
	public void setFechaModificacion(LocalDateTime fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	@Override
	public String toString() {
		return "Usuario [idRol=" + idRol + ", username=" + username + ", password=" + password + ", usuarioCreador="
				+ usuarioCreador + ", usuarioModificador=" + usuarioModificador + ", fechaCreacion=" + fechaCreacion
				+ ", fechaModificacion=" + fechaModificacion + "]";
	}

}
