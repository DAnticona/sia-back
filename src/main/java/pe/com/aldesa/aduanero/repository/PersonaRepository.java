package pe.com.aldesa.aduanero.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

	List<Persona> findByNombres(String nombres);

	List<Persona> findByApellidoPaterno(String apellidopaterno);

	List<Persona> findByApellidoMaterno(String apellidomaterno);

	List<Persona> findByNumeroDocumento(String numerodocumento);

}
