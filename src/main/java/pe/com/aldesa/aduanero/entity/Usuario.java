package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "usuario", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
@PrimaryKeyJoinColumn(name = "id_persona")
public class Usuario extends Persona implements Serializable {

	private static final long serialVersionUID = -5566869717098648176L;

	@ManyToOne
	@JoinColumn(name = "id_rol", nullable = false)
	private Rol rol;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "passwd", nullable = false)
	private String password;

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
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
		return "Usuario [rol=" + rol + ", username=" + username + ", password=" + password + "]";
	}

}
