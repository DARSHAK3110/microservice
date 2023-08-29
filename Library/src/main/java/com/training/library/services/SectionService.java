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
import com.training.library.dto.request.SectionRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.MessageResponseDto;
import com.training.library.dto.response.SectionResponseDto;
import com.training.library.entity.Floor;
import com.training.library.entity.Section;
import com.training.library.entity.Shelf;
import com.training.library.entity.User;
import com.training.library.repositories.FloorRepository;
import com.training.library.repositories.SectionRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@PropertySource("classpath:message.properties")
public class SectionService {
	@Value("${rabbitmq.queue.name}")
	private String queue;
	@Value("${rabbitmq.exchange.name}")
	private String exchange;
	@Value("${rabbitmq.routingkey.name}")
	private String routingkey;
	@Autowired
	private SectionRepository sectionRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private FloorRepository floorRepository;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	@Autowired
	private RabbitTemplate rabbitTemplate;
	private Section newSection;

	public SectionResponseDto findSection(Long id) {
		Optional<SectionResponseDto> section = sectionRepository.findBySectionIdAndDeletedAtIsNull(id);
		if (section.isPresent()) {
			return section.get();
		}
		return null;
	}

	public Page<SectionResponseDto> findSections(FilterDto dto) {
		Pageable pageble = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return sectionRepository.findAllByDeletedAtIsNull(dto.getSearch(), pageble);
	}

	public ResponseEntity<CustomBaseResponseDto> saveSection(SectionRequestDto dto, String userName) {
		Section section = new Section();
		User user = null;
		user = userService.findByPhone(Long.parseLong(userName));

		if (user == null) {
			user = userService.newUser(userName);
		}
		section.setUser(user);
		Optional<Floor> floor = floorRepository.findByFloorNo(dto.getFloorNo());
		if (floor.isPresent()) {
			section.setFloor(floor.get());
		}
		section.setSectionName(dto.getSectionName());

		Section save = sectionRepository.save(section);
		return ResponseEntity
				.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS), save.getSectionId()));
	}

	public ResponseEntity<CustomBaseResponseDto> updateSection(Long id, SectionRequestDto dto) {
		Optional<Section> sectionOptional = sectionRepository.findById(id);
		if (sectionOptional.isPresent()) {
			Section section = sectionOptional.get();
			Optional<Floor> floor = floorRepository.findByFloorNo(dto.getFloorNo());
			if (floor.isPresent()) {
				section.setFloor(floor.get());
			}
			section.setSectionName(dto.getSectionName());
			sectionRepository.save(section);
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteSection(Long id) {
		sectionRepository.deleteById(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public List<SectionResponseDto> findSectionsByFloors(Long floorId) {
		return sectionRepository.getAllByFloor_FloorNoAndDeletedAtIsNotNull(floorId);
	}

	public ResponseEntity<CustomBaseResponseDto> deleteLetter(SectionRequestDto dto, String userName) {
		Optional<Section> sectionOptional = sectionRepository.findById(dto.getSectionId());
		Section section = null;
		Optional<Section> newSectionOptional = null;
		ResponseEntity<CustomBaseResponseDto> newSectionResult = null;
		if (sectionOptional.isPresent()) {
			section = sectionOptional.get();
			List<Shelf> shelfs = section.getShelf();
			newSectionResult = saveSection(dto, userName);
			newSectionOptional = sectionRepository.findById(newSectionResult.getBody().getSaveEntityId());
			if (newSectionOptional.isPresent()) {
				newSection = newSectionOptional.get();
			}
			shelfs.stream().map(s -> setSection(s)).collect(Collectors.toList());
			newSection.setShelf(shelfs);
			section.setShelf(null);
			sectionRepository.delete(section);
			sectionRepository.save(newSection);
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	private Shelf setSection(Shelf s) {
		s.setSection(newSection);
		return s;
	}

	public ResponseEntity<CustomBaseResponseDto> autoDeleteMessage(@Valid SectionRequestDto secDto, String userName,
			String entity) {
		MessageResponseDto dto = new MessageResponseDto();
		dto.setEntity(entity);
		dto.setEntityRequestDto(secDto);
		dto.setUserName(userName);
		if (sectionRepository.countBySectionNameAndFloor_FloorId(secDto.getSectionName(), secDto.getFloorNo()) > 0) {
			throw new RuntimeException("Section already available!!");
		}
		rabbitTemplate.convertAndSend(exchange, routingkey, dto);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}
}
