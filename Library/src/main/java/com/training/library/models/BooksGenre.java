package com.training.library.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "books_genres")
@IdClass(BookGenreId.class)
public class BooksGenre {

	@Id
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "BOOK_ID")
	private Book book;

	@Id
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "GENRE_ID")
	private Genre genre;

	public BooksGenre() {
		super();
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

}
