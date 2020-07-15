package pe.com.aldesa.aduanero.security.auth.web;

/**
 * Esta clase contiene todos los campos de un usuario autorizado y serán parte
 * de la respuesta Http durante el proceso de autenticación
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
public class AuthUserData {

	private Long id;
	private String name;
	private String lastname;
	private String email;
	private String username;
	private String rol;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

}
