/**
 * 
 */
package com.moustercorp.config.spring.security

/**
 * Clase que se utiliza para configurar el JWT
 * @author jbatis
 */
final class ConfigSecurityParams {

	public static final String ISSUER_INFO = "http://localhost:9090/";
	public static final String SUPER_SECRET_KEY = "secretKeys";
	public static final long TOKEN_EXPIRATION_TIME = 864_000_000;
	public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
	public static final String TOKEN_BEARER_PREFIX = "Bearer ";
	
	/**
	 * Constructor sin argumentos
	 */
	private ConfigSecurityParams() {
		super();
	}

}
