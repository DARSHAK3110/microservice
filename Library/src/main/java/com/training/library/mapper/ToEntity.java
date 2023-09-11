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
		Location location = mapLocationFromLocationId(bookDto.getLocationId());
		if(location == null) {
			throw new RuntimeException("The Location is invalid:" + bookDto.getLocationId());
				
		}if (!location.getIsAvailable()) {
			throw new RuntimeException("The Location with id:" + location.getLocationId() + "already reserved!!");
		} else {
			book.setLocation(location);
		}
	}

	@BeforeMapping
	public BookStatus setBookDetails(BookDto bookDto) {
		Long locationId = bookDto.getLocationId();
		BookStatus bookStatus = bookStatusService.findByLocationId(locationId);
		Location location = locationService.findLocationById(locationId);
		if(location == null) {
			throw new RuntimeException("The Location with id: "+  locationId+"is invalid");
				
		}
		if (bookStatus != null) {
			if (!location.getIsAvailable()) {
				throw new RuntimeException("The Location with id:" + location.getLocationId() + "already reserved!!");
			} else {
				bookStatus.setLocation(location);
			}
			Location locationNowAvailable = bookStatus.getLocation();
			locationNowAvailable.setIsAvailable(true);
			locationService.updateLocationAvailability(locationNowAvailable);
			bookStatus.setAvailable(true);
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
