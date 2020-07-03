package pe.com.aldesa.aduanero.security.model.token;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import pe.com.aldesa.aduanero.config.JwtSettings;
import pe.com.aldesa.aduanero.security.model.Scopes;
import pe.com.aldesa.aduanero.security.model.UserContext;

/**
 * Esta clase es una fábrica que entrega objetos que contienen el token de acceso y el refreshToken respectivamente
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
@Component
public class JwtTokenFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtTokenFactory.class);
	
	private final JwtSettings settings;

	@Autowired
	public JwtTokenFactory(JwtSettings settings) {
		this.settings = settings;
	}

	public JwtToken createAccessJwtToken(UserContext userContext) {
		if (StringUtils.isBlank(userContext.getUsername()))
			throw new IllegalArgumentException("No se puede crear el token JWT sin nombre de usuario");

		if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty())
			throw new IllegalArgumentException("El usuario no tiene privilegios");

		Claims claims = Jwts.claims().setSubject(userContext.getUsername());
		claims.put("scopes", userContext.getAuthorities().stream().map(GrantedAuthority::toString).collect(Collectors.toList()));

		LocalDateTime currentTime = LocalDateTime.now();

		String token = Jwts.builder().setClaims(claims).setIssuer(settings.getTokenIssuer())
				.setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
				.setExpiration(Date.from(currentTime.plusMinutes(settings.getTokenExpirationTime())
						.atZone(ZoneId.systemDefault()).toInstant()))
				.signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey()).compact();
		logger.info("Access Token: {}", token);
		return new AccessJwtToken(token, claims);
	}
	
	public JwtToken createRefreshToken(UserContext userContext) {
        if (StringUtils.isBlank(userContext.getUsername())) {
            throw new IllegalArgumentException("No se puede crear el token JWT sin nombre de usuario");
        }

        LocalDateTime currentTime = LocalDateTime.now();

        Claims claims = Jwts.claims().setSubject(userContext.getUsername());
        claims.put("scopes", Arrays.asList(Scopes.REFRESH_TOKEN.authority()));
        
        String token = Jwts.builder()
          .setClaims(claims)
          .setIssuer(settings.getTokenIssuer())
          .setId(UUID.randomUUID().toString())
          .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant()))
          .setExpiration(Date.from(currentTime
              .plusMinutes(settings.getRefreshTokenExpTime())
              .atZone(ZoneId.systemDefault()).toInstant()))
          .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
        .compact();
        logger.info("Refresh Token: {}", token);
        return new AccessJwtToken(token, claims);
    }

}
