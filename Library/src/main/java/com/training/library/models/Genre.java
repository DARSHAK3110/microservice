package com.training.library.models;

import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "genres")
public class Genre {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GENRE_ID")
	private Long genreId;
	@Column(name = "GENRE_NAME")
	private String genreName;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "genre")
	private List<BooksGenre> bookGenre;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "UPLOAD_ID")
	private Upload upload;

	public Genre() {
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

	public List<BooksGenre> getBookGenre() {
		return bookGenre;
	}

	public void setBookGenre(List<BooksGenre> bookGenre) {
		this.bookGenre = bookGenre;
	}

	public Upload getUpload() {
		return upload;
	}

	public void setUpload(Upload upload) {
		this.upload = upload;
	}

	@Override
	public int hashCode() {
		return Objects.hash(bookGenre, genreId, genreName, upload);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Genre other = (Genre) obj;
		return Objects.equals(bookGenre, other.bookGenre) && Objects.equals(genreId, other.genreId)
				&& Objects.equals(genreName, other.genreName) && Objects.equals(upload, other.upload);
	}

}
