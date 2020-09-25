package pe.com.aldesa.aduanero.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Cotizacion;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {

	@Query("select c from Cotizacion c where c.fecha between :fechaInicial and :fechaFinal")
	List<Cotizacion> searchByRangoFechas(@Param("fechaInicial") Date fechaInicial, @Param("fechaFinal") Date fechaFinal);

}
