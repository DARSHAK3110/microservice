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

import com.training.library.dto.request.BorrowingDto;
import com.training.library.dto.request.ReservationDto;
import com.training.library.dto.response.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.helper.ExcelToDtoMapper;
import com.training.library.mapper.DtoToEntity;
import com.training.library.models.Borrowing;
import com.training.library.models.Upload;
import com.training.library.repositories.BorrowingRepository;
import com.training.library.repositories.UploadRepository;

@Service
public class BorrowingService {

	@Autowired
	protected DtoToEntity dtoToEntityMapper;
	@Autowired
	private UploadRepository uploadRepository;
	@Autowired
	private BorrowingRepository borrowingRepository;

	public ResponseDto saveBorrowings(MultipartFile file, String username) throws IOException, CustomExceptionHandler,
			ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {

		ExcelToDtoMapper mapper = new ExcelToDtoMapper(file);
		List<BorrowingDto> borrowingDto = mapper.mapToList(BorrowingDto.class);
		List<Borrowing> borrowings = dtoToEntityMapper.toBorrowings(borrowingDto);

		Upload upload = new Upload();
		upload.setFileName(file.getOriginalFilename());
		upload.setUploadName(username);
		upload.setBorrowings(borrowings);
		upload.setUploadDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		uploadRepository.save(upload);

		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("insertedRows", borrowingDto.size());
		res.put("uploadId", upload.getUploadId());
		if (borrowingDto.size() > 2) {
			res.put("data", borrowingDto.subList(0, 2));
		} else {
			res.put("data", borrowingDto);
		}
		result.setResult(res);
		return result;
	}

	public ResponseDto getBorrowingsByUploadId(Long uploadId, int pageNumber, int pageSize) {
		Pageable pageble = PageRequest.of(pageNumber, pageSize);
		Page<ReservationDto> borrowigsByUploadId = borrowingRepository.findBorrowingsByUploadId(uploadId, pageble);
		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("uploadId", uploadId);
		res.put("data", borrowigsByUploadId );
		result.setResult(res);
		return result;
	}
}