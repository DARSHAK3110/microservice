package com.training.library.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "book_details")
public class BookDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long bookDetailsId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bookDetails")
	private List<BookStatus> bookStatus = new ArrayList<>();

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "isbn", unique = true, nullable = false)
	private Long isbn;

	@Column(name = "total_copies", nullable = false)
	private Long totalCopies = 1L;

	@Column(name = "available_copies", nullable = false)
	private Long availableCopies = 1L;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bookDetails")
	private List<BookReservation> bookReservation;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bookDetails")
	private List<Favourite> favourite = new ArrayList<>();

	@ManyToOne
	private Author author;

	@ManyToOne
	private Upload upload;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at")
	private Date deletedAt;

	public Long getBookDetailsId() {
		return bookDetailsId;
	}

	public void setBookDetailsId(Long bookDetailsId) {
		this.bookDetailsId = bookDetailsId;
	}

	public List<BookStatus> getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(List<BookStatus> bookStatus) {
		this.bookStatus = bookStatus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public List<BookReservation> getBookReservation() {
		return bookReservation;
	}

	public void setBookReservation(List<BookReservation> bookReservation) {
		this.bookReservation = bookReservation;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getDeletedAt() {
		return deletedAt;
	}

	public void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Upload getUpload() {
		return upload;
	}

	public void setUpload(Upload upload) {
		this.upload = upload;
	}

	public BookDetails() {
		super();
	}

	public List<Favourite> getFavourite() {
		return favourite;
	}

	public void setFavourite(List<Favourite> favourite) {
		this.favourite = favourite;
	}

	public BookStatus addBookStatus(BookStatus bs) {
		bs.setBookDetails(this);
		getBookStatus().add(bs);
		return bs;
	}

	public void addAllBookStatus(List<BookStatus> resultBSList) {
		List<BookStatus> bs = getBookStatus();
		List<BookStatus> result = resultBSList.stream().map(res->addBookStatus(res)).collect(Collectors.toList());
		bs.addAll(result);
	}

}
