package pe.com.aldesa.aduanero.util;

import pe.com.aldesa.aduanero.entity.Usuario;
import pe.com.aldesa.aduanero.security.auth.web.AuthUserData;

public class WebUtil {
	
	private WebUtil() {
		throw new IllegalStateException();
	}
	
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
