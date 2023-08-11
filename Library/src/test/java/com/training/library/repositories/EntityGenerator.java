package com.training.library.repositories;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;

import com.training.library.dto.request.AuthorRequestDto;
import com.training.library.dto.request.BookBorrowingRequestDto;
import com.training.library.dto.request.BookDetailsRequestDto;
import com.training.library.dto.request.BookReservationRequestDto;
import com.training.library.dto.request.BookStatusRequestDto;
import com.training.library.dto.request.FilterDto;
import com.training.library.dto.request.FloorRequestDto;
import com.training.library.dto.request.LocationRequestDto;
import com.training.library.dto.request.SectionRequestDto;
import com.training.library.dto.request.ShelfRequestDto;
import com.training.library.dto.response.AuthorResponseDto;
import com.training.library.dto.response.BookBorrowingResponseDto;
import com.training.library.dto.response.BookDetailsResponseDto;
import com.training.library.dto.response.BookReservationResponseDto;
import com.training.library.dto.response.BookStatusResponseDto;
import com.training.library.dto.response.FloorResponseDto;
import com.training.library.dto.response.LocationResponseDto;
import com.training.library.dto.response.SectionResponseDto;
import com.training.library.dto.response.ShelfResponseDto;
import com.training.library.entity.Author;
import com.training.library.entity.BookBorrowing;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookReservation;
import com.training.library.entity.BookStatus;
import com.training.library.entity.Floor;
import com.training.library.entity.Location;
import com.training.library.entity.Section;
import com.training.library.entity.Shelf;
import com.training.library.entity.Upload;
import com.training.library.entity.User;

@Component
public class EntityGenerator {
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private ShelfRepository shelfRepository;
	@Autowired
	private SectionRepository sectionRepository;
	@Autowired
	private LocationRepository locationRepository;
	@Autowired
	private FloorRepository floorRepository;
	@Autowired
	private BookDetailsRepository bookDetailsRepository;
	@Autowired
	private BookStatusRepository bookStatusRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookBorrowingRepository bookBorrowingRepository;

	public EntityGenerator() {
		super();
	}

	public Floor getFloor() {
		Floor floor = new Floor();
		floor.setFloorNo(1000000L);
		return floor;
	}

	public Floor getMockFloor() {
		Floor floor = new Floor();
		floor.setFloorNo(1000000L);
		return floor;
	}

	public Section getSection() {
		Section section = new Section();
		section.setSectionName("Manga");
		section.setFloor(floorRepository.save(getFloor()));
		return section;
	}

	public Section getMockSection() {
		Section section = new Section();
		section.setSectionName("Manga");
		section.setFloor(getMockFloor());
		return section;
	}

	public Shelf getShelf() {
		Shelf shelf = new Shelf();
		shelf.setShelfNo(1000L);
		shelf.setSection(sectionRepository.save(getSection()));
		return shelf;
	}

	public Shelf getMockShelf() {
		Shelf shelf = new Shelf();
		shelf.setShelfNo(1000L);
		shelf.setSection(getMockSection());
		return shelf;
	}

	public Location getLocation() {
		Location location = new Location();
		location.setPosition("a11");
		location.setShelf(shelfRepository.save(getShelf()));
		return location;
	}

	public Location getMockLocation() {
		Location location = new Location();
		location.setPosition("a11");
		location.setShelf(getMockShelf());
		return location;
	}

	public Author getAuthor() {
		Author author = new Author();
		author.setAuthorName("Author1000");
		author.setAuthorDOB(new Date(System.currentTimeMillis()));
		return author;
	}

	public BookDetails getBookDetails() {
		BookDetails bookDetails = new BookDetails();
		bookDetails.setAuthor(authorRepository.save(getAuthor()));
		bookDetails.setTitle("test title");
		bookDetails.setIsbn(9999999999999L);
		return bookDetails;
	}

	public BookDetails getMockBookDetails() {
		BookDetails bookDetails = new BookDetails();
		bookDetails.setAuthor(getMockAuthor());
		bookDetails.setTitle("test title");
		bookDetails.setIsbn(9999999999999L);
		return bookDetails;
	}

	public BookStatus getBookStatus() {
		BookStatus bookStatus = new BookStatus();
		bookStatus.setLocation(locationRepository.save(getLocation()));
		bookStatus.setAvailable(true);
		bookStatus.setBookDetails(bookDetailsRepository.save(getBookDetails()));
		return bookStatus;
	}

	public BookStatus getMockBookStatus() {
		BookStatus bookStatus = new BookStatus();
		bookStatus.setLocation(getMockLocation());
		bookStatus.setAvailable(true);
		bookStatus.setBookDetails(getMockBookDetails());
		return bookStatus;
	}

	public User getUser() {
		User user = new User();
		user.setPhone(1231231231L);
		return user;
	}

	public User getMockUser() {
		User user = new User();
		user.setPhone(1231231231L);
		return user;
	}

	public BookReservation getBookReservation() {

		BookReservation bookReservation = new BookReservation();
		bookReservation.setBookDetails(bookDetailsRepository.save(getBookDetails()));
		bookReservation.setReservationDate(new Date(System.currentTimeMillis()));
		bookReservation.setReserver(userRepository.save(getUser()));
		return bookReservation;
	}

