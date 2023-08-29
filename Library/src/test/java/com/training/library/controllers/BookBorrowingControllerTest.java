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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.library.dto.request.BookBorrowingRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.repositories.EntityGenerator;
import com.training.library.security.LibrarySecurityConfig;
import com.training.library.services.BookBorrowingService;

@WebMvcTest(BookBorrowingController.class)
@Import(LibrarySecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "1231231231", roles = { "ADMIN" })
class BookBorrowingControllerTest {

	@MockBean
	private BookBorrowingService bookBorrowingService;
	@InjectMocks
	private BookBorrowingController bookBorrowingController;
	@Autowired
	private MockMvc mockMvc;
	final ObjectMapper mapper = new ObjectMapper();

	private EntityGenerator entityGenerator = new EntityGenerator();

	@Test
	void findBookBorrowingTest1() throws Exception {
		when(bookBorrowingService.findBookBorrowing(anyLong())).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/borrowings/bookborrowing/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void findBookBorrowingTest2() throws Exception {
		 when(bookBorrowingService.findBookBorrowing(anyLong())).thenReturn(entityGenerator.getBookBorrowingResponseDto(0L));
		 mockMvc.perform(get("/library/api/v1/borrowings/bookborrowing/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.bookBorrowingId").value("0")));
}

	@Test
	void findBookBorrowingsTest1() throws Exception {
		 when(bookBorrowingService.findAllBookBorrowing(any(FilterDto.class),any(String.class))).thenReturn(entityGenerator.getBookBorrowingPage());
		 mockMvc.perform(get("/library/api/v1/borrowings").content(mapper.writeValueAsString(entityGenerator.getFilterDto()))
			.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
			.andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1")));
			
	}

	@Test
	void findBookBorrowingsTest2() throws Exception {
		when(bookBorrowingService.findAllBookBorrowing(any(FilterDto.class),any(String.class))).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/borrowings").with(SecurityMockMvcRequestPostProcessors.opaqueToken()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void saveBookBorrowingTest1() throws JsonProcessingException, Exception {
		MockHttpServletResponse response = mockMvc
				.perform(post("/library/api/v1/borrowings").accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON)
						.content(mapper.writeValueAsString(entityGenerator.getBookBorrowingRequestDto()))
						.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
								.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isBadRequest()).andReturn().getResponse();
		System.out.println(response.getContentAsString());
	}

	@Test
	void saveBookBorrowingTest2() throws JsonProcessingException, Exception {
		BookBorrowingRequestDto dto = entityGenerator.getBookBorrowingRequestDto();
		dto.setBookStatusId(1000L);
		mockMvc.perform(post("/library/api/v1/borrowings").accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto))
				.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
						.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void deleteBookBorrowingTest1() throws Exception {
		mockMvc.perform(
				delete("/library/api/v1/borrowings/bookborrowing/{id}", 0).with(SecurityMockMvcRequestPostProcessors
						.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
		void findBookBorrowingByBookStatusTest1() throws Exception {
			when(bookBorrowingService.findBookBorrowing(anyLong())).thenReturn(null);
			mockMvc.perform(get("/library/api/v1/borrowings/bookstatus/{id}", 0).with(SecurityMockMvcRequestPostProcessors
				.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
		}

	@Test
	void countBookBorrowingByUserTest1() throws Exception {
		mockMvc.perform(
				get("/library/api/v1/borrowings/bookcounter/{id}", 1L).with(SecurityMockMvcRequestPostProcessors
						.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$").isBoolean());
	}
}
