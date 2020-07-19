package pe.com.aldesa.aduanero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
	
	@Query("select max(idPersona) from Persona")
	Long findLastIdPersona();

}
