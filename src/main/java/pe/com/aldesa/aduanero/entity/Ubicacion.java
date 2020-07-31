package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ubicacion")
public class Ubicacion extends Auditable<String> implements Serializable {

	private static final long serialVersionUID = 4412040510334547731L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_ubicacion")
	private Long idUbicacion;
	
	// TODO
	@Column(name = "id_area")
	private Area area;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "abrev")
	private String abreviatura;
	
	@Column(name = "nu_rack")
	private Integer numeroRack;


}
