package pe.com.aldesa.aduanero.service;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Esta clase maneja la capa de negocio que interactúa con los datos del usuario
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
public interface UserService {
	
	/**
	 * Carga datos del usuario
	 * 
	 * @param username
	 * @return
	 */
	UserDetails loadUserByUsername(String username);

}
