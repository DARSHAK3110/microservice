package com.training.authentication.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.training.authentication.dto.request.FilterDto;
import com.training.authentication.dto.request.TokenRequestDto;
import com.training.authentication.dto.request.UserRequestDto;
import com.training.authentication.dto.response.ClaimsResponseDto;
import com.training.authentication.dto.response.CustomBaseResponseDto;
import com.training.authentication.dto.response.TokenResponseDto;
import com.training.authentication.dto.response.UserResponseDto;
import com.training.authentication.entity.Log;
import com.training.authentication.entity.User;
import com.training.authentication.entity.enums.Roles;
import com.training.authentication.repository.LogRepository;
import com.training.authentication.repository.UserRepository;
import com.training.authentication.repository.specifications.UserSpecifications;
import com.training.authentication.security.CustomUserDetail;
import com.training.authentication.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.constraints.Min;

@Service
public class UserService {

	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private JwtService jwtService;
	@Autowired
	private UserSpecifications userSpecifications;
	@Autowired
	private LogRepository logRepository;
	public String ACCESS_STRING = "access";
	private String REFRESH_STRING = "refresh";

	public Page<UserResponseDto> getAllUsers(FilterDto searchWord) {
		return userSpecifications.searchSpecification(searchWord);
	}

	public UserResponseDto getUser(Long userId) {
		Optional<UserResponseDto> user = this.userRepository.findUser(userId);
		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	public String saveUser(UserRequestDto userDto) {
		User user = new User();
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setPassword(passwordEncoder.encode(userDto.getPassword()));
		user.setPhoneNumber(userDto.getPhoneNumber());
		user.setRole(Roles.valueOf(userDto.getRole()));
		user.setEmail(userDto.getEmail());
		Optional<User> userByEmail = userRepository.findByEmail(user.getEmail());
		if(userByEmail.isPresent()) {
			throw new RuntimeException("This email already used!!");
		}
		this.userRepository.save(user);
		Map<String, Object> map = new HashedMap<>();
		map.put("name", user.getFirstName() + " " + user.getLastName());
		map.put("role", user.getRole().name());
		return jwtService.generateToken(new CustomUserDetail(user), map);
	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteUser(Long userId, String token) {
		UserResponseDto user = getUser(userId);
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token.replace("Bearer ", ""));
		HttpEntity<String> entity = new HttpEntity<>("body", headers);

		ResponseEntity<Boolean> isUserBorrowedBookEntity = restTemplate.exchange(
				"http://localhost:8091/library/api/v1/borrowings/checkuser/" + user.getPhoneNumber(), HttpMethod.GET,
				entity, Boolean.class);
		if (Boolean.TRUE.equals(isUserBorrowedBookEntity.getBody())) {
			throw new RuntimeException("Already user borrowed book, so you can't delete them now");
		} else {
			this.userRepository.deleteUser(userId);
			return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
		}
	}

	public ResponseEntity<CustomBaseResponseDto> updateUser(Long userId, UserRequestDto user) {
		Optional<User> userOptional = this.userRepository.findByPhoneNumberAndDeletedAtIsNull(userId);
		User savedUser = null;
		if (userOptional.isPresent()) {
			savedUser = userOptional.get();
			savedUser.setFirstName(user.getFirstName());
			savedUser.setLastName(user.getLastName());
			savedUser.setUpdatedAt(new Date());
			this.userRepository.save(savedUser);
			return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
		}
		return null;
	}

	public TokenResponseDto generateToken(Long phoneNumer) {
		Optional<User> userOptional = this.userRepository.findByPhoneNumberAndDeletedAtIsNull(phoneNumer);
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			Map<String, Object> map = new HashedMap<>();
			map.put("name", user.getFirstName() + " " + user.getLastName());
			map.put("role", user.getRole().name());
			TokenResponseDto res = new TokenResponseDto();
			res.setRole(user.getRole().name());
			res.setUserId(user.getPhoneNumber());
			res.setRefreshToken(jwtService.generateRefreshToken(phoneNumer));
			res.setToken(jwtService.generateToken(new CustomUserDetail(user), map));
			res.setMessage(env.getRequiredProperty(OPERATION_SUCCESS));
			return res;
		}
		return null;
	}

	public ClaimsResponseDto getDetails(String token) {
		token = token.replace("Bearer ", "");
		Claims extractAllClaims = this.jwtService.extractAllClaims(token, ACCESS_STRING);
		ClaimsResponseDto res = new ClaimsResponseDto();
		res.setSubject(extractAllClaims.getSubject());
		res.setIssuedDate(extractAllClaims.getIssuedAt());
		res.setExpireDate(extractAllClaims.getExpiration());
		res.setName((String) extractAllClaims.get("name"));
		res.setRole((String) extractAllClaims.get("role"));
		return res;
	}

	public TokenResponseDto refreshToken(TokenRequestDto tokenDto) {
		String subject = null;
		try {
			this.jwtService.extractAllClaims(tokenDto.getToken(), ACCESS_STRING);
		} catch (ExpiredJwtException e) {
			Claims claims = e.getClaims();
			subject = claims.getSubject();
		}

		Claims refreshClaim = this.jwtService.extractAllClaims(tokenDto.getRefreshToken(), REFRESH_STRING);
		if (refreshClaim.getSubject().equals(subject)) {
			return generateToken(Long.parseLong(subject));
		}
		return null;
	}

	public void logUser(Long phoneNumber) {
		Optional<User> user = userRepository.findByPhoneNumberAndDeletedAtIsNull(phoneNumber);
		Log log = new Log();
		if (user.isPresent()) {
			log.setUser(user.get());
		}
		logRepository.save(log);

	}

	public Map<String, Object> getClaims(String jwt) {
		Claims extractAllClaims = jwtService.extractAllClaims(jwt, ACCESS_STRING);
		return new HashMap<>(extractAllClaims);
	}

	public String getEmailByPhone(@Min(1) Long phone) {
		Optional<User> userOptional = userRepository.findByPhoneNumberAndDeletedAtIsNull(phone);
		if (userOptional.isPresent()) {
			return userOptional.get().getEmail();
		}
		return null;
	}
}
