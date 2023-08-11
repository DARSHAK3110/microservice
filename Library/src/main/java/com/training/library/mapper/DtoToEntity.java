package com.training.library.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.training.library.dto.request.BookDto;
import com.training.library.entity.BookStatus;

@Mapper(componentModel = "spring", uses= {ToEntityImpl.class})
public interface DtoToEntity {
	
//	List<Author> authorDtoToAuthor(List<AuthorDto> dto);
//	List<Genre> genreDtoToGenre(List<GenreDto> genresDto);
//	List<Member> memberDtoToMember(List<MemberDto> membersDto);
//	List<Librarian> librariansDtoToLibrarians(List<LibrariranDto> librariansDto);
//	@Mapping(target = "isbn", ignore = true)
//	List<BookDetails> toBooks(List<BookDto> bookDTO);
//	List<Reservation> toReservations(List<ReservationDto> reservationDto);
//	List<Borrowing> toBorrowings(List<BorrowingDto> borrowingDto);
	List<BookStatus> toBookStatus(List<BookDto> booksDto);
}
    