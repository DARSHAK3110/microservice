package com.training.library.services;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.training.library.dto.request.GenreDto;
import com.training.library.dto.request.ResponseDto;
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

	public ResponseDto saveGenres(MultipartFile file, String username) throws IOException, CustomExceptionHandler, ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
	
		ExcelToDtoMapper mapper = new ExcelToDtoMapper(file);
		List<GenreDto> genresDto = mapper.mapToList(GenreDto.class);
		List<Genre> genres = dtoToEntityMapper.genreDtoToGenre(genresDto);

		Upload upload = new Upload();
		upload.setFileName(file.getOriginalFilename());
		upload.setUploadName(username);
		upload.setGenres(genres);
		upload.setUploadDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
		uploadRepository.save(upload);
		
		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("Inserted rows: ", genres.size());
		result.setResult(res);
		return result;
	}

	public Genre getGenre(Long genreId) {
		Optional<Genre> genre = genreRepository.findById(genreId);
		if(genre.isPresent()) {
			return genre.get();
		}
		return null;
	}

}
