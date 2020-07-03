package pe.com.aldesa.aduanero.dao;

import pe.com.aldesa.aduanero.entity.User;

/**
 * Esta clase permite realizar las sentencias en base de datos sobre la tabla users
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
public interface UserDAO {
	
	/**
	 * Encuentra un usuario a través de su username
	 * 
	 * @param username
	 * @return
	 */
	User findUserByUsername(String username);

}
