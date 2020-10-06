package pe.com.aldesa.aduanero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Subservicio;
import pe.com.aldesa.aduanero.entity.SubservicioId;

@Repository
public interface SubservicioRepository extends JpaRepository<Subservicio, SubservicioId> {

	@Query(value = "select * from subservicio s where s.id_servicio = :idServicio", nativeQuery = true)
	List<Subservicio> searchSubservicioByServicio(Integer idServicio);

	@Query(value = "delete from subservicio s where s.id_servicio = :idServicio and s.id_subservicio = :idSubservicio", nativeQuery = true)
	void deleteSubservicio(Integer idServicio, Integer idSubservicio);

}
