package pe.com.aldesa.aduanero.dto;

import java.util.List;

public class RolContext {

	private Integer id;
	private String nombre;
	private List<MenuContext> menus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<MenuContext> getMenus() {
		return menus;
	}

	public void setMenus(List<MenuContext> menus) {
		this.menus = menus;
	}

}
