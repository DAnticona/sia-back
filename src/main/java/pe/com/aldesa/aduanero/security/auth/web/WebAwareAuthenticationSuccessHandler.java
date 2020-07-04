package pe.com.aldesa.aduanero.security.auth.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import pe.com.aldesa.aduanero.security.model.UserContext;
import pe.com.aldesa.aduanero.security.model.token.JwtToken;
import pe.com.aldesa.aduanero.security.model.token.JwtTokenFactory;

/**
 * Esta clase es lamada cuando se ha producido una autenticación.
 * Esta clase se encarga de agregar el token al JSON payload que va en el cuerpo de la respuesta HTTP
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
@Component
public class WebAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	
	private final ObjectMapper mapper;
    private final JwtTokenFactory tokenFactory;
    
    @Autowired
    public WebAwareAuthenticationSuccessHandler(final ObjectMapper mapper, final JwtTokenFactory tokenFactory) {
        this.mapper = mapper;
        this.tokenFactory = tokenFactory;
    }

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		UserContext userContext = (UserContext) authentication.getPrincipal();

		JwtToken accessToken = tokenFactory.createAccessJwtToken(userContext);
        
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", accessToken.getToken());
        
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        mapper.writeValue(response.getWriter(), tokenMap);

        clearAuthenticationAttributes(request);
	}

	/**
	 * Remueve temporalmente los datos de autenticación que han sido almacenados en una sesión durante el proceso de autenticación
	 * 
	 * @param request
	 */
	protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
