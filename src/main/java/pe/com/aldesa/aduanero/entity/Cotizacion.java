package pe.com.aldesa.aduanero.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "cotizacion")
public class Cotizacion extends Auditable<String> implements Serializable {

	private static final long serialVersionUID = 6937274288794045024L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_cotizacion")
	private Long idCotizacion;

	@OneToOne
	@JoinColumn(name = "id_vendedor", nullable = false)
	private Vendedor vendedor;

	@OneToOne
	@JoinColumn(name = "id_cliente", nullable = false)
	private Cliente cliente;

	@OneToOne
	@JoinColumn(name = "id_agencia")
	private AgenciaAduanas agenciaAduana;

	@OneToOne
	@JoinColumn(name = "id_moneda", nullable = false)
	private Moneda moneda;

	@Column(name = "fg_etapa", nullable = false, length = 1)
	private String etapa;

	@Column(name = "referencia")
	private String referencia;

	@Column(name = "observaciones")
	private String observaciones;

	public Long getIdCotizacion() {
		return idCotizacion;
	}

	public void setIdCotizacion(Long idCotizacion) {
		this.idCotizacion = idCotizacion;
	}

	public Vendedor getVendedor() {
		return vendedor;
	}

	public void setVendedor(Vendedor vendedor) {
		this.vendedor = vendedor;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public AgenciaAduanas getAgenciaAduana() {
		return agenciaAduana;
	}

	public void setAgenciaAduana(AgenciaAduanas agenciaAduana) {
		this.agenciaAduana = agenciaAduana;
	}

	public Moneda getMoneda() {
		return moneda;
	}

	public void setMoneda(Moneda moneda) {
		this.moneda = moneda;
	}

	public String getEtapa() {
		return etapa;
	}

	public void setEtapa(String etapa) {
		this.etapa = etapa;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	@Override
	public String toString() {
		return "Cotizacion [idCotizacion=" + idCotizacion + ", vendedor=" + vendedor + ", cliente=" + cliente
				+ ", agenciaAduana=" + agenciaAduana + ", moneda=" + moneda + ", etapa=" + etapa + ", referencia="
				+ referencia + ", observaciones=" + observaciones + "]";
	}

}
