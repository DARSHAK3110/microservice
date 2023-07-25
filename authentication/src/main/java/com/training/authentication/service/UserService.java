package com.training.authentication.service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.training.authentication.dto.request.FilterDto;
import com.training.authentication.dto.request.TokenRequestDto;
import com.training.authentication.dto.request.UserRequestDto;
import com.training.authentication.dto.response.ClaimsResponseDto;
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

@Service
public class UserService {

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
		this.userRepository.save(user);
		Map<String, Object> map = new HashedMap<>();
		map.put("name", user.getFirstName() + " " + user.getLastName());
		map.put("role", user.getRole().name());
		return jwtService.generateToken(new CustomUserDetail(user), map);
	}

	@Transactional
	public void deleteUser(Long userId) {
		this.userRepository.deleteUser(userId);
	}

	public String updateUser(Long userId, UserRequestDto user) {
		Optional<User> userOptional = this.userRepository.findByPhoneNumberAndDeletedAtIsNull(userId);
		User savedUser = null;
		if (userOptional.isPresent()) {
			savedUser = userOptional.get();
			savedUser.setFirstName(user.getFirstName());
			savedUser.setLastName(user.getLastName());
			savedUser.setUpdatedAt(new Date());
			this.userRepository.save(savedUser);
			Map<String, Object> map = new HashedMap<>();
			map.put("name", user.getFirstName() + " " + user.getLastName());
			map.put("role", user.getRole());
			return jwtService.generateToken(new CustomUserDetail(savedUser), map);
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
			return res;
		}
		return null;
	}

	public ClaimsResponseDto getDetails(String token) {
		token = token.replace("Bearer ", "");
		Claims extractAllClaims = this.jwtService.extractAllClaims(token, "access");
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
			this.jwtService.extractAllClaims(tokenDto.getToken(), "access");
		} catch (ExpiredJwtException e) {
			Claims claims = e.getClaims();
			subject = claims.getSubject();
		}
		
			Claims refreshClaim = this.jwtService.extractAllClaims(tokenDto.getRefreshToken(), "refresh");
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
}
