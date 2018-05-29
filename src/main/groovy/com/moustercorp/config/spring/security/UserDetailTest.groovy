/**
 * 
 */
package com.moustercorp.config.spring.security

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * Clase que utilizamos unicamente para realizar pruebas de autentificación
 * emulando un resource
 * @author jbatis
 */
class UserDetailTest {

	String username = "jbatis";
	String password = "password";
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}
