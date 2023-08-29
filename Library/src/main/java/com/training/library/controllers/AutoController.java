package com.training.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.training.library.dto.request.LocationRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.services.AutoService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@CrossOrigin
@RequestMapping("/library/api/v1/auto")
public class AutoController {
	@Autowired
	private AutoService autoService;
	@PutMapping("/{entity}")
	public ResponseEntity<CustomBaseResponseDto> autoDelete(@PathVariable("entity") String entity,@Valid @RequestBody Object dto,
			HttpServletRequest req) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		return autoService.autoDeleteMessage(dto, userName,entity);
	}
}
