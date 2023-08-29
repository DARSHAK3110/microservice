package com.training.library.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.training.library.dto.request.BookStatusRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.repositories.EntityGenerator;
import com.training.library.security.LibrarySecurityConfig;
import com.training.library.services.BookStatusService;

@WebMvcTest(BookStatusController.class)
@Import(LibrarySecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@ExtendWith(MockitoExtension.class)
@WithMockUser(username = "1231231231", roles = { "ADMIN" })
class BookStatusControllerTest {

	@MockBean
	private BookStatusService bookStatusService;
	@Autowired
	private MockMvc mockMvc;
	final ObjectMapper mapper = new ObjectMapper();

	private EntityGenerator entityGenerator = new EntityGenerator();

	@Test
	void findBookStatusTest1() throws Exception {
		when(bookStatusService.findBookStatus(anyLong())).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/bookstatuses/bookstatus/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void findBookStatusTest2() throws Exception {
		 when(bookStatusService.findBookStatus(anyLong())).thenReturn(entityGenerator.getBookStatusResponseDto(0L));
		 mockMvc.perform(get("/library/api/v1/bookstatuses/bookstatus/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.locationId").value("0")));
}

	@Test
	void updateBookStatusTest() throws JsonProcessingException, Exception {
		BookStatusRequestDto dto = entityGenerator.getBookStatusRequestDto();
		dto.setLocationId(100000L);
		dto.setBookStatusId(1000L);
		mockMvc.perform(put("/library/api/v1/bookstatuses/bookstatus/{id}", 0).accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(dto))
				.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
						.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void testDeleteBookStatus() throws Exception {
		mockMvc.perform(
				delete("/library/api/v1/bookstatuses/bookstatus/{id}", 0).with(SecurityMockMvcRequestPostProcessors
						.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void testCheckBooksByLocation() throws Exception {
		mockMvc.perform(
				get("/library/api/v1/bookstatuses/locationcounter/{id}", 0).with(SecurityMockMvcRequestPostProcessors
						.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void testCheckBooksByShelf() throws Exception {
		mockMvc.perform(
				get("/library/api/v1/bookstatuses/shelfcounter/{id}", 0).with(SecurityMockMvcRequestPostProcessors
						.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void testCheckBooksBySection() throws Exception {
		mockMvc.perform(
				get("/library/api/v1/bookstatuses/sectioncounter/{id}", 0).with(SecurityMockMvcRequestPostProcessors
						.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void testCheckBooksByFloor() throws Exception {
		mockMvc.perform(
				get("/library/api/v1/bookstatuses/floorcounter/{id}", 0).with(SecurityMockMvcRequestPostProcessors
						.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
				.andExpect(status().isOk());
	}

	@Test
	void findAllBookStatusByLocationTest1() throws Exception {
		when(bookStatusService.findAllBooksByLocation(anyLong(), any(FilterDto.class))).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/bookstatuses/location/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void findAllBookStatusByLocationTest2() throws Exception {
		when(bookStatusService.findAllBooksByLocation(anyLong(), any(FilterDto.class))).thenReturn(entityGenerator.getBookStatusPage());
		 mockMvc.perform(get("/library/api/v1/bookstatuses/location/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
		 	.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1")));
}

	@Test
	void findAllBookStatusByShelfTest1() throws Exception {
		when(bookStatusService.findAllBooksByShelf(anyLong(), any(FilterDto.class))).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/bookstatuses/shelf/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void findAllBookStatusByShelfTest2() throws Exception {
		when(bookStatusService.findAllBooksByShelf(anyLong(), any(FilterDto.class))).thenReturn(entityGenerator.getBookStatusPage());
		 mockMvc.perform(get("/library/api/v1/bookstatuses/shelf/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
		 	.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1")));
}

	@Test
	void findAllBookStatusBySectionTest1() throws Exception {
		when(bookStatusService.findAllBooksBySection(anyLong(), any(FilterDto.class))).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/bookstatuses/section/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void findAllBookStatusBySectionTest2() throws Exception {
		when(bookStatusService.findAllBooksBySection(anyLong(), any(FilterDto.class))).thenReturn(entityGenerator.getBookStatusPage());
		 mockMvc.perform(get("/library/api/v1/bookstatuses/section/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
		 	.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1")));
}

	@Test
	void findAllBookStatusByFloorTest1() throws Exception {
		when(bookStatusService.findAllBooksBySection(anyLong(), any(FilterDto.class))).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/bookstatuses/floor/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

	@Test
	void findAllBookStatusByFloorTest2() throws Exception {
		when(bookStatusService.findAllBooksByFloor(anyLong(), any(FilterDto.class))).thenReturn(entityGenerator.getBookStatusPage());
		 mockMvc.perform(get("/library/api/v1/bookstatuses/floor/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
		 	.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1")));
}

	@Test
	void findBookStatusByISBNTest1() throws Exception {
		when(bookStatusService.findAllBookStatusByISBN(any(FilterDto.class), anyLong())).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/bookstatuses/isbn/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}
	
	
	

	@Test
	void findBookStatusByISBNTest2() throws Exception {
		when(bookStatusService.findAllBookStatusByISBN(any(FilterDto.class), anyLong())).thenReturn(entityGenerator.getBookStatusPage());
		 mockMvc.perform(get("/library/api/v1/bookstatuses/isbn/{id}", 0).with(SecurityMockMvcRequestPostProcessors
			.opaqueToken().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
		 	.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1")));
}
	@Test
	void findBookStatusByBookDetailsTest1() throws Exception {
		 when(bookStatusService.findAllBookStatusByBookDetailsId(any(FilterDto.class),any(Long.class))).thenReturn(entityGenerator.getBookStatusPage());
		 mockMvc.perform(get("/library/api/v1/bookstatuses/book/{id}",0).content(mapper.writeValueAsString(entityGenerator.getFilterDto()))
			.with(SecurityMockMvcRequestPostProcessors.opaqueToken()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
			.andExpect(status().isOk())
			.andExpect((MockMvcResultMatchers.jsonPath("$.numberOfElements").value("1")))
			.andReturn();
	}

	@Test
	void findBookStatusByBookDetailsTest2() throws Exception {
		when(bookStatusService.findAllBookStatusByBookDetailsId(any(FilterDto.class),any(Long.class))).thenReturn(null);
		mockMvc.perform(get("/library/api/v1/bookstatuses/book/{id}",0).with(SecurityMockMvcRequestPostProcessors.opaqueToken()
			.authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.jsonPath("$").doesNotExist());
	}

}
