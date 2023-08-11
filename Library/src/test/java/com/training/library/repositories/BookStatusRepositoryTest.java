package com.training.library.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.training.library.dto.response.BookStatusResponseDto;
import com.training.library.entity.BookStatus;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookStatusRepositoryTest {

	@Autowired
	private BookStatusRepository repo;
	private Long id;
	private Long bookDetailsId;
	@Autowired
	private EntityGenerator entityGenerator;
	private BookStatus savedBookStatus;

	@Transactional
	@BeforeEach
	void setUp() throws Exception {

		savedBookStatus = repo.save(entityGenerator.getBookStatus());
		id = savedBookStatus.getBookStatusId();
		bookDetailsId = savedBookStatus.getBookDetails().getBookDetailsId();
	}

	@Test
	void testFindByBookStatusIdAndDeletedAtIsNull() {
		Optional<BookStatusResponseDto> result = repo.findByBookStatusIdAndDeletedAtIsNull(id);
		assertThat(result.get().getBookStatusId()).isEqualTo(id);
	}

	@Test
	void testFailFindByBookStatusIdAndDeletedAtIsNull() {
		Optional<BookStatusResponseDto> result = repo.findByBookStatusIdAndDeletedAtIsNull(id);
		assertThat(result.get().getBookStatusId()).isNotEqualTo(0L);
	}

	@Test
	void testDeleteByBookStatusId() {
		repo.deleteByBookStatusId(id);
		Optional<BookStatusResponseDto> result = repo.findByBookStatusIdAndDeletedAtIsNull(id);
		assertThat(result).isEmpty();
	}

	@Test
	void testFindAllByDeletedAtIsNullAndBookDetails_BookDetailsIdPageableLong() {
		Page<BookStatusResponseDto> result = repo
				.findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(PageRequest.of(0, 2), bookDetailsId);
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void testAvailableFindAllByDeletedAtIsNullAndBookDetails_BookDetailsIdPageableLongBoolean() {
		Page<BookStatusResponseDto> result = repo
				.findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(PageRequest.of(0, 2), bookDetailsId, true);
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void testNotAvailableFindAllByDeletedAtIsNullAndBookDetails_BookDetailsIdPageableLongBoolean() {
		Page<BookStatusResponseDto> result = repo
				.findAllByDeletedAtIsNullAndBookDetails_BookDetailsId(PageRequest.of(0, 2), bookDetailsId, false);
		assertThat(result.getContent().size()).isNotPositive();
	}

	@Test
	void findByLocation_LocationIdTest1() {
		Optional<BookStatus> result = repo.findByLocation_LocationId(savedBookStatus.getLocation().getLocationId());
		assertThat(result.get().getLocation().getLocationId()).isEqualTo(savedBookStatus.getLocation().getLocationId());
	}

	@Test
	void failFindByLocation_LocationIdTest2() {
		Optional<BookStatusResponseDto> result = repo.findByBookStatusIdAndDeletedAtIsNull(id);
		assertThat(result.get().getBookStatusId()).isNotEqualTo(0L);
	}

	@Test
	void countByLocation_Shelf_ShelfIdAndDeletedAtIsNullTest1() {
		Long result = repo
				.countByLocation_Shelf_ShelfIdAndDeletedAtIsNull(savedBookStatus.getLocation().getShelf().getShelfId());
		assertThat(result).isEqualTo(1L);
	}

	@Test
	void countByLocation_Shelf_ShelfIdAndDeletedAtIsNullTest2() {

		Long result = repo.countByLocation_Shelf_ShelfIdAndDeletedAtIsNull(0L);
		assertThat(result).isEqualTo(0L);
	}

	@Test
	void countByLocation_Shelf_Section_SectionIdAndDeletedAtIsNullTest1() {
		Long result = repo.countByLocation_Shelf_Section_SectionIdAndDeletedAtIsNull(
				savedBookStatus.getLocation().getShelf().getSection().getSectionId());
		assertThat(result).isEqualTo(1L);
	}

	@Test
	void countByLocation_Shelf_Section_SectionIdAndDeletedAtIsNullTest2() {

		Long result = repo.countByLocation_Shelf_Section_SectionIdAndDeletedAtIsNull(0L);
		assertThat(result).isEqualTo(0L);
	}

	@Test
	void countByLocation_LocationIdAndDeletedAtIsNullTest1() {
		Long result = repo.countByLocation_LocationIdAndDeletedAtIsNull(savedBookStatus.getLocation().getLocationId());
		assertThat(result).isEqualTo(1L);
	}

	@Test
	void countByLocation_LocationIdAndDeletedAtIsNullTest2() {

		Long result = repo.countByLocation_LocationIdAndDeletedAtIsNull(0L);
		assertThat(result).isEqualTo(0L);
	}

	@Test
	void countByLocation_Shelf_Section_Floor_FloorIdAndDeletedAtIsNullTest1() {
		Long result = repo.countByLocation_Shelf_Section_Floor_FloorIdAndDeletedAtIsNull(
				savedBookStatus.getLocation().getShelf().getSection().getFloor().getFloorId());
		assertThat(result).isEqualTo(1L);
	}

	@Test
	void countByLocation_Shelf_Section_Floor_FloorIdAndDeletedAtIsNullTest2() {

		Long result = repo.countByLocation_Shelf_Section_Floor_FloorIdAndDeletedAtIsNull(0L);
		assertThat(result).isEqualTo(0L);
	}

	@Test
	void testFindAllByLocationId() {
		Page<BookStatusResponseDto> result = repo.findAllByLocationId(savedBookStatus.getLocation().getLocationId(),
				PageRequest.of(0, 2));
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void testFindAllByShelfId() {
		Page<BookStatusResponseDto> result = repo
				.findAllByShelfId(savedBookStatus.getLocation().getShelf().getShelfId(), PageRequest.of(0, 2));
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void testFindAllBySectionId() {
		Page<BookStatusResponseDto> result = repo.findAllBySectionId(
				savedBookStatus.getLocation().getShelf().getSection().getSectionId(), PageRequest.of(0, 2));
		assertThat(result.getContent().size()).isPositive();
	}

	@Test
	void testFindAllByFloorId() {
		Page<BookStatusResponseDto> result = repo.findAllByFloorId(
				savedBookStatus.getLocation().getShelf().getSection().getFloor().getFloorId(), PageRequest.of(0, 2));
		assertThat(result.getContent().size()).isPositive();
	}

}
