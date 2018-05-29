/**
 * 
 */
package com.moustercorp.config.spring.security

/**
 * @author jbatis
 */
class AccountCredentials {

	private String username;
	private String password;
	
	/**
	 * Constructor sin argumentos
	 */
	public AccountCredentials() {
		super();
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}
