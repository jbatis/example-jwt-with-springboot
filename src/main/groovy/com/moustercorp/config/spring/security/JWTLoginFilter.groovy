/**
 * 
 */
package com.moustercorp.config.spring.security

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

import com.fasterxml.jackson.databind.ObjectMapper

import io.jsonwebtoken.Clock
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultClock

/**
 * Procesa el envío de información para la autenticación.
 * Los formularios de inicio de sesión deben presentar dos parámetros para este filtro: 
 * un nombre de usuario y contraseña.
 * @author jbatis
 */
class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

	private Clock clock = DefaultClock.INSTANCE;
	
	/**
	 * Autentication manneger
	 */
	private AuthenticationManager authenticationManager
	
	/**
	 * @param url
	 * @param authManager
	 */
	public JWTLoginFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager
	}
	
	/**
	 * 
	 * Este metodo realiza lo siguiente: <br>
	 * <ul>
	 * 	<li>1.- Devuelve un token de autenticación con la información del usuario autenticado, lo que indica una autenticación exitosa.</li>
	 *  <li>2.- Devuelve nulo, lo que indica que el proceso de autenticación aún está en progreso. Antes de regresar, 
	 *      la implementación debe realizar cualquier trabajo adicional requerido para completar el proceso.</li>
	 *  <li>3.- Lanza una AuthenticationException si el proceso de autenticación falla.</li>
	 * </ul>
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
	throws AuthenticationException {
		try {
			AccountCredentials creds = new ObjectMapper().readValue(request.getInputStream(), AccountCredentials.class);
			return authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(creds.getUsername(), creds.getPassword(), Collections.emptyList()));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * En caso de exito, generamos el token con la informacion relacionada al usuario autenticado.
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth) 
	throws IOException, ServletException {
		
		Date date = clock.now();
		println date
		long minutos = 50l;
		Date newDate = new Date(date.getTime() + (minutos * 10000))
		println newDate
		
		Map<String, Object> claims = new HashMap<>();
		
		String token = Jwts.builder()
			.setClaims(claims)
			.setSubject(((User)auth.getPrincipal()).getUsername())
			.setIssuedAt(date)
			.setExpiration( newDate )
			.signWith(SignatureAlgorithm.HS512, ConfigSecurityParams.SUPER_SECRET_KEY)
			.setIssuer(ConfigSecurityParams.ISSUER_INFO)
			.setAudience("app/customers")
			.compact();
		String beareHeader = new StringBuilder(ConfigSecurityParams.TOKEN_BEARER_PREFIX).append(token).toString();
		println "responce: " + beareHeader
		response.addHeader(ConfigSecurityParams.HEADER_AUTHORIZACION_KEY, beareHeader);
	}

}
