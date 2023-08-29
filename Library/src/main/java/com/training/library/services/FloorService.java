package com.training.library.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.FilterDto;
import com.training.library.dto.request.FloorRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.FloorResponseDto;
import com.training.library.dto.response.MessageResponseDto;
import com.training.library.entity.Floor;
import com.training.library.entity.Section;
import com.training.library.entity.User;
import com.training.library.repositories.FloorRepository;

import jakarta.validation.Valid;

@Service
@PropertySource("classpath:message.properties")
public class FloorService {
	@Value("${rabbitmq.queue.name}")
	private String queue;
	@Value("${rabbitmq.exchange.name}")
	private String exchange;
	@Value("${rabbitmq.routingkey.name}")
	private String routingkey;
	@Autowired
	private UserService userService;
	@Autowired
	private FloorRepository floorRepository;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	private Floor newFloor;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public FloorResponseDto findFloor(Long id) {
		Optional<FloorResponseDto> floor = floorRepository.findByFloorIdAndDeletedAtIsNull(id);
		if (floor.isPresent()) {
			return floor.get();
		}
		return null;
	}

	public Page<FloorResponseDto> findFloors(FilterDto dto) {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return floorRepository.findAllByDeletedAtIsNull(dto.getSearch(), pageble);
	}

	public ResponseEntity<CustomBaseResponseDto> saveFloor(FloorRequestDto dto, String userName) {
		User user = null;
		Floor floor = new Floor();
		user = userService.findByPhone(Long.parseLong(userName));
		if (user == null) {
			user = userService.newUser(userName);
		}
		floor.setUser(user);
			floor.setFloorNo(dto.getFloorNo());
		Floor save = floorRepository.save(floor);
		return ResponseEntity
				.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS), save.getFloorId()));

	}

	public ResponseEntity<CustomBaseResponseDto> updateFloor(Long id, FloorRequestDto dto) {
		Optional<Floor> floorOptional = floorRepository.findById(id);
		if (floorOptional.isPresent()) {
			Floor floor = floorOptional.get();
			floor.setFloorNo(dto.getFloorNo());
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public ResponseEntity<CustomBaseResponseDto> deleteFloor(Long id) {
		floorRepository.deleteById(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public ResponseEntity<CustomBaseResponseDto> deleteLetter(FloorRequestDto dto, String userName) {
		Optional<Floor> floorOptional = floorRepository.findById(dto.getFloorId());
		Floor floor = null;
		Optional<Floor> newFloorOptional = null;
		ResponseEntity<CustomBaseResponseDto> newFloorResult = null;
		if (floorOptional.isPresent()) {
			floor = floorOptional.get();
			List<Section> sections = floor.getSection();
			newFloorResult = saveFloor(dto, userName);
			newFloorOptional = floorRepository.findById(Long.parseLong(newFloorResult.getBody().getMessage()));
			if (newFloorOptional.isPresent()) {
				newFloor = newFloorOptional.get();
			}
			sections.stream().map(s -> setFloor(s)).collect(Collectors.toList());
			newFloor.setSection(sections);
			floor.setSection(null);
			floorRepository.delete(floor);
			floorRepository.save(newFloor);
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	private Section setFloor(Section s) {
		s.setFloor(newFloor);
		return s;
	}

	public ResponseEntity<CustomBaseResponseDto> autoDeleteMessage(@Valid FloorRequestDto floorDto, String userName,
			String entity) {
		MessageResponseDto dto = new MessageResponseDto();
		dto.setEntity(entity);
		dto.setEntityRequestDto(floorDto);
		dto.setUserName(userName);
		if (floorRepository.countByFloorNo(floorDto.getFloorNo()) > 0) {
			throw new RuntimeException("Floor already available!!");
		}
		rabbitTemplate.convertAndSend(exchange, routingkey, dto);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

}
