package com.training.library.controllers;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.request.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.services.GenreService;
import com.training.library.services.ReservationService;

@Controller
@RequestMapping("/api/v1/library/reservations")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;
	@PostMapping("/excel")
	public ResponseEntity<ResponseDto> saveReservations(MultipartFile file) throws IOException, CustomExceptionHandler, URISyntaxException{
		
		ResponseDto result = this.reservationService.saveReservations(file);
		
		return ResponseEntity.ok(result);
	}
}
