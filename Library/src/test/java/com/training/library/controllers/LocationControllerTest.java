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
import com.training.library.dto.request.LocationRequestDto;
import com.training.library.repositories.EntityGenerator;
import com.training.library.security.LibrarySecurityConfig;
import com.training.library.services.LocationService;

@WebMvcTest(LocationController.class)
@Import(LibrarySecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "1231231231", roles = { "ADMIN" })
class LocationControllerTest {

	@MockBean
	private LocationService locationService;
	@InjectMocks
	private LocationController locationController;
	@Autowired
	private MockMvc mockMvc;
	final ObjectMapper mapper = new ObjectMapper();

	private EntityGenerator entityGenerator = new EntityGenerator();

	@Test
	void findLocationTest1() throws Exception {
		when(locationService.findLocation(anyLong())).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/locations/location/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void findLocationTest2() throws Exception {
		 when(locationService.findLocation(anyLong())).thenReturn(entityGenerator.getLocationResponseDto(0L));
		 mockMvc.perform(get("/library/api/v1/locations/location/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.locationId").value("0")));
}

	@Test
	void findLocationsTest1() throws Exception {
		 when(locationService.findLocations(any(FilterDto.class))).thenReturn(entityGenerator.getLocationPage());
		 mockMvc.perform(get("/library/api/v1/locations").content(mapper.writeValueAsString(entityGenerator.getFilterDto()))
			.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
			.andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1")))
			.andReturn();
	}

	@Test
	void findLocationsTest2() throws Exception {
		when(locationService.findLocations(any(FilterDto.class))).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/locations").with(SecurityMockMvcRequestPostProcessors.opaqueToken()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void saveLocationTest1() throws JsonProcessingException, Exception {
		mockMvc.perform(
				post("/library/api/v1/locations").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(entityGenerator.getLocationRequestDto()))
						.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
								.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isBadRequest());
	}

	@Test
	void saveLocationTest2() throws JsonProcessingException, Exception {
		LocationRequestDto dto = entityGenerator.getLocationRequestDto();
		dto.setShelfId(100000L);
		mockMvc.perform(post("/library/api/v1/locations").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto))
				.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
						.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void updateLocationTest1() throws JsonProcessingException, Exception {
		LocationRequestDto dto = entityGenerator.getLocationRequestDto();
		dto.setShelfId(100000L);
		mockMvc.perform(put("/library/api/v1/locations/location/{id}", 0).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto))
				.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
						.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
		
	}

	@Test
	void deleteLocationTest1() throws Exception {
		mockMvc.perform(delete("/library/api/v1/locations/location/{id}", 0).with(SecurityMockMvcRequestPostProcessors
				.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk());
	}

	@Test
	void findLocationByFloorTest1() throws Exception {
		when(locationService.findLocationsByShelf(anyLong())).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/locations/shelfs/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void findLocationByFloorTest2() throws Exception {
		 when(locationService.findLocationsByShelf(anyLong())).thenReturn(List.of(entityGenerator.getLocationResponseDto(0L)));
		 mockMvc.perform(get("/library/api/v1/locations/shelfs/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$").exists()));

	}
}
