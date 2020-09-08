package pe.com.aldesa.aduanero.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Cotizacion;
import pe.com.aldesa.aduanero.entity.CotizacionDetalle;

@Repository
public interface CotizacionDetalleRepository extends JpaRepository<CotizacionDetalle, String> {

	@Query("select d from CotizacionDetalle d where d.cotizacion = :cotizacion and d.item = :item")
	Optional<CotizacionDetalle> searchByIdCotizacionAndItem(Cotizacion cotizacion, Integer item);

	@Query("delete from CotizacionDetalle d where d.cotizacion = :cotizacion and d.item = :item")
	void deleteByIdCotizacionAndItem(Cotizacion cotizacion, Integer item);
}
