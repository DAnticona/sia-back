package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "usuario", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
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
	public String toString() {
		return "Usuario [idRol=" + idRol + ", username=" + username + ", password=" + password + "]";
	}

}
