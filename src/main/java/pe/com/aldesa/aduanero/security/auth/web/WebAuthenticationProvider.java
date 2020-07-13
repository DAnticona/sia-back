package pe.com.aldesa.aduanero.security.auth.web;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import pe.com.aldesa.aduanero.security.model.UserContext;
import pe.com.aldesa.aduanero.service.UsuarioService;

/**
 * Esta clase tiene las siguientes responsabilidades:
 * <ol>
 * <li>Verifica las credenciales de usuario contra la base de datos</li>
 * <li>Si el usuario y/o contraseña no hacen match con los registros de la base de datos lanza una excepción</li>
 * <li>Crea un {@link UserContext} con los datos necesarios (usuario y privilegios)</li>
 * <li>Luego de la autenticación delega la creación del JWT token en {@link WebAwareAuthenticationSuccessHandler}</li>
 * </ol>
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
@Component
public class WebAuthenticationProvider implements AuthenticationProvider {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebAuthenticationProvider.class);
	
	private final BCryptPasswordEncoder encoder;
    private final UsuarioService userService;
    
    @Autowired
    public WebAuthenticationProvider(final UsuarioService userService, final BCryptPasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Assert.notNull(authentication, "No se proporcionaron datos de autenticación");

        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        
        LOGGER.info("Username: {}", username);

        UserDetails user = userService.loadUserByUsername(username);
        
        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Autenticación falló. Username o Password no válido.");
        }

        if (user.getAuthorities() == null) 
        	throw new InsufficientAuthenticationException("User no tiene roles asignados");
        
        List<GrantedAuthority> authorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());
        
        UserContext userContext = UserContext.create(user.getUsername(), authorities);
        
        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
