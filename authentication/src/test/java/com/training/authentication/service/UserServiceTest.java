package com.training.authentication.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.training.authentication.dto.request.FilterDto;
import com.training.authentication.dto.request.TokenRequestDto;
import com.training.authentication.dto.request.UserRequestDto;
import com.training.authentication.dto.response.ClaimsResponseDto;
import com.training.authentication.dto.response.CustomBaseResponseDto;
import com.training.authentication.dto.response.TokenResponseDto;
import com.training.authentication.dto.response.UserResponseDto;
import com.training.authentication.entity.Log;
import com.training.authentication.entity.User;
import com.training.authentication.repository.LogRepository;
import com.training.authentication.repository.UserRepository;
import com.training.authentication.repository.specifications.UserSpecifications;
import com.training.authentication.security.CustomUserDetail;
import com.training.authentication.security.JwtService;

import io.jsonwebtoken.ExpiredJwtException;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class UserServiceTest {

	@Mock
	private UserRepository repo;
	@InjectMocks
	private UserService service;
	@Mock
	private UserSpecifications userSpecifications;
	@Mock
	private JwtService jwtService;
	
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private LogRepository logRepository;
@Autowired
private EntityGenerator entityGenerator;
	
	@Test
	void getAllUsersTest() {
		FilterDto dto = entityGenerator.getFilterDto();
		Page<UserResponseDto> actual = entityGenerator.getUserPage();
		when(userSpecifications.searchSpecification(dto)).thenReturn(actual);
		Page<UserResponseDto> result = service.getAllUsers(dto);
		assertThat(result).isEqualTo(actual);
	}

	
	@Test
	void getUserTest() {
		when(repo.findUser(1L)).thenReturn(Optional.of(entityGenerator.getUserResponseDto(1L)));
		UserResponseDto result = service.getUser(1L);
		assertThat(result.getUserId()).isEqualTo(1L);
	}

	@Test
	void getUserTest2() {
		when(repo.findUser(1L)).thenReturn(Optional.empty());
		UserResponseDto result = service.getUser(1L);
		assertThat(result).isNull();
	}

	@Test
	void saveAuthorTest() {
		UserRequestDto req = entityGenerator.getUserRequestDto();
		User user = entityGenerator.getMockUser(0L);
		when(repo.save(any(User.class))).thenReturn(user);
		when(passwordEncoder.encode(any(String.class))).thenReturn("ZXcv!@34");
		when(jwtService.generateToken(any(CustomUserDetail.class), any(Map.class))).thenReturn("Valid Token");
		String result = service.saveUser(req);
		assertThat(result).isEqualTo("Valid Token");
	}
	
	@Test
	void deleteUserTest() {
		service.deleteUser(0L,"abcd");
		verify(repo,times(1)).deleteUser(any(Long.class));
	}

//	@Test
//	void UpdateUserTest1() {
//		when(repo.findByPhoneNumberAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.empty());
//		ResponseEntity<CustomBaseResponseDto> updateUser = service.updateUser(0L, entityGenerator.getUserRequestDto());
//		assertThat(updateUser).isNull();
//	}
//	
//	@Test
//	void UpdateUserTest2() {
//		when(repo.findByPhoneNumberAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockUser(0L)));
//		when(jwtService.generateToken(any(CustomUserDetail.class), any(Map.class))).thenReturn("Valid Token");
//		String updateUser = service.updateUser(0L, entityGenerator.getUserRequestDto());
//		assertThat(updateUser).isEqualTo("Valid Token");
//		
//	}

	@Test
	void generateTokenTest1() {
		when(repo.findByPhoneNumberAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockUser(0L)));
		when(jwtService.generateRefreshToken(1231231231L)).thenReturn("Refresh Token");
		when(jwtService.generateToken(any(CustomUserDetail.class), any(Map.class))).thenReturn("Valid Token");
		TokenResponseDto result = service.generateToken(1231231231L);
		assertThat(result.getRefreshToken()).isEqualTo("Refresh Token");
	}
	
	@Test
	void generateTokenTest2() {
		when(repo.findByPhoneNumberAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.empty());
		TokenResponseDto result = service.generateToken(1231231231L);
		assertThat(result).isNull();
	}

	@Test
	void testGetDetails() {
		when(jwtService.extractAllClaims(any(String.class), any(String.class))).thenReturn(entityGenerator.getClaims());
		ClaimsResponseDto details = service.getDetails(entityGenerator.getToken());
		assertThat(details.getName()).isEqualTo("1231231231");
	}

	@Test
	void refreshTokenTest1() {
		TokenRequestDto tokenRequestDto = entityGenerator.getTokenRequestDto();
		doThrow(new ExpiredJwtException(null, entityGenerator.getClaims(), null)).when(jwtService).extractAllClaims(tokenRequestDto.getToken(), "access");
		when(repo.findByPhoneNumberAndDeletedAtIsNull(any(Long.class))).thenReturn(Optional.of(entityGenerator.getMockUser(0L)));
		when(jwtService.generateRefreshToken(1231231231L)).thenReturn("Refresh Token");
		when(jwtService.generateToken(any(CustomUserDetail.class), any(Map.class))).thenReturn("Valid Token");
		when(jwtService.extractAllClaims(entityGenerator.getToken(), "refresh")).thenReturn(entityGenerator.getClaims());
		TokenResponseDto result = service.refreshToken(entityGenerator.getTokenRequestDto());
		assertThat(result).isNotNull();
	}
	
	@Test
	void refreshTokenTest2() {
		when(jwtService.extractAllClaims(any(String.class), any(String.class))).thenReturn(entityGenerator.getClaims());
		TokenResponseDto result = service.refreshToken(entityGenerator.getTokenRequestDto());
		assertThat(result).isNull();
	}
	
	@Test
	void logUserTest() {
		when(repo.findByPhoneNumberAndDeletedAtIsNull(1231231231L)).thenReturn(Optional.of(entityGenerator.getMockUser(1L)));
		service.logUser(1231231231L);
		verify(logRepository,times(1)).save(any(Log.class));
	}

	@Test
	void logUserTest2() {
		service.logUser(1231231231L);
		verify(logRepository,times(1)).save(any(Log.class));
	}

	@Test
	void getClaimsTest() {
		when(jwtService.extractAllClaims(any(String.class), any(String.class))).thenReturn(entityGenerator.getClaims());
		Map<String, Object> result = service.getClaims(entityGenerator.getToken());
		assertThat(result).isNotNull();
	}

}
