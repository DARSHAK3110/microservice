package com.training.library.mapper;

import org.springframework.stereotype.Component;

@Component
public abstract class DtoToEntityMapper {

//	@Autowired
//	private GenreService genreService;
//	@Autowired
//	private AuthorService authorService;
//	@Autowired
//	private BookService bookService;
//	
//	
//	@Named("mapGenresFromIsbn")
//	public List<BooksGenre> mapGenresFromIsbn(BookDto bookDto) {
//		Book book = bookService.getBook(bookDto.getIsbn());
//		if (book != null) {
//			book = new Book();
//		}
//		List<BooksGenre> genres = book.getBookGenre();
//		Genre genre = genreService.getGenre(bookDto.getGenreId());
//		BooksGenre bg = new BooksGenre();
//		bg.setGenre(genre);
//		return genres;
//	}
//
//	@Named("mapAuthorFromAuthorId")
//	 public Author mapAuthorFromAuthorId(Long authorId) {
//        return authorService.getAuthor(authorId);
//    }
}
