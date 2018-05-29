package com.moustercorp.symemysv2

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.context.annotation.ComponentScan
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moustercorp.config.spring.security.UserDetailTest

/**
 * @author jbatis
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ComponentScan(basePackages = "com.moustercorp")
@Ignore
class JWTTestUtil {

	private MockMvc mockMvc;
	private static Set<Class> inited = new HashSet<>();
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@Before
	public void setup() {
		mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
	}
	
	@Before
	public void init() throws Exception {
		if (!inited.contains(getClass())) {
			inited.add(getClass());
		}
	}
	
	/**
	 * Contructor sin argumentos
	 */
	public JWTTestUtil() {
		super()
	}
	
	/**
	 * @param o
	 * @return objeto representado en formato json
	 * @throws IOException
	 */
	protected String json(Object o) throws IOException {
		mapper.writeValueAsString(o);
	}
	
	/**
	 * @param auth detalles del usuario a autenticar
	 * @return {@link MvcResult} 
	 * @throws Exception si el servicio nos regresa un 401, es decir no autenticado.
	 */
	protected MvcResult doLogin(UserDetailTest auth) throws Exception {
		getMockMvc().perform(MockMvcRequestBuilders.post("/login")
			.content(json(auth)))
			.andExpect(status().isOk())
			.andReturn()
	}

	
	protected MockMvc getMockMvc() {
		mockMvc;
	}
	
}
