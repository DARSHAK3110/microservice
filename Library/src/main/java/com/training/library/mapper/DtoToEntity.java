package com.training.library.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.training.library.dto.request.AuthorDto;
import com.training.library.dto.request.BookDto;
import com.training.library.dto.request.BorrowingDto;
import com.training.library.dto.request.GenreDto;
import com.training.library.dto.request.LibrariranDto;
import com.training.library.dto.request.MemberDto;
import com.training.library.dto.request.ReservationDto;
import com.training.library.models.Author;
import com.training.library.models.Book;
import com.training.library.models.Borrowing;
import com.training.library.models.Genre;
import com.training.library.models.Librarian;
import com.training.library.models.Member;
import com.training.library.models.Reservation;

@Mapper(componentModel = "spring", uses= {ToEntityImpl.class})
public interface DtoToEntity {
	
	List<Author> authorDtoToAuthor(List<AuthorDto> dto);
	List<Genre> genreDtoToGenre(List<GenreDto> genresDto);
	List<Member> memberDtoToMember(List<MemberDto> membersDto);
	List<Librarian> librariansDtoToLibrarians(List<LibrariranDto> librariansDto);
	@Mapping(target = "isbn", ignore = true)
	List<Book> toBooks(List<BookDto> bookDTO);
	List<Reservation> toReservations(List<ReservationDto> reservationDto);
	List<Borrowing> toBorrowings(List<BorrowingDto> borrowingDto);
}
    