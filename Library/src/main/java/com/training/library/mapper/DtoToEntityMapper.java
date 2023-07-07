package com.training.library.mapper;

import java.util.List;

import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.training.library.dto.request.BookDto;
import com.training.library.models.Author;
import com.training.library.models.Book;
import com.training.library.models.BooksGenre;
import com.training.library.models.Genre;
import com.training.library.services.AuthorService;
import com.training.library.services.BookService;
import com.training.library.services.GenreService;

@Component
public abstract class DtoToEntityMapper {

	@Autowired
	private GenreService genreService;
	@Autowired
	private AuthorService authorService;
	@Autowired
	private BookService bookService;
	
	
	@Named("mapGenresFromIsbn")
	public List<BooksGenre> mapGenresFromIsbn(BookDto bookDto) {
		Book book = bookService.getBook(bookDto.getIsbn());
		if (book != null) {
			book = new Book();
		}
		List<BooksGenre> genres = book.getBookGenre();
		Genre genre = genreService.getGenre(bookDto.getGenreId());
		BooksGenre bg = new BooksGenre();
		bg.setGenre(genre);
		return genres;
	}

	@Named("mapAuthorFromAuthorId")
	 public Author mapAuthorFromAuthorId(Long authorId) {
        return authorService.getAuthor(authorId);
    }
}
