package com.training.library.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.training.library.dto.request.BookReservationRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.repositories.EntityGenerator;
import com.training.library.security.LibrarySecurityConfig;
import com.training.library.services.BookReservationService;

@WebMvcTest(BookReservationController.class)
@Import(LibrarySecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "1231231231", roles = { "ADMIN" })
class BookReservationControllerTest {

	@MockBean
	private BookReservationService bookReservationService;
	@InjectMocks
	private BookReservationController bookReservationController;
	@Autowired
	private MockMvc mockMvc;
	final ObjectMapper mapper = new ObjectMapper();

	private EntityGenerator entityGenerator = new EntityGenerator();

	@Test
	void findBookReservationTest1() throws Exception {
		when(bookReservationService.findBookReservation(anyLong())).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/reservations/reservation/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void findBookReservationTest2() throws Exception {
		 when(bookReservationService.findBookReservation(anyLong())).thenReturn(entityGenerator.getBookReservationResponseDto(0L));
		 mockMvc.perform(get("/library/api/v1/reservations/reservation/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.bookReservationId").value("0")));
}

	@Test
	void findBookReservationsTest1() throws Exception {
		 when(bookReservationService.findAllBookReservation(any(FilterDto.class), any(String.class))).thenReturn(entityGenerator.getBookReservationPage());
		 mockMvc.perform(get("/library/api/v1/reservations").content(mapper.writeValueAsString(entityGenerator.getFilterDto()))
			.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
			.andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1")))
			;
	}

	@Test
	void findBookReservationsTest2() throws Exception {
		when(bookReservationService.findAllBookReservation(any(FilterDto.class), any(String.class))).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/reservations").with(SecurityMockMvcRequestPostProcessors.opaqueToken()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void saveBookReservationTest1() throws JsonProcessingException, Exception {
		mockMvc.perform(
				post("/library/api/v1/reservations").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(entityGenerator.getBookReservationRequestDto()))
						.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
								.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isBadRequest());
	}

	@Test
	void saveBookReservationTest2() throws JsonProcessingException, Exception {
		BookReservationRequestDto dto = entityGenerator.getBookReservationRequestDto();
		dto.setBookDetailsId(1L);
		mockMvc.perform(post("/library/api/v1/reservations").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto))
				.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
						.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void deleteBookReservationTest1() throws Exception {
		mockMvc.perform(
				delete("/library/api/v1/reservations/reservation/{id}", 0).with(SecurityMockMvcRequestPostProcessors
						.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}
	
	@Test
	void saveBookReservationStatus() throws Exception {
		mockMvc.perform(
				post("/library/api/v1/reservations/status/{id}", 0).with(SecurityMockMvcRequestPostProcessors
						.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))		
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(true))
)
		
				.andExpect(status().isOk());
	}
}
