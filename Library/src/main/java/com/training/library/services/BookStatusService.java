package com.training.library.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.training.library.dto.request.BookStatusRequestDto;
import com.training.library.dto.view.BookStatusView;
import com.training.library.entity.BookDetails;
import com.training.library.entity.BookStatus;
import com.training.library.repositories.BookStatusRepository;

@Service
public class BookStatusService {
	@Autowired
	private BookStatusRepository bookStatusRepository;

	@Autowired
	private LocationService locationService;
	
	public BookStatusView findBookStatus(Long id) {
		Optional<BookStatusView> bookStatus = bookStatusRepository.findByBookStatusIdAndDeletedAtIsNull(id);
		if (bookStatus.isPresent()) {
			return bookStatus.get();
		}
		return null;
	}

	public List<BookStatusView> findAllBookStatus() {
		Optional<List<BookStatusView>> bookStatusList = bookStatusRepository.findAllByDeletedAtIsNull();
		if (bookStatusList.isPresent()) {
			return bookStatusList.get();
		}
		return Collections.emptyList();
	}

	public List<BookStatusView> deleteBookStatus(Long id) {
		bookStatusRepository.deleteByBookStatusId(id);
			return findAllBookStatus();
	}

	public List<BookStatusView> updateBookStatus(Long id, BookStatusRequestDto dto) {
		Optional<BookStatus> bookStatus = bookStatusRepository.findById(dto.getBookStatusId());
		if(bookStatus.isPresent()) {
			BookStatus bs = bookStatus.get();
			bs.setAvailable(dto.getIsAvailable());
			bs.setLocation(locationService.findLocationById(dto.getLocation()));
			bookStatusRepository.save(bs);
		}
		return findAllBookStatus();
	}

	public BookStatus findBookById(Long bookStatusId) {
		Optional<BookStatus> bookStatus= bookStatusRepository.findById(bookStatusId);
		if (bookStatus.isPresent()) {
			return bookStatus.get();
		}
		return null;
	}

}
