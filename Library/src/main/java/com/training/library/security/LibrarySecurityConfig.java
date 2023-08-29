package com.training.library.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class LibrarySecurityConfig {

	@Value(value = "${spring.security.oauth2.resourceserver.opaquetoken.introspection-uri}")
	private String introspectionUri;
	@Value(value = "${spring.security.oauth2.client.registration.library.client-id}")
	private String clientId;
	@Value(value = "${spring.security.oauth2.client.registration.library.client-secret}")
	private String clientSecret;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
		DefaultBearerTokenResolver resolver = new DefaultBearerTokenResolver();
		resolver.setAllowUriQueryParameter(true);
		resolver.setBearerTokenHeaderName(HttpHeaders.AUTHORIZATION);
		security.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
		.authorizeHttpRequests(auth->auth.anyRequest().authenticated())
				.sessionManagement(
						sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.oauth2ResourceServer(oauth2 -> oauth2.bearerTokenResolver(resolver)
						.opaqueToken(opaqueToken -> opaqueToken.introspector(myIntrospector())));
		return security.build();
	}

	@Bean
	public OpaqueTokenIntrospector myIntrospector() {
		return new NimbusOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret);
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
