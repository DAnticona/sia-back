package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "movimiento")
public class Movimiento extends Auditable<String> implements Serializable {

	private static final long serialVersionUID = -6869079058371769385L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_movimiento")
	private Long idMovimiento;

	@ManyToOne
	@JoinColumn(name = "id_tarjeta", nullable = false)
	private Tarjeta tarjeta;

	@ManyToOne
	@JoinColumn(name = "id_ticket", nullable = false)
	private Ticket ticket;

	@Column(name = "fg_movimiento", nullable = false)
	private String fgMovimiento;

	@Column(name = "fe_movimiento", nullable = false)
	private Date fechaMovimiento;

	public Long getIdMovimiento() {
		return idMovimiento;
	}

	public void setIdMovimiento(Long idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public Tarjeta getTarjeta() {
		return tarjeta;
	}

	public void setTarjeta(Tarjeta tarjeta) {
		this.tarjeta = tarjeta;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	public String getFgMovimiento() {
		return fgMovimiento;
	}

	public void setFgMovimiento(String fgMovimiento) {
		this.fgMovimiento = fgMovimiento;
	}

	public Date getFechaMovimiento() {
		return fechaMovimiento;
	}

	public void setFechaMovimiento(Date fechaMovimiento) {
		this.fechaMovimiento = fechaMovimiento;
	}

	@Override
	public String toString() {
		return "Movimiento [idMovimiento=" + idMovimiento + ", tarjeta=" + tarjeta + ", ticket=" + ticket
				+ ", fgMovimiento=" + fgMovimiento + ", fechaMovimiento=" + fechaMovimiento + "]";
	}

}
