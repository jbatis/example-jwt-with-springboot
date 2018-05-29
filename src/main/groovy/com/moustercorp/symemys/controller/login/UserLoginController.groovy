/**
 * 
 */
package com.moustercorp.symemys.controller.login

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import org.yaml.snakeyaml.util.ArrayUtils

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.moustercorp.symemys.controller.login.UserLoginController.Usaurio

import org.springframework.web.bind.annotation.ResponseBody

/**
 * Defino el servicio de login para los usuarios que lo necesiten.
 * @author jbatis
 */
@RestController
class UserLoginController {

	/**
	 * Contructor sin argumentos
	 */
	public UserLoginController() {
		super();
	}

	@RequestMapping(value="/doPrueba", method=RequestMethod.GET, produces="application/json")
	def String doPrueba() {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(new Usaurio('Juan', 'jbatis', '1'));
	}
	
	@RequestMapping(value="/users", method=RequestMethod.GET, produces="application/json")
	def @ResponseBody String getUsers() {
	  return getAllUsersTests();
	}
	
	String getAllUsersTests() {
		List<Usaurio> listUsers = new ArrayList();
		listUsers.add(new Usaurio('Juan', 'jbatis', '1'))
		listUsers.add(new Usaurio('Carlos', 'mostercorp', '2'))
		
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(listUsers);
	}
	
	
	class Usaurio {
		String nombre;
		String usuario;
		String id;
		
		@JsonCreator
		Usaurio(@JsonProperty("nombre") String nombre, 
			@JsonProperty("usuario") String usuario, 
			@JsonProperty("id") String id) {
			this.nombre = nombre;
			this.usuario = usuario;
			this.id = id;
		}
		
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public String getUsuario() {
			return usuario;
		}
		public void setUsuario(String usuario) {
			this.usuario = usuario;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		
		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("nombre: ")
					.append(this.nombre).append("\n")
					.append("usuario: ")
					.append(this.usuario).append("\n")
					.append("id: ")
					.append(this.id).append("\n")
	
			return stringBuilder.toString();
		}
		
	}
	
}
