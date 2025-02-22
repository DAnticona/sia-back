package pe.com.aldesa.aduanero.security.auth;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import pe.com.aldesa.aduanero.security.model.UserContext;
import pe.com.aldesa.aduanero.security.model.token.RawAccessJwtToken;

/**
 * Esta clase establece la autenticación mediante una colección de objetos GrantedAuthority
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 8565977445245093877L;

	private RawAccessJwtToken rawAccessToken;
	private UserContext userContext;

	public JwtAuthenticationToken(RawAccessJwtToken unsafeToken) {
		super(null);
		this.rawAccessToken = unsafeToken;
		this.setAuthenticated(false);
	}

	public JwtAuthenticationToken(UserContext userContext, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.eraseCredentials();
		this.userContext = userContext;
		super.setAuthenticated(true);
	}

	@Override
	public void setAuthenticated(boolean authenticated) {
		if (authenticated) {
			throw new IllegalArgumentException(
					"No se puede establecer este token como confiable: En su lugar use un constructor que tome una lista de GrantedAuthority");
		}
		super.setAuthenticated(false);
	}

	@Override
	public Object getCredentials() {
		return rawAccessToken;
	}

	@Override
	public Object getPrincipal() {
		return this.userContext;
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.rawAccessToken = null;
	}

}
