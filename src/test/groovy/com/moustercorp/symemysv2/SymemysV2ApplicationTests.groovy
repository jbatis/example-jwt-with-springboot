package com.moustercorp.symemysv2

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals

import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MvcResult
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.util.StringUtils
import org.springframework.web.client.RestTemplate

import com.jayway.jsonpath.JsonPath
import com.moustercorp.config.spring.security.UserDetailTest

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

class SymemysV2ApplicationTests extends JWTTestUtil {
	
	private org.slf4j.Logger log = LoggerFactory.getLogger "SymemysV2ApplicationTests";
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	/**
	 * Este metodo prueba y valida de forma exitosa el login de usuario.
	 * Ocupando el usuario default
	 * @throws Exception
	 */
	@Test
	public void testSuccessLogin() throws Exception {
		final UserDetailTest auth = new UserDetailTest();
		doLogin(auth)
	}

	/**
	 * Este metodo estresa el sistema con un usuario y/o password incorrecto
	 * Para validar su comportamiento bajo estas condiciones.
	 * @throws Exception
	 */
	@Test
	public void testFailedLogin() throws Exception {
		final UserDetailTest auth = new UserDetailTest();
		auth.setUsername("juan")
		auth.setPassword("failedPass")
		doLogin(auth)
	}
	
	/**
	 * Realizamos una petición a un recurso, el cual va a ser denegado
	 * ya que no se esta auntenticado en el sistema.
	 */
	@Test
	public void testResourceAccessDenied() {
		String body = this.restTemplate.getForObject("/users", String.class);
		Object item = JsonPath.parse(body).read('$.message');
		assertThat(item).isEqualTo("Access Denied");
	}
	
	/**
	 * Permite autenticar y validar el usuario para obtener el token.
	 * En base al token solicitamos un recurso REST y lo obtenemos de 
	 * forma exitosa.
	 * @throws Exception
	 */
	@Test
	public void testCanGetTokenAndAuthenticationAndUsers() throws Exception {
		final UserDetailTest auth = new UserDetailTest();
		String token = doLogin(auth).getResponse().getHeader("Authorization")
		log.info "Token Authorization [{}]", token.substring(7)
		getMockMvc().perform(MockMvcRequestBuilders.get("/users")
			.header("Authorization", token))
			.andExpect(status().isOk())
			.println()
	}

	
}
