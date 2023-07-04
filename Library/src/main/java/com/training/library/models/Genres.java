package com.training.library.models;

import java.util.List;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Genres {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GENRE_ID")
	private Long genreId;
	@Column(name = "GENRE_NAME")
	private String genreName;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "genre", fetch = FetchType.EAGER)
	private List<BooksGenres> bookGenre;

	public Genres() {
		super();
	}

	public Long getGenreId() {
		return genreId;
	}

	public void setGenreId(Long genreId) {
		this.genreId = genreId;
	}

	public String getGenreName() {
		return genreName;
	}

	public void setGenreName(String genreName) {
		this.genreName = genreName;
	}

	public List<BooksGenres> getBookGenre() {
		return bookGenre;
	}

	public void setBookGenre(List<BooksGenres> bookGenre) {
		this.bookGenre = bookGenre;
	}

}
