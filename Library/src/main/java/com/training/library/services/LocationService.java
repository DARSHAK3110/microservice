package com.training.library.services;

import java.util.List;
import java.util.Optional;

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
import com.training.library.dto.request.LocationRequestDto;
import com.training.library.dto.response.CustomBaseResponseDto;
import com.training.library.dto.response.LocationResponseDto;
import com.training.library.dto.response.MessageResponseDto;
import com.training.library.entity.BookStatus;
import com.training.library.entity.Location;
import com.training.library.entity.Shelf;
import com.training.library.entity.User;
import com.training.library.repositories.LocationRepository;
import com.training.library.repositories.ShelfRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Service
@PropertySource("classpath:message.properties")
public class LocationService {
	@Value("${rabbitmq.queue.name}")
	private String queue;
	@Value("${rabbitmq.exchange.name}")
	private String exchange;
	@Value("${rabbitmq.routingkey.name}")
	private String routingkey;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private BookStatusService bookStatusService;
	@Autowired
	private ShelfRepository shelfRepository;
	private static final String OPERATION_SUCCESS = "operation.success";
	@Autowired
	private Environment env;
	@Autowired
	private RabbitTemplate rabbitTemplate;

	public LocationResponseDto findLocation(Long id) {
		Optional<LocationResponseDto> location = locationRepository.findByLocationIdAndDeletedAtIsNull(id);
		if (location.isPresent()) {
			return location.get();
		}
		return null;
	}

	public Page<LocationResponseDto> findLocations(FilterDto dto) {
		Pageable pageable = PageRequest.of(dto.getPageNumber(), dto.getPageSize());
		return locationRepository.findAllByDeletedAtIsNull(dto.getSearch(), pageable);
	}

	public ResponseEntity<CustomBaseResponseDto> saveLocation(LocationRequestDto dto, String userName) {
		Location location = new Location();
		User user = null;
		user = userService.findByPhone(Long.parseLong(userName));
		if (user == null) {
			user = userService.newUser(userName);
		}
		location.setUser(user);
		location.setPosition(dto.getPosition());
		Optional<Shelf> shelf = shelfRepository.findById(dto.getShelfId());
		if (shelf.isPresent()) {
			location.setShelf(shelf.get());
		}
		location.setIsAvailable(true);
		Location save = locationRepository.save(location);
		return ResponseEntity
				.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS), save.getLocationId()));
	}

	public ResponseEntity<CustomBaseResponseDto> updateLocation(Long id, LocationRequestDto dto) {
		Optional<Location> locationOptional = locationRepository.findById(id);
		if (locationOptional.isPresent()) {
			Location location = locationOptional.get();
			location.setPosition(dto.getPosition());
			Optional<Shelf> shelf = shelfRepository.findById(dto.getShelfId());
			if (shelf.isPresent()) {
				location.setShelf(shelf.get());
			}
			locationRepository.save(location);
		}
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	@Transactional
	public ResponseEntity<CustomBaseResponseDto> deleteLocation(Long id) {
		locationRepository.deleteById(id);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}

	public Location findLocationById(Long id) {
		Optional<Location> location = locationRepository.findById(id);
		if (location.isPresent()) {
			return location.get();
		}
		return null;
	}

	public List<LocationResponseDto> findLocationsByShelf(Long shelfId) {
		return locationRepository.getAllByShelf_ShelfIdAndDeletedAtIsNotNull(shelfId);
	}

	public void updateLocationAvailability(Location location) {
		locationRepository.save(location);
	}

	public ResponseEntity<CustomBaseResponseDto> deleteLetter(LocationRequestDto dto, String userName) {

		Optional<Location> locationOptional = locationRepository.findById(dto.getLocationId());
		ResponseEntity<CustomBaseResponseDto> newLocationResult = saveLocation(dto, userName);
		Optional<Location> newLocationOptional = locationRepository
				.findById(newLocationResult.getBody().getSaveEntityId());
		Location location = null;
		Location newLocation = null;
		if (newLocationOptional.isPresent()) {
			newLocation = newLocationOptional.get();
			newLocation.setIsAvailable(false);
		}
		if (locationOptional.isPresent()) {
			location = locationOptional.get();
			BookStatus bookStatus = location.getBookStatus();
			bookStatus.setLocation(newLocation);
			bookStatusService.updateBookStatusAvailability(bookStatus);
			location.setBookStatus(null);
			locationRepository.delete(location);
			locationRepository.save(newLocation);
		}

		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));

	}

	public ResponseEntity<CustomBaseResponseDto> autoDeleteMessage(@Valid LocationRequestDto locDto, String userName,
			String entity) {
		MessageResponseDto dto = new MessageResponseDto();
		dto.setEntity(entity);
		dto.setEntityRequestDto(locDto);
		dto.setUserName(userName);
		if (locationRepository.countByPositionAndShelf_ShelfId(locDto.getPosition(), locDto.getShelfId()) > 0) {
			throw new RuntimeException("Location already available!!");
		}
		rabbitTemplate.convertAndSend(exchange, routingkey, dto);
		return ResponseEntity.ok(new CustomBaseResponseDto(env.getRequiredProperty(OPERATION_SUCCESS)));
	}
}
