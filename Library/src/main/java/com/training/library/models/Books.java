package com.training.library.models;

import java.util.Date;
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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Books {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "BOOK_ID")
	private Long bookId;

	@Column(name = "TITLE")
	private String title;

	@Column(name = "PUBLICATION_DATE")
	private Date publicationDate;

	@OneToOne
	@JoinColumn(name = "AUTHOR_ID")
	private Author author;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "book", fetch = FetchType.EAGER)
	private List<BooksGenres> bookGenre;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "book", fetch = FetchType.EAGER)
	private List<Borrowings> borrowings;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "book", fetch = FetchType.EAGER)
	private List<Reservations> reservations;

	@Column(name = "ISBN")
	private Long isbn;

	@Column(name = "TOTAL_COPIES")
	private Long totalCopies;

	@Column(name = "AVAILABLE_COPIES")
	private Long availableCopies;

	public Books() {
		super();
	}

	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
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

	public List<BooksGenres> getBookGenre() {
		return bookGenre;
	}

	public void setBookGenre(List<BooksGenres> bookGenre) {
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

	public Long getAvailableCopies() {
		return availableCopies;
	}

	public void setAvailableCopies(Long availableCopies) {
		this.availableCopies = availableCopies;
	}

	public List<Borrowings> getBorrowings() {
		return borrowings;
	}

	public void setBorrowings(List<Borrowings> borrowings) {
		this.borrowings = borrowings;
	}

	public List<Reservations> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservations> reservations) {
		this.reservations = reservations;
	}

	@Override
	public String toString() {
		return "Books [bookId=" + bookId + ", title=" + title + ", publicationDate=" + publicationDate + ", author="
				+ author + ", bookGenre=" + bookGenre + ", borrowings=" + borrowings + ", reservations=" + reservations
				+ ", isbn=" + isbn + ", totalCopies=" + totalCopies + ", availableCopies=" + availableCopies + "]";
	}
	

}
