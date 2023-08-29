package com.training.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.training.library.dto.request.FilterDto;
import com.training.library.dto.response.BookDetailsResponseDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.services.FavouriteService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@CrossOrigin
@RequestMapping("/library/api/v1/favourite")
public class FavouriteController {
	@Autowired
	private FavouriteService favouriteService;
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping
	public ResponseEntity<CustomBaseResponseDto> saveBookDetailsToFavourite(@RequestBody Long bookDetailsId,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return favouriteService.saveBookDetailsToFavourite(bookDetailsId, userName);
	}
	
	@GetMapping
	public ResponseEntity<Page<BookDetailsResponseDto>> findAllFavouriteItems(FilterDto dto,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Page<BookDetailsResponseDto> bookDetailsPage = favouriteService.findAllBookDetailsByUserId(dto,userName);
		return ResponseEntity.ok(bookDetailsPage);
	}
	

	@PreAuthorize("hasRole('ROLE_USER')")
	@DeleteMapping("/favouriteitem/{id}")
	public ResponseEntity<CustomBaseResponseDto> deleteBoofDetailsFromFavourite(@PathVariable("id") Long id) {
		return favouriteService.deleteBookDetailsFromFavourite(id);
	}
}
