package pe.com.aldesa.aduanero.security;

import org.springframework.security.core.AuthenticationException;

import pe.com.aldesa.aduanero.security.model.token.JwtToken;

/** 
 * Esta excepción es lanzada cuando ha expirado el JWT Token
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
public class JwtExpiredTokenException extends AuthenticationException {
    private static final long serialVersionUID = -5959543783324224864L;
    
    private JwtToken token;

    public JwtExpiredTokenException(String msg) {
        super(msg);
    }

    public JwtExpiredTokenException(JwtToken token, String msg, Throwable t) {
        super(msg, t);
        this.token = token;
    }

    public String token() {
        return this.token.getToken();
    }
}
