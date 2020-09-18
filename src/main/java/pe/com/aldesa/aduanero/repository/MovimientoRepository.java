package pe.com.aldesa.aduanero.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Movimiento;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = "insert into movimiento (id_tarjeta, id_ticket, fg_movimiento, fe_movimiento) values(:idtarjeta, :idTicket, :fgMovimiento, :fechaMovimiento)", nativeQuery = true)
	void saveMovimiento(Long idtarjeta, Long idTicket, String fgMovimiento, Date fechaMovimiento);

}
