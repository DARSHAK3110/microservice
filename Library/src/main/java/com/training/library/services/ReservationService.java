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

import com.training.library.dto.request.ReservationDto;
import com.training.library.dto.response.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.helper.ExcelToDtoMapper;
import com.training.library.mapper.DtoToEntity;
import com.training.library.models.Reservation;
import com.training.library.models.Upload;
import com.training.library.repositories.ReservationRepository;
import com.training.library.repositories.UploadRepository;

@Service
public class ReservationService {

	@Autowired
	protected DtoToEntity dtoToEntityMapper;
	@Autowired
	private UploadRepository uploadRepository;
	@Autowired
	private ReservationRepository reservationRepository;
	public ResponseDto saveReservations(MultipartFile file, String username) throws IOException, CustomExceptionHandler,
			ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {

		ExcelToDtoMapper mapper = new ExcelToDtoMapper(file);
		List<ReservationDto> reservationDto = mapper.mapToList(ReservationDto.class);
		List<Reservation> reservations = dtoToEntityMapper.toReservations(reservationDto);

		Upload upload = new Upload();
		upload.setFileName(file.getOriginalFilename());
		upload.setUploadName(username);
		upload.setReservations(reservations);
		upload.setUploadDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		uploadRepository.save(upload);

		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("insertedRows", reservationDto.size());
		res.put("uploadId", upload.getUploadId());
		if (reservationDto.size() > 2) {
			res.put("data", reservationDto.subList(0, 2));
		} else {
			res.put("data", reservationDto);
		}
		result.setResult(res);
		return result;
	}

	public ResponseDto getReservationsByUploadId(Long uploadId, int pageNumber, int pageSize) {
		Pageable pageble = PageRequest.of(pageNumber, pageSize);
		Page<ReservationDto> reservationsByUploadId = reservationRepository.findReservationsByUploadId(uploadId, pageble);
		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		Map<String, Object> res = new HashMap<>();
		res.put("uploadId", uploadId);
		res.put("data", reservationsByUploadId);
		result.setResult(res);
		return result;
	}
}
