package com.training.library.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="uploads")
public class Upload {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UPLOAD_ID")
	private Long uploadId;

	@Column(name = "UPLOAD_BY")
	private String uploadName;

	@Column(name = "FILE_NAME")
	private String fileName;

	@CreatedDate
	@Column(name = "UPLOAD_DATE")
	private Date uploadDate;

	@JsonManagedReference
	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "UPLOAD_ID")
	private List<Author> authors;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "UPLOAD_ID")
	private List<Book> books = new ArrayList<>();

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "UPLOAD_ID")
	private List<Borrowing> borrowings;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "UPLOAD_ID")
	private List<Genre> genres;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "UPLOAD_ID")
	private List<Librarian> librarians;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "UPLOAD_ID")
	private List<Member> members;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "UPLOAD_ID")
	private List<Reservation> reservations;

	public Long getUploadId() {
		return uploadId;
	}

	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}

	public String getUploadName() {
		return uploadName;
	}

	public void setUploadName(String uploadName) {
		this.uploadName = uploadName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public Upload() {
		super();
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}

	public List<Book> getBooks() {
		return books;
	}

	public void setBooks(List<Book> books) {
		this.books = books;
	}

	public List<Borrowing> getBorrowings() {
		return borrowings;
	}

	public void setBorrowings(List<Borrowing> borrowings) {
		this.borrowings = borrowings;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public List<Librarian> getLibrarians() {
		return librarians;
	}

	public void setLibrarians(List<Librarian> librarians) {
		this.librarians = librarians;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	@Override
	public int hashCode() {
		return Objects.hash(authors, books, borrowings, fileName, genres, librarians, members, reservations, uploadDate,
				uploadId, uploadName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Upload other = (Upload) obj;
		return Objects.equals(authors, other.authors) && Objects.equals(books, other.books)
				&& Objects.equals(borrowings, other.borrowings) && Objects.equals(fileName, other.fileName)
				&& Objects.equals(genres, other.genres) && Objects.equals(librarians, other.librarians)
				&& Objects.equals(members, other.members) && Objects.equals(reservations, other.reservations)
				&& Objects.equals(uploadDate, other.uploadDate) && Objects.equals(uploadId, other.uploadId)
				&& Objects.equals(uploadName, other.uploadName);
	}

}
