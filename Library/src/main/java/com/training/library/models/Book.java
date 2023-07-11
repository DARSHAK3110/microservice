package com.training.library.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="books")
public class Book {

	@Id
	@Column(name = "ISBN")
	private Long isbn;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "PUBLICATION_DATE")
	private Date publicationDate;

	@ManyToOne
	@JoinColumn(name = "AUTHOR_ID")
	private Author author;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "UPLOAD_ID")
	private Upload upload;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
	private List<BooksGenre> bookGenre = new ArrayList<>();

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
	private List<Borrowing> borrowings;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "book")
	private List<Reservation> reservations;

	

	@Column(name = "TOTAL_COPIES")
	private Long totalCopies;

	@Column(name = "AVAILABLE_COPIES")
	private Long availCopies;

	public Book() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Upload getUpload() {
		return upload;
	}

	public void setUpload(Upload upload) {
		this.upload = upload;
	}

	public List<BooksGenre> getBookGenre() {
		return bookGenre;
	}

	public void setBookGenre(List<BooksGenre> bookGenre) {
		this.bookGenre = bookGenre;
	}

	public Long getIsbn() {
		return isbn;
	}

	public void setIsbn(Long isbn) {
		this.isbn = isbn;
	}

	public Long getTotalCopies() {
		return totalCopies;
	}

	public void setTotalCopies(Long totalCopies) {
		this.totalCopies = totalCopies;
	}

	public Long getAvailCopies() {
		return availCopies;
	}

	public void setAvailCopies(Long availCopies) {
		this.availCopies = availCopies;
	}

	public List<Borrowing> getBorrowings() {
		return borrowings;
	}

	public void setBorrowings(List<Borrowing> borrowings) {
		this.borrowings = borrowings;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}
	
	public void addBookGenre(BooksGenre booksGenre) {
		booksGenre.setBook(this);
		this.bookGenre.add(booksGenre);
	}

	@Override
	public int hashCode() {
		return Objects.hash(author, availCopies, bookGenre, borrowings, isbn, publicationDate, reservations, title,
				totalCopies, upload);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		return Objects.equals(author, other.author) && Objects.equals(availCopies, other.availCopies)
				&& Objects.equals(bookGenre, other.bookGenre) && Objects.equals(borrowings, other.borrowings)
				&& Objects.equals(isbn, other.isbn) && Objects.equals(publicationDate, other.publicationDate)
				&& Objects.equals(reservations, other.reservations) && Objects.equals(title, other.title)
				&& Objects.equals(totalCopies, other.totalCopies) && Objects.equals(upload, other.upload);
	}

}
