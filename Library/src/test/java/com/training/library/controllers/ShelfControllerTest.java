package com.training.library.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
import com.training.library.dto.request.ShelfRequestDto;
import com.training.library.repositories.EntityGenerator;
import com.training.library.security.LibrarySecurityConfig;
import com.training.library.services.ShelfService;

@WebMvcTest(ShelfController.class)
@Import(LibrarySecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "1231231231", roles = { "ADMIN" })
class ShelfControllerTest {

	@MockBean
	private ShelfService shelfService;
	@InjectMocks
	private ShelfController shelfController;
	@Autowired
	private MockMvc mockMvc;
	final ObjectMapper mapper = new ObjectMapper();

	private EntityGenerator entityGenerator = new EntityGenerator();

	@Test
	void findShelfTest1() throws Exception {
		when(shelfService.findShelf(anyLong())).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/shelfs/shelf/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void findSectionTest2() throws Exception {
		 when(shelfService.findShelf(anyLong())).thenReturn(entityGenerator.getShelfResponseDto(0L));
		 mockMvc.perform(get("/library/api/v1/shelfs/shelf/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.shelfId").value("0")));
}

	@Test
	void findShelfsTest1() throws Exception {
		 when(shelfService.findShelfs(any(FilterDto.class))).thenReturn(entityGenerator.getShelfPage());
		 mockMvc.perform(get("/library/api/v1/shelfs").content(mapper.writeValueAsString(entityGenerator.getFilterDto()))
			.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
			.andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1")))
			.andReturn();
	}

	@Test
	void findShelfsTest2() throws Exception {
		when(shelfService.findShelfs(any(FilterDto.class))).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/shelfs").with(SecurityMockMvcRequestPostProcessors.opaqueToken()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void saveShelfTest1() throws JsonProcessingException, Exception {
		ShelfRequestDto dto = entityGenerator.getShelfRequestDto();
		dto.setSectionId(100000L);
		dto.setShelfNo(1000000L);
		mockMvc.perform(post("/library/api/v1/shelfs").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto))
				.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
						.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());

	}

	@Test
	void saveShelfTest2() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/library/api/v1/shelfs").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(entityGenerator.getShelfRequestDto()))
				.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
						.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isBadRequest()).andExpect(MockMvcResultMatchers.jsonPath("$.result").exists());
	}

	@Test
	void updateShelfTest1() throws JsonProcessingException, Exception {
		ShelfRequestDto dto = entityGenerator.getShelfRequestDto();
		dto.setSectionId(100000L);
		dto.setShelfId(1000L);
		dto.setShelfNo(1000000L);
		mockMvc.perform(put("/library/api/v1/shelfs/shelf/{id}", 0).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto))
				.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
						.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void deleteShelfTest1() throws Exception {
		mockMvc.perform(delete("/library/api/v1/shelfs/shelf/{id}", 0).with(SecurityMockMvcRequestPostProcessors
				.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk());
	}

	@Test
	void findShelfBySectionIdTest1() throws Exception {
		when(shelfService.findShelfsBySection(anyLong())).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/shelfs/sections/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void findSectionBySectionIdTest2() throws Exception {
		 when(shelfService.findShelfsBySection(anyLong())).thenReturn(List.of(entityGenerator.getShelfResponseDto(0L)));
		 mockMvc.perform(get("/library/api/v1/shelfs/sections/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$").exists()));
	}
}
