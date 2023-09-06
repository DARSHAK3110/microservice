package com.training.library.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.training.library.dto.request.BookDto;
import com.training.library.entity.BookStatus;
import com.training.library.entity.Location;
import com.training.library.services.BookStatusService;
import com.training.library.services.LocationService;

@Component
public abstract class ToEntity {

	@Autowired
	private BookStatusService bookStatusService;
	@Autowired
	private LocationService locationService;

	@AfterMapping
	public void setLocationOnBookStatusTreeDTO(BookDto bookDto, @MappingTarget BookStatus book) {
		book.setLocation(mapLocationFromLocationId(bookDto.getLocationId()));
	}

	@BeforeMapping
	public BookStatus setBookDetails(BookDto bookDto) {
		Long locationId = bookDto.getLocationId();
		BookStatus bookStatus = bookStatusService.findByLocationId(locationId);
		Location location = locationService.findLocationById(locationId);
		if (bookStatus != null) {
			Location locationNowAvailable = bookStatus.getLocation();
			locationNowAvailable.setIsAvailable(true);
			locationService.updateLocationAvailability(locationNowAvailable);
			bookStatus.setAvailable(true);
			bookStatus.setLocation(location);
		}
		return bookStatus;
	}

//
//	public BookStatus mapBookStatusByLocation(BookDto bookDto) {
//		Long locationId = bookDto.getLocationId();
//		Location location = locationService.findLocationById(locationId);
//		if (location != null) {
//			if (Boolean.TRUE.equals(location.getIsAvailable())) {
//				BookStatus bookStatus = bookStatusService.findByLocationId(locationId);
//				if (bookStatus != null) {
//					if (Objects.equals(bookStatus.getBookDetails().getIsbn(), bookDto.getIsbn())) {
//						Location locationNowAvailable = bookStatus.getLocation();
//						locationNowAvailable.setIsAvailable(true);
//						locationService.updateLocationAvailability(locationNowAvailable);
//					}
//				} else {
//					bookStatus = new BookStatus();
//				}
//				bookStatus.setAvailable(true);
//				bookStatus.setLocation(location);
//				return bookStatus;
//			}
//			return null;
//		}
//		return null;
//	}
//
	public Location mapLocationFromLocationId(Long locationId) {
		return locationService.findLocationById(locationId);
	}
}
