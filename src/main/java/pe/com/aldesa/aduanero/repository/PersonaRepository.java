package pe.com.aldesa.aduanero.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Direccion;
import pe.com.aldesa.aduanero.entity.Persona;
import pe.com.aldesa.aduanero.entity.TipoDocumento;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

	@Query("select p from Persona p where p.nombres like %:nombres% or p.apellidoPaterno like %:nombres% or p.apellidoMaterno like %:nombres%")
	List<Persona> searchByNombres(String nombres);

	@Query("select p from Persona p where p.numeroDocumento = :numeroDocumento")
	Optional<Persona> findByNumeroDocumento(String numeroDocumento);

	@Override
	Page<Persona> findAll(Pageable pageable);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Persona u SET u.nombres = :nombres, "
			+ "u.apellidoPaterno = :apellidoPaterno, u.apellidoMaterno = :apellidoMaterno, "
			+ "u.tipoDocumento = :tipoDocumento, u.numeroDocumento = :numeroDocumento, "
			+ "u.sexo = :sexo, u.fechaNacimiento = :fechaNacimiento, u.email = :email, "
			+ "u.direccion = :direccion WHERE u.idPersona = :id")
	void updatePersona(Long id, String nombres, String apellidoPaterno, String apellidoMaterno,
			TipoDocumento tipoDocumento, String numeroDocumento, Character sexo, Date fechaNacimiento, String email,
			Direccion direccion);

}
