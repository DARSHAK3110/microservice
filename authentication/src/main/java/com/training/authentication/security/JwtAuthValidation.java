package com.training.authentication.security;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.lang.Strings;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthValidation extends OncePerRequestFilter {
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserDetailsService userDetailService;

	@Override
	protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response,
			@Nonnull FilterChain filterChain) throws ServletException, IOException {
		String username;
		String authHeader = request.getHeader("Authorization");
		String jwt;
		if (authHeader == null || !authHeader.startsWith("Bearer")) {
			authHeader = request.getHeader("authorization");
			if (authHeader == null || !authHeader.startsWith("Basic")) {
				filterChain.doFilter(request, response);
				return;
			}
			BufferedReader reader = request.getReader();
			authHeader = reader.readLine();
			jwt = Strings.replace(authHeader, "token=", "");
		} else {
			jwt = Strings.replace(authHeader, "Bearer ", "");
		}
		
		try {
			username = jwtService.extractUsername(jwt);
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = this.userDetailService.loadUserByUsername(username);
				
				UsernamePasswordAuthenticationToken newUserToken = new UsernamePasswordAuthenticationToken(
						userDetails.getUsername(), jwt, userDetails.getAuthorities());
				newUserToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(newUserToken);
			}

		} catch (Exception e) {
			filterChain.doFilter(request, response);
		}
				filterChain.doFilter(request, response);
	}
}
