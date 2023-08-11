package com.training.library.exceptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.training.library.dto.response.ResponseDto;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
@PropertySource("classpath:message.properties")
public class GlobalExceptionHandler {
	@Autowired
	private Environment env;

	@ExceptionHandler(CustomExceptionHandler.class)
	public ResponseEntity<ResponseDto> sqlErrorHandler(CustomExceptionHandler e, Model model,
			HttpServletResponse response) throws IOException {
		ResponseDto res = new ResponseDto();
		res.setMessage(e.getMessage());
		return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ResponseDto> validationHandler(ConstraintViolationException e, Model model,
			HttpServletResponse response) throws IOException {
		ResponseDto res = new ResponseDto();
		Map<String, Object> result = new HashMap<>();
		e.getConstraintViolations().forEach(constrain -> {
			// result.put("Line number", e.getMessage());
			result.put(constrain.getPropertyPath().toString(), env.getRequiredProperty(constrain.getMessage()));
		});
		res.setMessage("Line number: " + e.getMessage() + "\n" + result);
		return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public final ResponseEntity<ResponseDto> dataIntegrityExceptionHandler(Exception e) {
		ResponseDto res = new ResponseDto();
		res.setMessage("Data already available!!");
		return new ResponseEntity<>(res, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(NullPointerException.class)
	public final ResponseEntity<ResponseDto> nullPointerExceptionHandler(Exception e) {
		ResponseDto res = new ResponseDto();
		res.setMessage("Please check your file structure!!");
		return new ResponseEntity<>(res, HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ResponseDto>  methodArgumentNotValidException(MethodArgumentNotValidException ex) {
	    BindingResult result = ex.getBindingResult();
	    final List<FieldError> fieldErrors = result.getFieldErrors();
	    HashMap<String, String> hashMap = new HashMap<>();
	    fieldErrors.forEach(fe->{
	    	hashMap.put(fe.getField(), env.getRequiredProperty(fe.getDefaultMessage()));
	    });
	    
	    ResponseDto res = new ResponseDto();
	    res.setResult(hashMap);
	    return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
	}
}
