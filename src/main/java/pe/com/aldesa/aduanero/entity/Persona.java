package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "persona")
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
public class Persona implements Serializable {

	private static final long serialVersionUID = 4955788495130287463L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_persona", nullable = false)
	private Long idPersona;

	@Column(name = "nu_doc", nullable = false)
	private String numeroDocumento;

	@Column(name = "nombres", nullable = false)
	private String nombres;

	@Column(name = "ap_paterno", nullable = false)
	private String apellidoPaterno;

	@Column(name = "ap_materno")
	private String apellidoMaterno;

	@Column(name = "sexo")
	private Character sexo;

	@Column(name = "fecha_nac")
	private LocalDate fechaNacimiento;

	@Column(name = "email")
	private String email;

	@Column(name = "imagen")
	private String imagen;

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
	
	@ManyToOne
	@JoinColumn(name = "id_tidoc")
	private TipoDocumento tipoDocumento;
	
	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidoPaterno() {
		return apellidoPaterno;
	}

	public void setApellidoPaterno(String apellidoPaterno) {
		this.apellidoPaterno = apellidoPaterno;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public Character getSexo() {
		return sexo;
	}

	public void setSexo(Character sexo) {
		this.sexo = sexo;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
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
		return "Persona [idPersona=" + idPersona + ", numeroDocumento=" + numeroDocumento + ", nombres=" + nombres
				+ ", apellidoPaterno=" + apellidoPaterno + ", apellidoMaterno=" + apellidoMaterno + ", sexo=" + sexo
				+ ", fechaNacimiento=" + fechaNacimiento + ", email=" + email + ", imagen=" + imagen
				+ ", usuarioCreador=" + usuarioCreador + ", usuarioModificador=" + usuarioModificador
				+ ", fechaCreacion=" + fechaCreacion + ", fechaModificacion=" + fechaModificacion + "]";
	}

}
