package com.training.authentication.exception;


import java.util.NoSuchElementException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.training.authentication.dto.response.ExceptionResponseDto;

@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
	public final ResponseEntity<ExceptionResponseDto> expiredJwtExceptionHandler(Exception e) {
		
		ExceptionResponseDto res = new ExceptionResponseDto("Your token is expired login again!!",e);
		return new ResponseEntity<>(res,HttpStatus.UNAUTHORIZED);
	}
	
	@ExceptionHandler({NullPointerException.class,NoSuchElementException.class})
	public final ResponseEntity<ExceptionResponseDto> noSuchElementExceptionHandler(Exception e) {
		e.printStackTrace();
		ExceptionResponseDto res = new ExceptionResponseDto("Data not found!!",e);
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public final ResponseEntity<ExceptionResponseDto> dataIntegrityExceptionHandler(Exception e) {
		
		ExceptionResponseDto res = new ExceptionResponseDto("Data already available!!",e);
		return new ResponseEntity<>(res,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public final ResponseEntity<ExceptionResponseDto> userNotFoundExceptionHandler(Exception e) {
		
		ExceptionResponseDto res = new ExceptionResponseDto("User not found!!",e);
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
	
}
