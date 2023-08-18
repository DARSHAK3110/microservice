package com.training.authentication.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.training.authentication.dto.request.FilterDto;
import com.training.authentication.dto.request.TokenRequestDto;
import com.training.authentication.dto.request.UserLoginRequestDto;
import com.training.authentication.dto.request.UserRequestDto;
import com.training.authentication.dto.response.ClaimsResponseDto;
import com.training.authentication.dto.response.CustomBaseResponseDto;
import com.training.authentication.dto.response.TokenResponseDto;
import com.training.authentication.dto.response.UserResponseDto;
import com.training.authentication.service.UserService;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@CrossOrigin
@PropertySource("classpath:message.properties")
@RequestMapping("/api/v1/users")
public class UserController {
	
	@Autowired
	private UserService userSerivceImpl;
	Logger log = Logger.getLogger(UserController.class.getName());
	@Autowired
	private AuthenticationManager authenticationManager;

	@GetMapping // get all user
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<Page<UserResponseDto>> getAllUsers(FilterDto searchWord) {
		Page<UserResponseDto> allUsers = this.userSerivceImpl.getAllUsers(searchWord);
		return ResponseEntity.of(Optional.of(allUsers));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal")
	@GetMapping("/{userId}") // get user
	public ResponseEntity<UserResponseDto> getUser(@PathVariable @Min(1) String userId) {
		UserResponseDto user = this.userSerivceImpl.getUser(Long.parseLong(userId));
		return ResponseEntity.of(Optional.of(user));
	}

	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping("/check_token")
	@PreAuthorize("isAuthenticated()")
	public ResponseEntity<?> checkToken(HttpServletRequest request) throws IOException {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String jwt = (String) authentication.getCredentials();
		Map<String, Object> claims = this.userSerivceImpl.getClaims(jwt);
		Map<String, Object> result = new HashMap<>();
		result.put("active", true);
		result.put("client_id", "library-client-id");
		result.put("username", claims.get("sub"));
		result.put("exp", claims.get("exp"));
		result.put("scope", authentication.getAuthorities());
		OAuth2IntrospectionAuthenticatedPrincipal principal = new OAuth2IntrospectionAuthenticatedPrincipal(result,
				(Collection<GrantedAuthority>) authentication.getAuthorities());
		return ResponseEntity.ok(principal);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping("/{userId}") // delete user
	public ResponseEntity<CustomBaseResponseDto> deleteUser(@PathVariable @Min(1) Long userId) {
		return this.userSerivceImpl.deleteUser(userId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal")
	@PutMapping("/{userId}") // update user
	public ResponseEntity<CustomBaseResponseDto> updateUser(@Valid @RequestBody UserRequestDto user,
			@PathVariable @Min(1) Long userId) {
		return this.userSerivceImpl.updateUser(userId, user);
	}

	@PostMapping // ad user and get token
	public ResponseEntity<TokenResponseDto> saveUser(@Valid @RequestBody UserRequestDto user) {
		this.userSerivceImpl.saveUser(user);
		TokenResponseDto res = new TokenResponseDto();
		return ResponseEntity.ok(res);
	}

	@PostMapping("/login") // get refresh token
	public ResponseEntity<TokenResponseDto> loginUser(@Valid @RequestBody UserLoginRequestDto user) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getPhoneNumber(), user.getPassword()));

		TokenResponseDto res = this.userSerivceImpl.generateToken(user.getPhoneNumber());
		this.userSerivceImpl.logUser(user.getPhoneNumber());
		return ResponseEntity.ok(res);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/details") // get details of token
	ResponseEntity<ClaimsResponseDto> tokenDetails(@NonNull HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		ClaimsResponseDto details = this.userSerivceImpl.getDetails(header);
		return ResponseEntity.ok(details);
	}

	@PostMapping("/refresh") // get refresh token
	public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody TokenRequestDto tokenDto) {
		TokenResponseDto res = this.userSerivceImpl.refreshToken(tokenDto);		
		return ResponseEntity.ok(res);

	}


}
