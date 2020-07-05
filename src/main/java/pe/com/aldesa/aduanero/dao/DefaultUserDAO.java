package pe.com.aldesa.aduanero.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import pe.com.aldesa.aduanero.entity.User;

/**
 * Implementación por defecto de {@link UserDAO}
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
@Repository
public class DefaultUserDAO implements UserDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public User findUserByUsername(String username) {
		Session session = sessionFactory.getCurrentSession();
		return session.get(User.class, username);
	}

}
