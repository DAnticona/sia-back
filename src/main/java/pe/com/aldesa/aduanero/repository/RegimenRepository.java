package pe.com.aldesa.aduanero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Regimen;

@Repository
public interface RegimenRepository extends JpaRepository<Regimen, Integer> {

	List<Regimen> findByCodigoAduana(Integer codigoaduana);

}
