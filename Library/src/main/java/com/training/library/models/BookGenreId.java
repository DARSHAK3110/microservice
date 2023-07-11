package com.training.library.models;

import java.util.Objects;

public class BookGenreId {

	private Book book;
	private Genre genre;

	public BookGenreId() {
		super();
	}

	public BookGenreId(Book book, Genre genre) {
		super();
		this.book = book;
		this.genre = genre;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	@Override
	public int hashCode() {
		return Objects.hash(book, genre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BookGenreId other = (BookGenreId) obj;
		return Objects.equals(book, other.book) && Objects.equals(genre, other.genre);
	}

}
