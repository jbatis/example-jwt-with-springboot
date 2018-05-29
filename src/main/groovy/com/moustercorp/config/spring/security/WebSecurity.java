/**
 * 
 */
package com.moustercorp.config.spring.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Clase que permite habilitar y configurar la seguridad del aplicativo.
 * @author jbatis
 */
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private JwtAuthenticationEntryPoint expressionHandler;
	
	/**
	 * Constructor sin argumentos
	 */
	public WebSecurity() {
		super();
	}
	
	/**
 	 * Configuramos la seguridad base de spring security para las peticiones http. 
 	 * Recordemos que nuestro apicativo es stateless, y va a exponer servicios solo
 	 * para usuarios autenticados e identificados por el aplicativo.
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			/*
			 * Ya que nuestra aplicación es stateless, no necesitamos manejar sessiones.
			 */
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				/*
				 * Cross-origin resource sharing (CORS) debe ser procesada antes que nada, 
				 * ya que la solicitud previa no contendrá ninguna cookie y 
				 * la solicitud determinará que el usuario no está autenticado y la rechazará. 
				 */
				.cors()
			.and() 
				//Vamos a deshabilitar el Cross-site para todas las solicitudes
				.csrf().disable().exceptionHandling().authenticationEntryPoint(expressionHandler)
			.and()
				.authorizeRequests()
					//Unicamante exponemos y permitimos a todos via POST la ruta '/login'
					.antMatchers(HttpMethod.POST, "/login").permitAll()
					//y para todos los demás deshabilitamos cualquier solicitud
					.anyRequest().authenticated() 
			.and()
				// We filter the api/login requests
				.addFilter(new JWTLoginFilter(authenticationManager()))
				// And filter other requests to check the presence of JWT in header
				.addFilter(new JWTAuthenticationFilter(authenticationManager()));
		
	}
	
	/**
	 * Configuramos el Bean CORS con el mecanismo que solo admite solicitudes 
	 * seguras entre dominios y transferencias de datos entre navegadores y servidores web.
	 * @return {@link CorsConfigurationSource} 
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		//Por el momento permito de todos los origenes
		configuration.setAllowedOrigins(Arrays.asList("*"));
		//Solo permitiremos GET y POST
		configuration.setAllowedMethods(Arrays.asList("GET","POST"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
	
	/**
	 * Solo configuramos e instanciamos el BCryptPasswordEncoder para codificar el password
	 * @return {@link BCryptPasswordEncoder}
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * @return instancia bean con el detalle de usuario pruebas
	 */
	@Bean
	public UserDetailTest userDetailTest() {
		return new UserDetailTest();
	}
	
	/**
	 * Esta configuración solo es para emular la autenticacion, la generamos en memoria temporal.
	 * Tu deberias generar el propio a partir de tus necesidades.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) 
	throws Exception {
		UserDetailTest usterTest = userDetailTest();
		// Creamos una cuenta default
		String password = usterTest.getPassword();
		String user = usterTest.getUsername();
		PasswordEncoder passwordEncoder = bCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(password);
		//generamos usuario en memoria, para fines de prueba.
		auth.inMemoryAuthentication().withUser(user).password(hashedPassword).roles("ADMIN");
	}
	
}
