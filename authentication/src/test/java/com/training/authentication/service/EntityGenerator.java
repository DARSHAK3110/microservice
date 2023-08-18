package com.training.authentication.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import com.training.authentication.dto.request.FilterDto;
import com.training.authentication.dto.request.TokenRequestDto;
import com.training.authentication.dto.request.UserLoginRequestDto;
import com.training.authentication.dto.request.UserRequestDto;
import com.training.authentication.dto.response.ClaimsResponseDto;
import com.training.authentication.dto.response.TokenResponseDto;
import com.training.authentication.dto.response.UserResponseDto;
import com.training.authentication.entity.User;
import com.training.authentication.entity.enums.Roles;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class EntityGenerator {
	public FilterDto getFilterDto() {
		FilterDto dto = new FilterDto();
		dto.setPageNumber(0);
		dto.setSetSize(2);
		dto.setFirstName("");
		dto.setLastName("");
		dto.setPhoneNumber("");
		return dto;
	}
	
	public Page<UserResponseDto> getUserPage() {

		List<UserResponseDto> authorList = new ArrayList<>();
		authorList.add(getUserResponseDto(1L));
		return PageableExecutionUtils.getPage(authorList, PageRequest.of(0, 2), () -> 1L);
	}
	
	public Page<UserResponseDto> getNullUserPage() {

		List<UserResponseDto> authorList = new ArrayList<>();
		return PageableExecutionUtils.getPage(authorList, PageRequest.of(0, 2), () -> 1L);
	}

	public UserResponseDto getUserResponseDto(long l) {
		UserResponseDto response = new UserResponseDto();
		response.setFirstName("fn");
		response.setLastName("ln");
		response.setPhoneNumber(1231231231L);
		response.setRole(Roles.ADMIN);
		response.setUserId(l);
		return response;
	}

	public UserRequestDto getUserRequestDto() {
		// TODO Auto-generated method stub
		UserRequestDto user = new UserRequestDto();
		
		user.setFirstName("user");
		user.setLastName("demo");
		user.setPassword("12312312");
		user.setPhoneNumber(1231231231L);
		user.setRole("ADMIN");
		return user;
	}

	public User getMockUser(Long id) {
		User user = new User();
		user.setUserId(id);
		user.setFirstName("user");
		user.setLastName("demo");
		user.setPassword("123123123");
		user.setPhoneNumber(1231231231L);
		user.setRole(Roles.ADMIN);
		return user;
	}
	
	public Claims getClaims() {
		String token = getToken();
		Claims claim =  Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode("12312312312311231231231231231231231231231231231123123123123123123123"))).build()
		.parseClaimsJws(token).getBody();
		return claim;
	}

	public String getToken() {
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("name", "1231231231");
		return Jwts.builder().setClaims(claims).setSubject("1231231231")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 60 * 10 * 1000))
				.signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("12312312312311231231231231231231231231231231231123123123123123123123")), SignatureAlgorithm.HS256).compact();
	}
	
	public String getExpiredToken() {
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("name", "1231231231");
		return Jwts.builder().setClaims(claims).setSubject("1231231231")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() - 60 * 10 * 1000))
				.signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("12312312312311231231231231231231231231231231231123123123123123123123")), SignatureAlgorithm.HS256).compact();
	}

	public TokenRequestDto getTokenRequestDto() {
		TokenRequestDto dto = new TokenRequestDto();
		dto.setRefreshToken(getToken());
		dto.setToken(getExpiredToken());
		return dto;
	}

	public TokenResponseDto getTokenResponseDto() {
		TokenResponseDto dto = new TokenResponseDto();
		dto.setRole("ADMIN");
		dto.setToken(getToken());
		dto.setRefreshToken(getToken());
		return dto;
	}

	public ClaimsResponseDto getClaimsDto() {
		ClaimsResponseDto dto = new ClaimsResponseDto();
		dto.setName("1231231231");
		return dto;
	}

	public Object getUserLoginRequestDto() {
		UserLoginRequestDto dto = new UserLoginRequestDto();
		dto.setPhoneNumber(1231231231L);
		dto.setPassword("12312312");
		return dto;
	}
}
