package com.training.library.exceptions;

import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.training.library.dto.request.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomExceptionHandler.class)
	public ResponseEntity<ResponseDto> SQLErrorHandler(CustomExceptionHandler e,Model model, HttpServletResponse response) throws IOException{
		ResponseDto res = new ResponseDto();
		res.setMessage(e.getMessage());
		res.setFileUrl(e.getFile());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
	}
}
