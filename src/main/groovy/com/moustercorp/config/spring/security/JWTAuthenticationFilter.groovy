/**
 * 
 */
package com.moustercorp.config.spring.security

import java.util.function.Function

import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.codehaus.groovy.util.StringUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Clock
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.impl.DefaultClock

/**
 * Procesa una autorización BÁSICA para las peticiones HTTP y coloca el resultado 
 * en SecurityContextHolder.
 * @author jbatis
 */
class JWTAuthenticationFilter extends BasicAuthenticationFilter {
	
	/**
	 * Looger
	 */
	protected final Logger logger = LoggerFactory.getLogger(getClass())
	
	/**
	 * user details
	 */
	private UserDetailsService userDetailsService;
	
	private Clock clock = DefaultClock.INSTANCE;
	
	/**
	 * @param authManager 
	 */
	public JWTAuthenticationFilter(AuthenticationManager authManager) {
		super(authManager);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
	throws IOException, ServletException {
		logger.debug("Proceso de autenticacion para: {}", req.getRequestURL());
		String token = req.getHeader(ConfigSecurityParams.HEADER_AUTHORIZACION_KEY);
		if (token == null || !token.startsWith(ConfigSecurityParams.TOKEN_BEARER_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		// Se identifica solo el token y se recupera el usuario.
		token = token.substring(7);
		def username = getUsernameFromToken(token);
		logger.debug("username  {} ", username);
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// Esto esta fuera del alcance del curso, aqui es tu responsabilidad para recuperar
			//el detalle del usuario
			//UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			
			// solo validamos algunos aspectos, asociados al token
			if (validateToken(token)) {
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			
		}
		chain.doFilter(req, res);
	}

	private def Boolean validateToken(String token) {
        final String username = getUsernameFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);
		return isTokenExpired(token);
    }
	
	private def Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		final Date now = clock.now();
		return !expiration.before(now);
	}
	
	private def Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, {Claims c -> c.getExpiration()});
	}
	
	private def Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, {Claims c -> c.getIssuedAt()});
	}
	
	private def String getUsernameFromToken(String token) {
		try {
			return getClaimFromToken(token, {Claims c -> c.getSubject()})
		} catch (IllegalArgumentException e) {
			logger.error("an error occured during getting username from token", e);
		} catch (ExpiredJwtException e) {
			logger.warn("the token is expired and not valid anymore", e);
		}
		return null;
	}
	
	private def <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	private def Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
			.setSigningKey(ConfigSecurityParams.SUPER_SECRET_KEY)
			.parseClaimsJws(token)
			.getBody();
	}
	
}
