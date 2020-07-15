package pe.com.aldesa.aduanero.util;

import pe.com.aldesa.aduanero.entity.Usuario;
import pe.com.aldesa.aduanero.security.auth.web.AuthUserData;

public class WebUtil {
	
	private WebUtil() {
		throw new IllegalStateException();
	}
	
	/**
	 * Este metodo crea un objeto que contiene los datos que enviarán junto
	 * con el token en la respuesta Http
	 *
	 */
	public static AuthUserData getAuthUser(Usuario usuario) {
		AuthUserData authUser = new AuthUserData();
		authUser.setId(usuario.getIdPersona());
		authUser.setName(usuario.getNombres());
		authUser.setLastname(usuario.getApellidoPaterno());
		authUser.setUsername(usuario.getUsername());
		authUser.setEmail(usuario.getEmail());
		authUser.setRol(usuario.getIdRol().getNombre());
		
		return authUser;
	}

}
