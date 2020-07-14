package pe.com.aldesa.aduanero.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pe.com.aldesa.aduanero.entity.Usuario;
import pe.com.aldesa.aduanero.repository.AuthorizationRepository;

/**
 * Implementación por defecto de {@link AuthorizationService}
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
@Service
public class DefaultAuthorizationUsuarioService implements AuthorizationService {

	@Autowired
	private AuthorizationRepository authorizationRepository;

	@Override
	public Optional<Usuario> loadUserByUsername(String username) {

		Usuario user = authorizationRepository.findUserByUsername(username);
		return Optional.ofNullable(user);
	}

}
