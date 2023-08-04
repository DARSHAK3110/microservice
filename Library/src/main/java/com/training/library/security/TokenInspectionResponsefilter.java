package com.training.library.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TokenInspectionResponsefilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		OAuth2IntrospectionAuthenticatedPrincipal principal = (OAuth2IntrospectionAuthenticatedPrincipal) SecurityContextHolder
				.getContext().getAuthentication().getPrincipal();
		List<String> authoritiesList = principal.getAttribute("scopes");
		List<SimpleGrantedAuthority> authorities = List.of();
		if (authoritiesList != null) {
			authorities = List.of(new SimpleGrantedAuthority(authoritiesList.get(0)));
		}
		UsernamePasswordAuthenticationToken newUserToken = new UsernamePasswordAuthenticationToken(
				principal.getUsername(), request.getHeader("Authorization"), authorities);
		newUserToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		SecurityContextHolder.getContext().setAuthentication(newUserToken);
		filterChain.doFilter(request, response);
	}

}
