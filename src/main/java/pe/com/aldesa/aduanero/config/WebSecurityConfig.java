package pe.com.aldesa.aduanero.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import pe.com.aldesa.aduanero.security.CustomCorsFilter;
import pe.com.aldesa.aduanero.security.auth.jwt.JwtAuthenticationProvider;
import pe.com.aldesa.aduanero.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import pe.com.aldesa.aduanero.security.auth.jwt.SkipPathRequestMatcher;
import pe.com.aldesa.aduanero.security.auth.web.WebAuthenticationProvider;
import pe.com.aldesa.aduanero.security.auth.web.WebLoginProcessingFilter;
import pe.com.aldesa.aduanero.security.extractor.TokenExtractor;

/**
 * Gestiona la configuración de la capa de seguridad en la aplicación.
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String JWT_TOKEN_HEADER_PARAM = "Authorization";
    public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/core-adesa/auth/login";
    public static final String FORM_BASED_LOGIN_MAP = "/auth/login";
    public static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/**";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/core-adesa/auth/token";
    
    @Autowired 
    private AuthenticationSuccessHandler successHandler;
    @Autowired 
    private AuthenticationFailureHandler failureHandler;
    @Autowired 
    private WebAuthenticationProvider ajaxAuthenticationProvider;
    @Autowired 
    private JwtAuthenticationProvider jwtAuthenticationProvider;
    @Autowired 
    private TokenExtractor tokenExtractor;
    @Autowired 
    private AuthenticationManager authenticationManager;
    @Autowired 
    private ObjectMapper objectMapper;
    
    protected WebLoginProcessingFilter buildAjaxLoginProcessingFilter() throws Exception {
        WebLoginProcessingFilter filter = new WebLoginProcessingFilter(FORM_BASED_LOGIN_MAP, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }
    
    protected JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {
        List<String> pathsToSkip = Arrays.asList(TOKEN_REFRESH_ENTRY_POINT, FORM_BASED_LOGIN_ENTRY_POINT);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip, TOKEN_BASED_AUTH_ENTRY_POINT);
        JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(failureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(this.authenticationManager);
        return filter;
    }
    
    @Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
    
    @Override
	protected void configure(AuthenticationManagerBuilder auth) {
		auth.authenticationProvider(ajaxAuthenticationProvider);
		auth.authenticationProvider(jwtAuthenticationProvider);
	}
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        .csrf().disable()
        .exceptionHandling()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
                .antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll()
                .antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll()
        .and()
            .authorizeRequests()
                .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated()
        .and()
        	.addFilterBefore(new CustomCorsFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(buildAjaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
