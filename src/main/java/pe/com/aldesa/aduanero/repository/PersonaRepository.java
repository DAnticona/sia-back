package pe.com.aldesa.aduanero.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

	@Query("select p from Persona p where p.nombres like %:nombres% or p.apellidoPaterno like %:nombres% or p.apellidoMaterno like %:nombres%")
	List<Persona> searchByNombres(String nombres);

	@Query("select p from Persona p where p.numeroDocumento = :numeroDocumento")
	Optional<Persona> findByNumeroDocumento(String numeroDocumento);

}
