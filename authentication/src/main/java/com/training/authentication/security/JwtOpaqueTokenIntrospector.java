package com.training.authentication.security;

import java.text.ParseException;

import org.springframework.security.oauth2.core.DefaultOAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nimbusds.jwt.proc.DefaultJWTProcessor;

import io.jsonwebtoken.Jwt;

@Component
public class JwtOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
	private OpaqueTokenIntrospector delegate = new NimbusOpaqueTokenIntrospector(
			"http://localhost:8090/api/v1/users/check_token", "library-client-id", "library-secret");
	private JwtDecoder jwtDecoder = new NimbusJwtDecoder(new ParseOnlyJWTProcessor());

	public OAuth2AuthenticatedPrincipal introspect(String token) {
		System.out.println("abcd");
//		OAuth2AuthenticatedPrincipal principal = this.delegate.introspect(token);
		Jwt jwt = (Jwt) this.jwtDecoder.decode(token);
		System.out.println(jwt);
		return new DefaultOAuth2AuthenticatedPrincipal(((org.springframework.security.oauth2.jwt.Jwt) jwt).getClaims(),
				null);

	}

	private static class ParseOnlyJWTProcessor extends DefaultJWTProcessor<SecurityContext> {
		public JWTClaimsSet process(SignedJWT jwt, SecurityContext context) throws JOSEException {
			try {
				return jwt.getJWTClaimsSet();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
}