package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "submenu", uniqueConstraints = { @UniqueConstraint(columnNames = "nombre") })
public class SubMenu extends Auditable<String> implements Serializable {

	private static final long serialVersionUID = -424674962558155186L;

	@EmbeddedId
	private SubmenuId submenuId;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@MapsId("idMenu") // mapea el atributo idMenu de embedded id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "id_menu", nullable = false)
	private Menu menu;

	@Column(name = "nu_orden")
	private Integer numeroOrden;

	@Column(name = "ruta")
	private String ruta;

	public SubmenuId getSubmenuId() {
		return submenuId;
	}

	public void setSubmenuId(SubmenuId submenuId) {
		this.submenuId = submenuId;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Integer getNumeroOrden() {
		return numeroOrden;
	}

	public void setNumeroOrden(Integer numeroOrden) {
		this.numeroOrden = numeroOrden;
	}

	public String getRuta() {
		return ruta;
	}

	public void setRuta(String ruta) {
		this.ruta = ruta;
	}

	@Override
	public String toString() {
		return "SubMenu [submenuId=" + submenuId + ", nombre=" + nombre + ", numeroOrden=" + numeroOrden + ", ruta=" + ruta + "]";
	}

}
