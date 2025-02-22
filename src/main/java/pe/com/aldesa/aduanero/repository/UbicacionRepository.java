package pe.com.aldesa.aduanero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Area;
import pe.com.aldesa.aduanero.entity.Ubicacion;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {

	List<Ubicacion> findByArea(Area area);

}
