package com.training.library.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookDetailsResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.entity.Favourite;
import com.training.library.entity.User;
import com.training.library.repositories.FavouriteRepository;

import jakarta.transaction.Transactional;

@Service
@PropertySource("classpath:message.properties")
public class FavouriteService {
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	@Autowired
	private FavouriteRepository favouriteRepository;
	@Autowired
	private BookDetailsService bookDetailsService;
	@Autowired
	private UserService userService;
	@Autowired
	private BookReservationService reserveService;
	
	public ResponseEntity<CustomBaseResponseDto> saveBookDetailsToCart(Long bookDetailsId, String userName) {
		Favourite favourite= new Favourite();
		favourite.setBookDetails(bookDetailsService.findBookDetailsById(bookDetailsId));
		User user = userService.findByPhone(Long.parseLong(userName));
		if (user == null) {
			user = userService.newUser(userName);
		}
		favourite.setUser(user);
		favouriteRepository.save(favourite);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public boolean checkCart(Long bookDetailsId, String userName) {
		Optional<Favourite> cartItem = favouriteRepository.findByBookDetails_BookDetailsIdAndUser_PhoneAndDeletedAtIsNull(bookDetailsId,Long.parseLong(userName));
		if(cartItem.isPresent()) {
			return true;
		}
		return false;
	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteBookDetailsFromCart(Long id) {
		favouriteRepository.deleteByBookDetails_BookDetailsId(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public Page<BookDetailsResponseDto> findAllBookDetailsByUserId(FilterDto dto, String userName) {
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		Page<BookDetailsResponseDto> result = favouriteRepository
				.findAllByUserId(
						dto.getSearch(), dto.getSearch(), pageable,Long.parseLong(userName));
			List<BookDetailsResponseDto> content = result.getContent();
			for (BookDetailsResponseDto response : content) {
				if (isReserved(response.getBookDetailsId(), userName)) {
					response.setReserved(true);
				} else {
					response.setReserved(false);
				}
					
			}
			return PageableExecutionUtils.getPage(content, pageable, () -> result.getTotalElements());

	}
	private boolean isReserved(Long bookDetailsId, String userName) {
		return reserveService.checkReservation(bookDetailsId, userName);
	}
}
