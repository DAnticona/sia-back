package pe.com.aldesa.aduanero.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pe.com.aldesa.aduanero.dao.UserDAO;
import pe.com.aldesa.aduanero.entity.Authority;
import pe.com.aldesa.aduanero.entity.User;

@Service
public class DefaultUserService implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Transactional
	@Override
	public UserDetails loadUserByUsername(String username) {
		
		User user = userDAO.findUserByUsername(username);

		UserBuilder builder = null;
		if (user != null) {
			builder = org.springframework.security.core.userdetails.User.withUsername(username);
			builder.disabled(!user.isEnabled());
			builder.password(user.getPassword());
			
			String[] authorities = user.getAuthorities().stream().map(Authority::getAuthority).toArray(String[]::new);
			builder.authorities(authorities);
		} else {
			throw new UsernameNotFoundException("User not found");
		}
		return builder.build();
	}

}
