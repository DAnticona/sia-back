package pe.com.aldesa.aduanero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.CotizacionDetalle;
import pe.com.aldesa.aduanero.entity.CotizacionDetalleId;

@Repository
public interface CotizacionDetalleRepository extends JpaRepository<CotizacionDetalle, CotizacionDetalleId> {

}
