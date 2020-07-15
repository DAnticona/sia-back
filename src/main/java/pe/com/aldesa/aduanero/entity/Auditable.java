package pe.com.aldesa.aduanero.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> {

	@CreatedBy
	@Column(name = "us_crea", nullable = false, updatable = false)
	protected T usuarioCreador;

	@LastModifiedBy
	@Column(name = "us_modi", nullable = false)
	protected T usuarioModificador;

	@CreatedDate
	@Column(name = "fe_crea", nullable = false, updatable = false)
	protected LocalDateTime fechaCreacion;

	@LastModifiedDate
	@Column(name = "fe_modi", nullable = false)
	protected LocalDateTime fechaModificacion;

	public T getUsuarioCreador() {
		return usuarioCreador;
	}

	public void setUsuarioCreador(T usuarioCreador) {
		this.usuarioCreador = usuarioCreador;
	}

	public T getUsuarioModificador() {
		return usuarioModificador;
	}

	public void setUsuarioModificador(T usuarioModificador) {
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

}
