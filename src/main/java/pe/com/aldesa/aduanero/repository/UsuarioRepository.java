package pe.com.aldesa.aduanero.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Direccion;
import pe.com.aldesa.aduanero.entity.Rol;
import pe.com.aldesa.aduanero.entity.TipoDocumento;
import pe.com.aldesa.aduanero.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Usuario u SET u.username = :username, u.rol = :rol, u.nombres = :nombres, "
			+ "u.apellidoPaterno = :apellidoPaterno, u.apellidoMaterno = :apellidoMaterno, "
			+ "u.tipoDocumento = :tipoDocumento, u.numeroDocumento = :numeroDocumento, "
			+ "u.sexo = :sexo, u.fechaNacimiento = :fechaNacimiento, u.email = :email, "
			+ "u.imagen = :imagen, u.direccion = :direccion, u.activo = :activo WHERE u.idPersona = :id")
	void updateUsuario(Long id, String username, Rol rol, String nombres, String apellidoPaterno,
			String apellidoMaterno, TipoDocumento tipoDocumento, String numeroDocumento, Character sexo,
			Date fechaNacimiento, String email, String imagen, Direccion direccion, String activo);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Usuario u SET u.password = :password WHERE u.idPersona = :id")
	void updatePassword(Long id, String password);

}
