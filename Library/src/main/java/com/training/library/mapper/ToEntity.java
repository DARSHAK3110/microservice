package com.training.library.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.training.library.dto.request.BookDto;
import com.training.library.dto.request.BorrowingDto;
import com.training.library.dto.request.ReservationDto;
import com.training.library.models.Author;
import com.training.library.models.Book;
import com.training.library.models.BooksGenre;
import com.training.library.models.Borrowing;
import com.training.library.models.Genre;
import com.training.library.models.Member;
import com.training.library.models.Reservation;
import com.training.library.services.AuthorService;
import com.training.library.services.BookService;
import com.training.library.services.GenreService;
import com.training.library.services.MemberService;

@Component
public abstract class ToEntity {
	@Autowired
	private MemberService memberService;
	@Autowired
	private BookService bookService;
	@Autowired
	private GenreService genreService;
	@Autowired
	private AuthorService authorService;

	@AfterMapping
	public void setMemberAndBookOnReservationTreeDTO(ReservationDto reservationDto,
			@MappingTarget Reservation reservation) {
		Member m = this.memberService.getMember(reservationDto.getMemberId());
		Book book = this.bookService.getBook(reservationDto.getBookId());
		reservation.setBook(book);
		reservation.setMember(m);
	}

	@AfterMapping
	public void setBookOnBorrowingTreeDTO(BorrowingDto borrowingDto, @MappingTarget Borrowing borrowing) {
		Member m = this.memberService.getMember(borrowingDto.getMemberId());
		Book book = this.bookService.getBook(borrowingDto.getBookId());
		borrowing.setBook(book);
		borrowing.setMember(m);
	}
	
	@BeforeMapping
	public Book setMemberAndBookOnBorrowingTreeDTO(BookDto bookDto) {
		Book book = bookService.getBook(bookDto.getIsbn());
		if (book!= null) {			
			book.addBookGenre(mapGenresFromIsbn(bookDto));
		}
		return book;
	
	}

	@AfterMapping
	public void setAuthorAndGenreOnBookTreeDTO(BookDto bookDto, @MappingTarget Book book) {
		book.addBookGenre(mapGenresFromIsbn(bookDto));
		book.setAuthor(mapAuthorFromAuthorId(bookDto.getAuthorId()));
	}

	public BooksGenre mapGenresFromIsbn(BookDto bookDto) {
		Genre genre = genreService.getGenre(bookDto.getGenreId());
		BooksGenre bg = new BooksGenre();
		bg.setGenre(genre);
		return bg;
	}

	public Author mapAuthorFromAuthorId(Long authorId) {
		return authorService.getAuthor(authorId);
	}
}
