package com.training.library.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class BooksGenres {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BOOK_GENRE_ID")
	private Long bookGenreId;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "BOOK_ID")
	private Books book;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinColumn(name = "GENRE_ID")
	private Genres genre;

	public BooksGenres() {
		super();
	}

	public Long getBookGenreId() {
		return bookGenreId;
	}

	public void setBookGenreId(Long bookGenreId) {
		this.bookGenreId = bookGenreId;
	}

	public Books getBook() {
		return book;
	}

	public void setBook(Books book) {
		this.book = book;
	}

	public Genres getGenre() {
		return genre;
	}

	public void setGenre(Genres genre) {
		this.genre = genre;
	}

	@Override
	public String toString() {
		return "BooksGenres [bookGenreId=" + bookGenreId + ", book=" + book + ", genre=" + genre + "]";
	}

}
