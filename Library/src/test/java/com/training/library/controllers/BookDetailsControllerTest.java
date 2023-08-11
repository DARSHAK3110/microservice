package com.training.library.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import com.training.library.dto.request.BookDetailsRequestDto;
import com.training.library.repositories.EntityGenerator;
import com.training.library.security.LibrarySecurityConfig;
import com.training.library.services.BookDetailsService;
@WebMvcTest(BookDetailsController.class)
@Import(LibrarySecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "1231231231", roles = { "ADMIN" })
class BookDetailsControllerTest {

	@MockBean
	private BookDetailsService bookDetailsService;
	@InjectMocks
	private BookDetailsController bookDetailsController;
	@Autowired
	private MockMvc mockMvc;
	final ObjectMapper mapper = new ObjectMapper();

	private EntityGenerator entityGenerator = new EntityGenerator();

	@Test
	void findBookDetailsTest1() throws Exception {
		when(bookDetailsService.findBookDetails(anyLong())).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/bookdetails/book/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void findBookDetailsTest2() throws Exception {
		 when(bookDetailsService.findBookDetails(anyLong())).thenReturn(entityGenerator.getBookDetailsResponseDto(0L));
		 mockMvc.perform(get("/library/api/v1/bookdetails/book/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.bookDetailsId").value("0")));
}

//	@Test
//	void findAllBookDetailsTest1() throws Exception {
//		 when(bookDetailsService.findAllBookDetails(any(FilterDto.class))).thenReturn(entityGenerator.getBookDetailsPage());
//		 mockMvc.perform(get("/library/api/v1/bookdetails").content(mapper.writeValueAsString(entityGenerator.getFilterDto()))
//			.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
//			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
//			.andExpect(status().isOk())
//			.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1")))
//			.andReturn();
//	}

//	@Test
//	void findAllBookDetailsTest2() throws Exception {
//		when(bookDetailsService.findAllBookDetails(any(FilterDto.class))).thenReturn(null);
//		mockMvc.perform(get("/library/api/v1/bookdetails").with(SecurityMockMvcRequestPostProcessors.opaqueToken()
//			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
//			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
//	}

	@Test
	void saveBookDetailsTest1() throws JsonProcessingException, Exception {
		mockMvc.perform(post("/library/api/v1/bookdetails").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(entityGenerator.getBookDetailsRequestDto()))
				.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
						.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isBadRequest()).andExpect(MockMvcResultMatchers.jsonPath("$.result").exists());
	}

	@Test
	void saveBookDetailsTest2() throws JsonProcessingException, Exception {
		BookDetailsRequestDto dto = entityGenerator.getBookDetailsRequestDto();
		dto.setAuthorId(100000L);
		mockMvc.perform(post("/library/api/v1/bookdetails").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto))
				.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
						.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void updateBookDetailsTest1() throws JsonProcessingException, Exception {
		mockMvc.perform(put("/library/api/v1/bookdetails/book/{id}", 0).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(entityGenerator.getFloorRequestDto()))
				.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
						.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void deleteBookDetailsTest1() throws Exception {
		mockMvc.perform(delete("/library/api/v1/bookdetails/book/{id}", 0).with(SecurityMockMvcRequestPostProcessors
				.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk());
	}

//	@Test
//	void saveBooksTest1() throws Exception {
//		mockMvc.perform(post("/library/api/v1/bookdetails/excel/{isbn}", 0).with(SecurityMockMvcRequestPostProcessors
//				.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk());
//	}


}
