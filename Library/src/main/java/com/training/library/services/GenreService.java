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

import com.training.library.dto.request.GenreDto;
import com.training.library.dto.response.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.helper.ExcelToDtoMapper;
import com.training.library.mapper.DtoToEntity;
import com.training.library.models.Genre;
import com.training.library.models.Upload;
import com.training.library.repositories.GenreRepository;
import com.training.library.repositories.UploadRepository;

@Service
public class GenreService {
	@Autowired
	private GenreRepository genreRepository;
	@Autowired
	protected DtoToEntity dtoToEntityMapper;
	@Autowired
	private UploadRepository uploadRepository;

	public ResponseDto saveGenres(MultipartFile file, String username) throws IOException, CustomExceptionHandler,
			ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {

		ExcelToDtoMapper mapper = new ExcelToDtoMapper(file);
		List<GenreDto> genresDto = mapper.mapToList(GenreDto.class);
		List<Genre> genres = dtoToEntityMapper.genreDtoToGenre(genresDto);

		Upload upload = new Upload();
		upload.setFileName(file.getOriginalFilename());
		upload.setUploadName(username);
		upload.setGenres(genres);
		upload.setUploadDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		uploadRepository.save(upload);

		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("insertedRows", genres.size());
		res.put("uploadId", upload.getUploadId());
		if (genresDto.size() > 2) {
			res.put("data", genresDto.subList(0, 2));
		} else {
			res.put("data", genresDto);
		}
		result.setResult(res);
		return result;
	}

	public Genre getGenre(Long genreId) {
		Optional<Genre> genre = genreRepository.findById(genreId);
		if (genre.isPresent()) {
			return genre.get();
		}
		return null;
	}

	public ResponseDto getGenresByUploadId(Long uploadId, int pageNumber, int pageSize) {
		Pageable pageble = PageRequest.of(pageNumber, pageSize);
		Page<GenreDto> genreByUploadId = genreRepository.findGenresByUploadId(uploadId, pageble);
		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("uploadId", uploadId);
		res.put("data", genreByUploadId);
		result.setResult(res);
		return result;
	}

}
