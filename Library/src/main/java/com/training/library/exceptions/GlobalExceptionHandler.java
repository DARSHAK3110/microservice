package com.training.library.exceptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.training.library.dto.request.ResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
@PropertySource("classpath:message.properties")
public class GlobalExceptionHandler {
	@Autowired
	private Environment env;

	@ExceptionHandler(CustomExceptionHandler.class)
	public ResponseEntity<ResponseDto> SQLErrorHandler(CustomExceptionHandler e,Model model, HttpServletResponse response) throws IOException{
		ResponseDto res = new ResponseDto();
		res.setMessage(e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ResponseDto> validationHandler(ConstraintViolationException e,Model model, HttpServletResponse response) throws IOException{
		ResponseDto res = new ResponseDto();
		res.setMessage(e.getMessage());
		Map<String, Object> result = new HashMap<>();
		e.getConstraintViolations().forEach(constrain->{
			result.put("Line number:", e.getMessage());
			result.put(constrain.getPropertyPath().toString(), env.getRequiredProperty(constrain.getMessage()));
		});
		res.setResult(result);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(res);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public final ResponseEntity<ResponseDto> dataIntegrityExceptionHandler(Exception e) {
		ResponseDto res = new ResponseDto();
		res.setMessage("Data already available!!");
		return new ResponseEntity<>(res,HttpStatus.CONFLICT);
	}
}
