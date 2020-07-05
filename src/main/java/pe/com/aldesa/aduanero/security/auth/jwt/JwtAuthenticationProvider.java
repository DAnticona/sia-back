package pe.com.aldesa.aduanero.security.auth.jwt;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import pe.com.aldesa.aduanero.config.JwtSettings;
import pe.com.aldesa.aduanero.security.auth.JwtAuthenticationToken;
import pe.com.aldesa.aduanero.security.model.UserContext;
import pe.com.aldesa.aduanero.security.model.token.RawAccessJwtToken;

/**
 * JwtAuthenticationProvider tiene las siguientes responsabilidades:
 * <p>
 * <ol>
 * <li>Verifica la firma del token.</li>
 * <li>Extrae el <tt>sub</tt> y los <tt>claims</tt> del token y los usa para crear un objeto {@link UserContext}.</li>
 * <li>Si el token está mal formado, expira o si está firmado inapropiadamente con la clave de autenticación se lanzará una exception.</li>
 * </ol>
 *  
 * @author Juan Pablo Cánepa Alvarez
 *
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
	
	private final JwtSettings jwtSettings;
	
	@Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

	@SuppressWarnings("unchecked")
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		RawAccessJwtToken rawAccessToken = (RawAccessJwtToken) authentication.getCredentials();
		
        Jws<Claims> jwsClaims = rawAccessToken.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = jwsClaims.getBody().getSubject();
        List<String> scopes = jwsClaims.getBody().get("scopes", List.class);
        List<GrantedAuthority> authorities = scopes.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        
        UserContext context = UserContext.create(subject, authorities);
        
        return new JwtAuthenticationToken(context, context.getAuthorities());
    }

	@Override
	public boolean supports(Class<?> authentication) {
		return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
	}
	

}
