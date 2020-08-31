package pe.com.aldesa.aduanero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.AgenciaAduanas;

@Repository
public interface AgenciaAduanasRepository extends JpaRepository<AgenciaAduanas, Long> {

	@Query("SELECT COUNT(a)>0 FROM AgenciaAduanas a WHERE a.codigoAduana = codigo")
	boolean existsByCodigoAgencia(Integer codigo);

	List<AgenciaAduanas> findByCodigoAduana(Integer codigoaduana);

}
