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

import com.training.library.dto.request.BookDto;
import com.training.library.dto.response.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.helper.ExcelToDtoMapper;
import com.training.library.mapper.DtoToEntity;
import com.training.library.models.Book;
import com.training.library.models.Upload;
import com.training.library.repositories.BookRepository;
import com.training.library.repositories.UploadRepository;

@Service
public class BookService {
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private DtoToEntity dtoToEntity;
	@Autowired
	private UploadRepository uploadRepository;

	public ResponseDto saveBooks(MultipartFile file, String username) throws IOException, CustomExceptionHandler,
			ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		
		ExcelToDtoMapper mapper = new ExcelToDtoMapper(file);
		List<BookDto> booksDto = mapper.mapToList(BookDto.class);
		List<Book> books = dtoToEntity.toBooks(booksDto);

		Upload upload = new Upload();
		upload.setFileName(file.getOriginalFilename());
		upload.setUploadName(username);
		upload.getBooks().addAll(books);
		upload.setUploadDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		uploadRepository.save(upload);
		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("insertedRows", booksDto.size());
		res.put("uploadId", upload.getUploadId());
		if (booksDto.size() > 2) {
			res.put("data", booksDto.subList(0, 2));
		} else {
			res.put("data", booksDto);
		}
		result.setResult(res);
		return result;
	}

	public Book getBook(Long isbn) {
		Optional<Book> book = bookRepository.findById(isbn);
		if (book.isPresent()) {
			return book.get();
		}
		return null;
	}

	public ResponseDto getBooksByUploadId(Long uploadId, int pageNumber, int pageSize) {
		Pageable pageble = PageRequest.of(pageNumber, pageSize);
		Page<BookDto> booksByUploadId = bookRepository.findBooksByUploadId(uploadId, pageble);
		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("uploadId", uploadId);
		res.put("data", booksByUploadId);
		result.setResult(res);
		return result;
	}
}
