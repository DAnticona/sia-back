package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "tipo_documento")
@EntityListeners(AuditingEntityListener.class)
public class TipoDocumento implements Serializable {

	private static final long serialVersionUID = 2150530504700988652L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_tidoc", nullable = false)
	private Integer idTipoDocumento;

	@Column(name = "nombre", nullable = false)
	private String nombre;

	@Column(name = "abrev", nullable = false)
	private String abreviatura;

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

	@OneToMany(mappedBy = "tipoDocumento")
	private List<Persona> personas;

	public Integer getIdTipoDocumento() {
		return idTipoDocumento;
	}

	public void setIdTipoDocumento(Integer idTipoDocumento) {
		this.idTipoDocumento = idTipoDocumento;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
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

	@Override
	public String toString() {
		return "TipoDocumento [idTipoDocumento=" + idTipoDocumento + ", nombre=" + nombre + ", abreviatura="
				+ abreviatura + ", usuarioCreador=" + usuarioCreador + ", usuarioModificador=" + usuarioModificador
				+ ", fechaCreacion=" + fechaCreacion + ", fechaModificacion=" + fechaModificacion + "]";
	}

}
