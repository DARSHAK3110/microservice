package com.training.authentication.exception;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.training.authentication.dto.response.ValidationExceptionResponseDto;

@ControllerAdvice
@PropertySource("classpath:message.properties")
public class ValidatorHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private Environment env;

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		List<ValidationExceptionResponseDto> valid = new ArrayList<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {

			String field = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			ValidationExceptionResponseDto res = new ValidationExceptionResponseDto();
			res.setException(ex);
			res.setFieldName(field);
			res.setMessage(env.getProperty(message));
			valid.add(res);
		});
		return new ResponseEntity<>(valid, HttpStatus.BAD_REQUEST);
	}

}
