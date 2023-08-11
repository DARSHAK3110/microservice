package com.training.library.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.library.dto.request.FilterDto;
import com.training.library.repositories.EntityGenerator;
import com.training.library.security.LibrarySecurityConfig;
import com.training.library.services.FloorService;

@WebMvcTest(FloorController.class)
@Import(LibrarySecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "1231231231", roles = { "ADMIN" })
class FloorControllerTest {

	@MockBean
	private FloorService floorService;
	@Autowired
	private MockMvc mockMvc;
	final ObjectMapper mapper = new ObjectMapper();

	private EntityGenerator entityGenerator = new EntityGenerator();

	@Test
	void findFloorTest1() throws Exception {
		when(floorService.findFloor(anyLong())).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/floors/floor/{id}", 1).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void findFloorTest2() throws Exception {
		 when(floorService.findFloor(anyLong())).thenReturn(entityGenerator.getFloorResponseDto(0L));
		 mockMvc.perform(get("/library/api/v1/floors/floor/{id}", 1).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.floorId").value("0")));
}

	@Test
	void findFloorsTest1() throws Exception {
		 when(floorService.findFloors(any(FilterDto.class))).thenReturn(entityGenerator.getFloorPage());
		 mockMvc.perform(get("/library/api/v1/floors").content(mapper.writeValueAsString(entityGenerator.getFilterDto()))
			.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
			.andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1")))
			.andReturn();
	}

	@Test
	void findFloorsTest2() throws Exception {
		when(floorService.findFloors(any(FilterDto.class))).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/floors").with(SecurityMockMvcRequestPostProcessors.opaqueToken()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void saveFloorTest1() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/library/api/v1/floors").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(entityGenerator.getFloorRequestDto()))
				.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
						.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void updateFloorTest1() throws JsonProcessingException, Exception {
		mockMvc.perform(put("/library/api/v1/floors/floor/{id}", 0).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(entityGenerator.getFloorRequestDto()))
				.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
						.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void deleteFloorTest1() throws Exception {
		mockMvc.perform(delete("/library/api/v1/floors/floor/{id}", 0).with(SecurityMockMvcRequestPostProcessors
				.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk());
	}

}
