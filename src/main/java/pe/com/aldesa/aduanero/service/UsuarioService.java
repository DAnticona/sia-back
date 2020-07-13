package pe.com.aldesa.aduanero.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import pe.com.aldesa.aduanero.entity.Usuario;

public interface UsuarioService {
	
	/**
	 * Carga datos del usuario
	 * 
	 * @param username
	 * @return
	 */
	UserDetails loadUserByUsername(String username);
	
	List<Usuario> findAll();
	
	Usuario findById(Long id);
	
	Usuario save(Usuario usuario);
	
	Usuario update(Usuario usuario);
	
	void delete(Integer id);

}
