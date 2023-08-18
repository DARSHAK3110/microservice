package com.training.library.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "`user`")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long userId;

	@Column(name = "phone", unique = true)
	private Long phone;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Upload> upload;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Author> author;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Floor> floor;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Section> section;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Shelf> shelf;
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Shelf> location;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<BookReservation> bookReservation;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Cart> cart = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<BookBorrowing> bookBOrrowing;

	@OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL)
	private List<BookBorrowing> borrower;

	@OneToMany(mappedBy = "reserver", cascade = CascadeType.ALL)
	private List<BookReservation> reserver;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public List<Upload> getUpload() {
		return upload;
	}

	public List<Shelf> getLocation() {
		return location;
	}

	public void setLocation(List<Shelf> location) {
		this.location = location;
	}

	public List<BookBorrowing> getBorrower() {
		return borrower;
	}

	public void setBorrower(List<BookBorrowing> borrower) {
		this.borrower = borrower;
	}

	public List<BookReservation> getReserver() {
		return reserver;
	}

	public void setReserver(List<BookReservation> reserver) {
		this.reserver = reserver;
	}

	public void setUpload(List<Upload> upload) {
		this.upload = upload;
	}

	public List<Author> getAuthor() {
		return author;
	}

	public void setAuthor(List<Author> author) {
		this.author = author;
	}

	public List<Floor> getFloor() {
		return floor;
	}

	public void setFloor(List<Floor> floor) {
		this.floor = floor;
	}

	public List<Section> getSection() {
		return section;
	}

	public void setSection(List<Section> section) {
		this.section = section;
	}

	public List<Shelf> getShelf() {
		return shelf;
	}

	public void setShelf(List<Shelf> shelf) {
		this.shelf = shelf;
	}

	public List<BookReservation> getBookReservation() {
		return bookReservation;
	}

	public void setBookReservation(List<BookReservation> bookReservation) {
		this.bookReservation = bookReservation;
	}

	public List<BookBorrowing> getBookBOrrowing() {
		return bookBOrrowing;
	}

	public void setBookBOrrowing(List<BookBorrowing> bookBOrrowing) {
		this.bookBOrrowing = bookBOrrowing;
	}

	public List<Cart> getCart() {
		return cart;
	}

	public void setCart(List<Cart> cart) {
		this.cart = cart;
	}

}
