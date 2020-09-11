package pe.com.aldesa.aduanero.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Chofer;
import pe.com.aldesa.aduanero.entity.Direccion;
import pe.com.aldesa.aduanero.entity.TipoDocumento;

@Repository
public interface ChoferRepository extends JpaRepository<Chofer, Long> {

	@Override
	Page<Chofer> findAll(Pageable pageable);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Chofer u SET u.nombres = :nombres, "
			+ "u.apellidoPaterno = :apellidoPaterno, u.apellidoMaterno = :apellidoMaterno, "
			+ "u.tipoDocumento = :tipoDocumento, u.numeroDocumento = :numeroDocumento, "
			+ "u.sexo = :sexo, u.fechaNacimiento = :fechaNacimiento, u.email = :email, "
			+ "u.direccion = :direccion, u.numeroLicencia = :numeroLicencia WHERE u.idPersona = :id")
	void updateChofer(Long id, String nombres, String apellidoPaterno, String apellidoMaterno,
			TipoDocumento tipoDocumento, String numeroDocumento, Character sexo, Date fechaNacimiento, String email,
			Direccion direccion, String numeroLicencia);

}
