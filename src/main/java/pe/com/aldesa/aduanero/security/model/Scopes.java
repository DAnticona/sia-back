package pe.com.aldesa.aduanero.security.model;

/**
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
public enum Scopes {
    REFRESH_TOKEN;
    
    public String authority() {
        return "ROLE_" + this.name();
    }
}
