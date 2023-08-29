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
import com.training.library.dto.request.ShelfRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.MessageResponseDto;
import com.training.library.dto.response.ShelfResponseDto;
import com.training.library.entity.Location;
import com.training.library.entity.Section;
import com.training.library.entity.Shelf;
import com.training.library.entity.User;
import com.training.library.repositories.SectionRepository;
import com.training.library.repositories.ShelfRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@PropertySource("classpath:message.properties")
public class ShelfService {
	@Autowired
	private ShelfRepository shelfRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private SectionRepository sectionRepository;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	private Shelf newShelf;
	@Value("${rabbitmq.queue.name}")
	private String queue;
	@Value("${rabbitmq.exchange.name}")
	private String exchange;
	@Value("${rabbitmq.routingkey.name}")
	private String routingkey;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public ShelfResponseDto findShelf(Long id) {
		Optional<ShelfResponseDto> shelf = shelfRepository.findByShelfIdAndDeletedAtIsNull(id);
		if (shelf.isPresent()) {
			return shelf.get();
		}
		return null;
	}

	public Page<ShelfResponseDto> findShelfs(FilterDto dto) {
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return shelfRepository.findAllByDeletedAtIsNull(dto.getSearch(), pageable);
	}

	public ResponseEntity<CustomBaseResponseDto> saveShelf(ShelfRequestDto dto, String userName) {
		User user = null;
		Shelf shelf = new Shelf();
		user = userService.findByPhone(Long.parseLong(userName));
		if (user == null) {
			user = userService.newUser(userName);
		}
		shelf.setUser(user);
		Optional<Section> section = sectionRepository.findById(dto.getSectionId());
		if (section.isPresent()) {
			shelf.setSection(section.get());
		}
		shelf.setShelfNo(dto.getShelfNo());

		Shelf save = shelfRepository.save(shelf);
		return ResponseEntity
				.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS), save.getShelfId()));

	}

	public ResponseEntity<CustomBaseResponseDto> updateShelf(Long id, ShelfRequestDto dto) {
		Optional<Shelf> shelfOptional = shelfRepository.findById(id);
		if (shelfOptional.isPresent()) {
			Shelf shelf = shelfOptional.get();
			Optional<Section> section = sectionRepository.findById(dto.getSectionId());
			if (section.isPresent()) {
				shelf.setSection(section.get());
			}
			shelf.setShelfNo(dto.getShelfNo());
			shelfRepository.save(shelf);
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteShelf(Long id) {
		shelfRepository.deleteById(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public List<ShelfResponseDto> findShelfsBySection(Long sectionId) {
		return shelfRepository.getAllBySection_SectionIdAndDeletedAtIsNotNull(sectionId);
	}

	public ResponseEntity<CustomBaseResponseDto> deleteLetter(ShelfRequestDto dto, String userName) {
		Optional<Shelf> shelfOptional = shelfRepository.findById(dto.getShelfId());
		Shelf shelf = null;
		Optional<Shelf> newShelfOptional = null;
		ResponseEntity<CustomBaseResponseDto> newShelfResult = null;
		if (shelfOptional.isPresent()) {
			shelf = shelfOptional.get();
			List<Location> locations = shelf.getLocation();
			newShelfResult = saveShelf(dto, userName);
			if (newShelfResult.getBody() != null) {
				newShelfOptional = shelfRepository.findById(newShelfResult.getBody().getSaveEntityId());
				if (newShelfOptional.isPresent()) {
					newShelf = newShelfOptional.get();
				}
			}

			locations.stream().map(l -> setShelf(l)).collect(Collectors.toList());
			newShelf.setLocation(locations);
			shelf.setLocation(locations);
			shelfRepository.delete(shelf);
			shelfRepository.save(newShelf);

		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	private Location setShelf(Location l) {
		l.setShelf(newShelf);
		return l;
	}

	public ResponseEntity<CustomBaseResponseDto> autoDeleteMessage(@Valid ShelfRequestDto shelfDto, String userName,
			String entity) {
		MessageResponseDto dto = new MessageResponseDto();
		dto.setEntity(entity);
		dto.setEntityRequestDto(shelfDto);
		dto.setUserName(userName);
		if (shelfRepository.countBySection_SectionIdAndShelfNo(shelfDto.getSectionId(), shelfDto.getShelfNo()) > 0) {
			throw new RuntimeException("Shelf already available!!");
		}
		rabbitTemplate.convertAndSend(exchange, routingkey, dto);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}
}
