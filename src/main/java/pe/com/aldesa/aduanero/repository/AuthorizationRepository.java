package pe.com.aldesa.aduanero.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.Usuario;

/**
 * Esta clase solo se emplea para el proceso de autenticación y autorización de usuarios
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
@Repository
public interface AuthorizationRepository extends JpaRepository<Usuario, Integer> {
	
	/**
	 * Encuentra un usuario a través de su username
	 * 
	 * @param username
	 * @return
	 */
	Usuario findUserByUsername(String username);

}
