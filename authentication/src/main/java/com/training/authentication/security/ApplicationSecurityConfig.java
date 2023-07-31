package com.training.authentication.security;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableTransactionManagement
public class ApplicationSecurityConfig {

	@Autowired
	private AuthenticationProvider authenticationProvider;
	@Autowired
	private JwtAuthValidation jwtAuthValidation;

	@Qualifier("customAuthenticationEntryPoint")
	@Autowired
	private AuthenticationEntryPoint authEntryPoint;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
		security.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.sessionManagement(
						sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider)
				.addFilterBefore(jwtAuthValidation, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint));
		 OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(security);
		return security.build();
	}

	  @Bean
	    public RegisteredClientRepository registeredClientRepository() {
	        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
	          .clientId("library-client-id")
	          .clientSecret("library-secret")
	          .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
	          .authorizationGrantType(AuthorizationGrantType.JWT_BEARER)
	          .build();
	        return new InMemoryRegisteredClientRepository(registeredClient);
	    }
	  
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(java.util.Arrays.asList("*"));
		configuration.setAllowedMethods(java.util.Arrays.asList("*"));
		configuration.setAllowCredentials(true);
		configuration.addAllowedOrigin("http://localhost:4200");
		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

}
