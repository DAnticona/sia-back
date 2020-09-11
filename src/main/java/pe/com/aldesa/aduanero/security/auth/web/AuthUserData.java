package pe.com.aldesa.aduanero.security.auth.web;

import java.util.List;

import pe.com.aldesa.aduanero.dto.MenuContext;

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
	private String imagen;
	private String activo;
	private List<MenuContext> menu;

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

	public List<MenuContext> getMenu() {
		return menu;
	}

	public void setMenus(List<MenuContext> menu) {
		this.menu = menu;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

}
