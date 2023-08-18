package com.training.authentication.controller;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.authentication.dto.request.FilterDto;
import com.training.authentication.dto.request.TokenRequestDto;
import com.training.authentication.dto.request.UserRequestDto;
import com.training.authentication.security.ApplicationSecurityConfig;
import com.training.authentication.service.EntityGenerator;
import com.training.authentication.service.UserService;

//@WebMvcTest(UserController.class)
@Import(ApplicationSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "1231231231", roles = { "ADMIN" })
@SpringBootTest
class UserControllerTest {

	@MockBean
	private UserService userService;
	@MockBean
	private UserDetailsService userDetailsService;
	@InjectMocks
	private UserController userController;
	@MockBean
	private AuthenticationManager authenticationManager;
	@Autowired
	private MockMvc mockMvc;
	final ObjectMapper mapper = new ObjectMapper();

	private EntityGenerator entityGenerator = new EntityGenerator();

	@Test
	void getAllUsersTest1() throws Exception {
		 when(userService.getAllUsers(any(FilterDto.class))).thenReturn(entityGenerator.getUserPage());
		 mockMvc.perform(get("/api/v1/users").content(mapper.writeValueAsString(entityGenerator.getFilterDto()))
			.with(SecurityMockMvcRequestPostProcessors.jwt()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
			.andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1")));
	}

	@Test
	void getAllUsersTest2() throws Exception {
		when(userService.getAllUsers(any(FilterDto.class))).thenReturn(entityGenerator.getNullUserPage());
		mockMvc.perform(get("/api/v1/users").content(mapper.writeValueAsString(entityGenerator.getFilterDto()))
				.with(SecurityMockMvcRequestPostProcessors.jwt()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
		.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("0")));
	}

	@Test
	void getUserTest1() throws Exception {
		when(userService.getUser(anyLong())).thenReturn(entityGenerator.getUserResponseDto(0L));
		 mockMvc.perform(get("/api/v1/users/{id}", 1).with(SecurityMockMvcRequestPostProcessors
			.jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.userId").value("0")));
	}

	@Test
	void  getUserTest2() throws Exception {
		when(userService.getUser(anyLong())).thenReturn(entityGenerator.getUserResponseDto(0L));
		 mockMvc.perform(get("/api/v1/users/{id}", 1).with(SecurityMockMvcRequestPostProcessors
			.jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.userId").value("0")));
}

	@Test
	void testCheckToken() {
		fail("Not yet implemented");
	}

	@Test
	void deleteUserTest() throws Exception {
		mockMvc.perform(delete("/api/v1/users/{id}", 1)
				.with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void updateUserTest1() throws JsonProcessingException, Exception {
		UserRequestDto dto = entityGenerator.getUserRequestDto();
		mockMvc.perform(
				put("/api/v1/users/{id}", 1).accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(dto)).with(SecurityMockMvcRequestPostProcessors.opaqueToken()
								.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void saveUserTest1() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/api/v1/users").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(entityGenerator.getUserRequestDto()))
				.with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void saveUserTest2() throws JsonProcessingException, Exception {
		UserRequestDto dto = entityGenerator.getUserRequestDto();
		dto.setPassword("12312");
		mockMvc.perform(post("/api/v1/users").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(dto))
				.with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isBadRequest())
				.andExpect((MockMvcResultMatchers.jsonPath("$").exists()));
	}

	@Test
	void loginUserTest() throws JsonProcessingException, Exception {
		when(userService.generateToken(any(Long.class))).thenReturn(entityGenerator.getTokenResponseDto());
		mockMvc.perform(post("/api/v1/users/login").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(entityGenerator.getUserLoginRequestDto()))
				.with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void tokenDetailsTest() throws Exception {
		
		 when(userService.getDetails(any(String.class))).thenReturn(entityGenerator.getClaimsDto());
		 mockMvc.perform(get("/api/v1/users/details").accept(MediaType.APPLICATION_JSON).header("Authorization", entityGenerator.getToken())
					.with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
					.andExpect(status().isOk())
					.andExpect((MockMvcResultMatchers.jsonPath("$.name").value("1231231231" )));
	}

	@Test
	void refreshTokenTest() throws JsonProcessingException, Exception {
		 when(userService.refreshToken(any(TokenRequestDto.class))).thenReturn(entityGenerator.getTokenResponseDto());
			mockMvc.perform(post("/api/v1/users/refresh").accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(entityGenerator.getTokenRequestDto()))
					.with(SecurityMockMvcRequestPostProcessors.jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
					.andExpect(status().isOk())
					.andExpect((MockMvcResultMatchers.jsonPath("$.refreshToken").exists( )));
		
	}
}
