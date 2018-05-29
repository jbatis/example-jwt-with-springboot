/**
 * 
 */
package com.moustercorp.config.spring.security

import java.io.IOException

import javax.naming.AuthenticationException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

/**
 * Usada para manejar autorizaciones exceptiones de tipo {@link ExceptionTranslationFilter}
 * @author jbatis
 */
@Component
class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
	
	private static final long serialVersionUID = -8970718410437077606L;
	
	
	/**
	 * Constructor sin argumentos
	 */
	public JwtAuthenticationEntryPoint() {
		super();
	}

	/**
	 * Comienza un esquema de autenticación.
	 * @see org.springframework.security.web.AuthenticationEntryPoint#commence(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			org.springframework.security.core.AuthenticationException authException)
			throws IOException, ServletException {
		// This is invoked when user tries to access a secured REST resource without supplying any credentials
		// We should just send a 401 Unauthorized response because there is no 'login page' to redirect to
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
	}
	
}
