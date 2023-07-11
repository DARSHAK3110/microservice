package com.training.library.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.request.AuthorDto;
import com.training.library.dto.response.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.helper.ExcelToDtoMapper;
import com.training.library.mapper.DtoToEntity;
import com.training.library.models.Author;
import com.training.library.models.Upload;
import com.training.library.repositories.AuthorRepository;
import com.training.library.repositories.UploadRepository;

@Service
public class AuthorService {

	@Autowired
	protected DtoToEntity dtoToEntityMapper;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private UploadRepository uploadRepository;

	public ResponseDto saveAuthors(MultipartFile file, String username) throws IOException, CustomExceptionHandler,
			ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {

		ExcelToDtoMapper mapper = new ExcelToDtoMapper(file);
		List<AuthorDto> authorsDto = mapper.mapToList(AuthorDto.class);
		List<Author> authors = dtoToEntityMapper.authorDtoToAuthor(authorsDto);

		Upload upload = new Upload();
		upload.setFileName(file.getOriginalFilename());
		upload.setUploadName(username);
		upload.setAuthors(authors);
		upload.setUploadDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		uploadRepository.save(upload);

		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("insertedRows", authors.size());
		res.put("uploadId", upload.getUploadId());
		if (authorsDto.size() > 2) {
			res.put("data", authorsDto.subList(0, 2));

		} else {
			res.put("data", authorsDto);
		}
		result.setResult(res);		
		return result;
	}

	public Author getAuthor(Long id) {
		Optional<Author> author = authorRepository.findById(id);
		if (author.isPresent()) {
			return author.get();
		}
		return null;
	}

	public ResponseDto getAuthorsByUploadId(Long uploadId, int pageNumber, int pageSize) {
		Pageable pageble = PageRequest.of(pageNumber, pageSize);
		Page<AuthorDto> authoByUploadId = authorRepository.findAuthorsByUploadId(uploadId, pageble);
		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("uploadId", uploadId);
		res.put("data", authoByUploadId);
		result.setResult(res);		
		return result;
	}
}