	public BookReservation getMockBookReservation() {

		BookReservation bookReservation = new BookReservation();
		bookReservation.setBookDetails(getMockBookDetails());
		bookReservation.setReservationDate(new Date(System.currentTimeMillis()));
		bookReservation.setReserver(getMockUser());
		return bookReservation;
	}

	public BookBorrowing getBookBorrowing() {

		BookBorrowing bookBorrowing = new BookBorrowing();
		bookBorrowing.setBookStatus(bookStatusRepository.save(getBookStatus()));
		bookBorrowing.setBorrowingDate(new Date(System.currentTimeMillis()));
		bookBorrowing.setBorrower(userRepository.save(getUser()));
		return bookBorrowingRepository.save(bookBorrowing);
	}

	public BookBorrowing getMockBookBorrowing() {

		BookBorrowing bookBorrowing = new BookBorrowing();
		bookBorrowing.setBookStatus(getMockBookStatus());
		bookBorrowing.setBorrowingDate(new Date(System.currentTimeMillis()));
		bookBorrowing.setBorrower(getMockUser());
		return bookBorrowing;
	}

	public FilterDto getFilterDto() {
		FilterDto dto = new FilterDto();
		dto.setPageNumber(0);
		dto.setPageSize(2);
		dto.setSearch("");
		return dto;
	}

	public Author getMockAuthor() {
		Author author = new Author();
		author.setAuthorId(1L);
		author.setAuthorName("Author1000");
		author.setAuthorDOB(new Date(System.currentTimeMillis()));
		return author;
	}

	public AuthorResponseDto getAuthorResponseDto(Long id) {
		AuthorResponseDto authorResponseDto = new AuthorResponseDto();
		authorResponseDto.setAuthorId(id);
		authorResponseDto.setAuthorName("Author1000");
		authorResponseDto.setAuthorDOB(new Date(System.currentTimeMillis()));
		return authorResponseDto;
	}

	public Page<AuthorResponseDto> getAuthorPage() {

		List<AuthorResponseDto> authorList = new ArrayList<>();
		authorList.add(getAuthorResponseDto(1L));
		return PageableExecutionUtils.getPage(authorList, PageRequest.of(0, 2), () -> 1L);
	}

	public AuthorRequestDto getAuthorRequestDto() {
		AuthorRequestDto req = new AuthorRequestDto();
		req.setAuthorId(1L);
		req.setAuthorName("Author10001");
		req.setAuthorDOB(new Date(System.currentTimeMillis()));
		return req;
	}

	public FloorResponseDto getFloorResponseDto(Long id) {
		FloorResponseDto floorResponseDto = new FloorResponseDto();
		floorResponseDto.setFloorId(id);
		floorResponseDto.setFloorNo(0L);
		return floorResponseDto;
	}

	public Page<FloorResponseDto> getFloorPage() {
		List<FloorResponseDto> floorList = new ArrayList<>();
		floorList.add(getFloorResponseDto(1L));
		return PageableExecutionUtils.getPage(floorList, PageRequest.of(0, 2), () -> 1L);
	}

	public FloorRequestDto getFloorRequestDto() {
		FloorRequestDto req = new FloorRequestDto();
		req.setFloorNo(1L);
		return req;
	}

	public SectionResponseDto getSectionResponseDto(Long id) {
		SectionResponseDto sectionResponseDto = new SectionResponseDto();
		sectionResponseDto.setSectionId(id);
		sectionResponseDto.setSectionName("testSection");
		return sectionResponseDto;
	}

	public Page<SectionResponseDto> getSectionPage() {
		List<SectionResponseDto> sectionList = new ArrayList<>();
		sectionList.add(getSectionResponseDto(1L));
		return PageableExecutionUtils.getPage(sectionList, PageRequest.of(0, 2), () -> 1L);
	}

	public SectionRequestDto getSectionRequestDto() {
		SectionRequestDto req = new SectionRequestDto();
		req.setSectionName("testSection");
		req.setFloorNo(0L);
		return req;
	}

	public ShelfResponseDto getShelfResponseDto(Long id) {
		ShelfResponseDto shelfResponseDto = new ShelfResponseDto();
		shelfResponseDto.setShelfId(id);
		shelfResponseDto.setShelfNo(0L);
		shelfResponseDto.setSectionName("testSection");
		return shelfResponseDto;
	}

	public Page<ShelfResponseDto> getShelfPage() {
		List<ShelfResponseDto> shelfList = new ArrayList<>();
		shelfList.add(getShelfResponseDto(1L));
		return PageableExecutionUtils.getPage(shelfList, PageRequest.of(0, 2), () -> 1L);
	}

	public ShelfRequestDto getShelfRequestDto() {
		ShelfRequestDto req = new ShelfRequestDto();
		req.setShelfNo(0L);
		req.setSectionId(0L);
		return req;
	}

