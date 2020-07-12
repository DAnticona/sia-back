package pe.com.aldesa.aduanero.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pe.com.aldesa.aduanero.entity.Usuario;
import pe.com.aldesa.aduanero.repository.UsuarioRepository;

/**
 * Implementación por defecto de {@link UserService}
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
@Service
public class DefaultUserService implements UserService {

	@Autowired
	private UsuarioRepository userDAO;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) {
		
		Usuario user = userDAO.findUserByUsername(username);

		UserBuilder builder = null;
		if (user != null) {
			builder = org.springframework.security.core.userdetails.User.withUsername(username);
			builder.password(user.getPassword());
			
			String authorities = user.getIdRol().getNombre();
			builder.authorities(authorities);
		} else {
			throw new UsernameNotFoundException("Usuario no encontrado");
		}
		return builder.build();
	}

}
