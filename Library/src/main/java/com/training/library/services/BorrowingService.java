package com.training.library.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.request.BorrowingDto;
import com.training.library.dto.request.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.helper.ExcelToDtoMapper;
import com.training.library.mapper.DtoToEntity;
import com.training.library.models.Borrowing;
import com.training.library.models.Upload;
import com.training.library.repositories.UploadRepository;

@Service
public class BorrowingService {

	@Autowired
	protected DtoToEntity dtoToEntityMapper;
	@Autowired
	private UploadRepository uploadRepository;

	public ResponseDto saveBorrowings(MultipartFile file, String username)
			throws IOException, CustomExceptionHandler, URISyntaxException, ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException,
			NoSuchMethodException, SecurityException, NoSuchFieldException {

		ExcelToDtoMapper mapper = new ExcelToDtoMapper(file);
		List<BorrowingDto> borrowingDto = mapper.mapToList(BorrowingDto.class);
		List<Borrowing> borrowings = dtoToEntityMapper.toBorrowings(borrowingDto);

		Upload upload = new Upload();
		upload.setFileName(file.getOriginalFilename());
		upload.setUploadName(username);
		upload.setBorrowings(borrowings);
		upload.setUploadDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		uploadRepository.save(upload);

		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("Inserted rows: ", borrowings.size());
		result.setResult(res);
		return result;
	}
}