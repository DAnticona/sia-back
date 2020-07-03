package pe.com.aldesa.aduanero.security.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.jsonwebtoken.Claims;

/**
 * Esta clase encapsula el token de acceso generado por la aplicación
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
public class AccessJwtToken implements JwtToken {
	private final String rawToken;
	@JsonIgnore
	private Claims claims;

	protected AccessJwtToken(final String token, Claims claims) {
		this.rawToken = token;
		this.claims = claims;
	}

	@Override
	public String getToken() {
		return this.rawToken;
	}
	
	public Claims getClaims() {
        return claims;
    }

}
