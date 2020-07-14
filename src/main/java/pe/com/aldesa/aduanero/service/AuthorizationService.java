package pe.com.aldesa.aduanero.service;

import java.util.Optional;

import pe.com.aldesa.aduanero.entity.Usuario;

public interface AuthorizationService {
	
	/**
	 * Carga datos del usuario
	 * 
	 * @param username
	 * @return
	 */
	Optional<Usuario> loadUserByUsername(String username);
	
}
