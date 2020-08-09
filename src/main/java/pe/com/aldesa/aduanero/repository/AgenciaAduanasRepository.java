package pe.com.aldesa.aduanero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.AgenciaAduanas;

@Repository
public interface AgenciaAduanasRepository extends JpaRepository<AgenciaAduanas, Long> {

	@Query("SELECT COUNT(a)>0 FROM AgenciaAduanas a WHERE a.codigoAduana = codigo")
	boolean existsByCodigoAgencia(Integer codigo);

	//	@Query("SELECT a FROM AgenciaAduanas a WHERE codigoAduana = codigo")
	//	AgenciaAduanas findByCodigoAgencia(Integer codigo);
}
