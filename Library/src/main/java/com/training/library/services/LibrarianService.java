package com.training.library.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.request.LibrariranDto;
import com.training.library.dto.response.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.helper.ExcelToDtoMapper;
import com.training.library.mapper.DtoToEntity;
import com.training.library.models.Librarian;
import com.training.library.models.Upload;
import com.training.library.repositories.LibrariansRepository;
import com.training.library.repositories.UploadRepository;

@Service
public class LibrarianService {

	@Autowired
	protected DtoToEntity dtoToEntityMapper;
	@Autowired
	private UploadRepository uploadRepository;
	@Autowired
	private LibrariansRepository librarianRepository;

	public ResponseDto saveLibrarians(MultipartFile file, String username)
			throws IOException, CustomExceptionHandler, ClassNotFoundException,
			IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException,
			NoSuchMethodException, SecurityException, NoSuchFieldException {
		ExcelToDtoMapper mapper = new ExcelToDtoMapper(file);
		List<LibrariranDto> librariansDto = mapper.mapToList(LibrariranDto.class);
		List<Librarian> librarians = dtoToEntityMapper.librariansDtoToLibrarians(librariansDto);

		Upload upload = new Upload();
		upload.setFileName(file.getOriginalFilename());
		upload.setUploadName(username);
		upload.setLibrarians(librarians);
		upload.setUploadDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		uploadRepository.save(upload);

		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("insertedRows", librariansDto.size());
		res.put("uploadId", upload.getUploadId());
		if (librariansDto.size() > 2) {
			res.put("data", librariansDto.subList(0, 2));
		} else {
			res.put("data", librariansDto);
		}
		result.setResult(res);
		return result;
	}

	public ResponseDto getLibrariansByUploadId(Long uploadId, int pageNumber, int pageSize) {
		Pageable pageble = PageRequest.of(pageNumber, pageSize);
		Page<LibrariranDto> genreByUploadId = librarianRepository.findLibrariansByUploadId(uploadId, pageble);
		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("uploadId", uploadId);
		res.put("data", genreByUploadId);
		result.setResult(res);
		return result;
	}
}