	public LocationResponseDto getLocationResponseDto(Long id) {
		LocationResponseDto locationResponseDto = new LocationResponseDto();
		locationResponseDto.setLocationId(id);
		locationResponseDto.setPosition("testPosition");
		locationResponseDto.setShelfNo(0L);
		locationResponseDto.setSectionName("testSection");
		return locationResponseDto;
	}

	public Page<LocationResponseDto> getLocationPage() {
		List<LocationResponseDto> shelfList = new ArrayList<>();
		shelfList.add(getLocationResponseDto(1L));
		return PageableExecutionUtils.getPage(shelfList, PageRequest.of(0, 2), () -> 1L);
	}

	public LocationRequestDto getLocationRequestDto() {
		LocationRequestDto req = new LocationRequestDto();
		req.setPosition("testPosition");
		req.setShelfId(0L);
		return req;
	}

	public BookDetailsResponseDto getBookDetailsResponseDto(Long id) {
		BookDetailsResponseDto bookDetailsResponseDto = new BookDetailsResponseDto();
		bookDetailsResponseDto.setBookDetailsId(id);
		bookDetailsResponseDto.setTitle("testTitle");
		bookDetailsResponseDto.setAuthor("testAuthor");
		bookDetailsResponseDto.setIsbn(1231231231231L);
		return bookDetailsResponseDto;
	}

	public Page<BookDetailsResponseDto> getBookDetailsPage() {
		List<BookDetailsResponseDto> bookDetailsList = new ArrayList<>();
		bookDetailsList.add(getBookDetailsResponseDto(1L));
		return PageableExecutionUtils.getPage(bookDetailsList, PageRequest.of(0, 2), () -> 1L);
	}

	public BookDetailsRequestDto getBookDetailsRequestDto() {
		BookDetailsRequestDto req = new BookDetailsRequestDto();
		req.setTitle("testTitle");
		req.setIsbn(1231231231231L);
		req.setAuthorId(0L);
		return req;
	}

	public Upload getMockUpload() {
		Upload upload = new Upload();
		upload.setBookDetails(List.of(getMockBookDetails()));
		upload.setFileName("testFile");
		upload.setUser(getUser());
		return upload;
	}

	public BookStatusResponseDto getBookStatusResponseDto(Long id) {
		BookStatusResponseDto bookStatusResponseDto = new BookStatusResponseDto();
		bookStatusResponseDto.setBookStatusId(id);
		bookStatusResponseDto.setIsAvailable(true);
		bookStatusResponseDto.setLocationId(0L);
		return bookStatusResponseDto;
	}

	public Page<BookStatusResponseDto> getBookStatusPage() {
		List<BookStatusResponseDto> bookStatusList = new ArrayList<>();
		bookStatusList.add(getBookStatusResponseDto(1L));
		return PageableExecutionUtils.getPage(bookStatusList, PageRequest.of(0, 2), () -> 1L);
	}

	public BookStatusRequestDto getBookStatusRequestDto() {
		BookStatusRequestDto req = new BookStatusRequestDto();
		req.setBookStatusId(0L);
		req.setIsAvailable(true);
		req.setLocationId(0L);
		return req;
	}

	public BookReservationResponseDto getBookReservationResponseDto(Long id) {
		BookReservationResponseDto bookReservationResponseDto = new BookReservationResponseDto();
		bookReservationResponseDto.setBookReservationId(id);
		bookReservationResponseDto.setBookTitle("testTitle");
		bookReservationResponseDto.setUserId(0L);
		return bookReservationResponseDto;
	}

	public Page<BookReservationResponseDto> getBookReservationPage() {
		List<BookReservationResponseDto> bookReservationList = new ArrayList<>();
		bookReservationList.add(getBookReservationResponseDto(1L));
		return PageableExecutionUtils.getPage(bookReservationList, PageRequest.of(0, 2), () -> 1L);
	}

	public BookReservationRequestDto getBookReservationRequestDto() {
		BookReservationRequestDto req = new BookReservationRequestDto();
		req.setBookDetailsId(0L);
		req.setPhone(1231231231L);
		return req;
	}

	public BookBorrowingResponseDto getBookBorrowingResponseDto(Long id) {
		BookBorrowingResponseDto bookBorrowingResponseDto = new BookBorrowingResponseDto();
		bookBorrowingResponseDto.setBookBorrowingId(id);
		bookBorrowingResponseDto.setBookTitle("testTitle");
		bookBorrowingResponseDto.setUserId(0L);
		return bookBorrowingResponseDto;
	}

	public Page<BookBorrowingResponseDto> getBookBorrowingPage() {
		List<BookBorrowingResponseDto> bookBorrowingList = new ArrayList<>();
		bookBorrowingList.add(getBookBorrowingResponseDto(1L));
		return PageableExecutionUtils.getPage(bookBorrowingList, PageRequest.of(0, 2), () -> 1L);
	}

	public BookBorrowingRequestDto getBookBorrowingRequestDto() {
		BookBorrowingRequestDto req = new BookBorrowingRequestDto();
		req.setBookStatusId(0L);
		req.setPhone(1231231231L);
		req.setBorrowingDate(new Date(System.currentTimeMillis()));
		return req;
	}

}