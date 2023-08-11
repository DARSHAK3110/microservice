package com.training.library.entity;

public class CartId {
	private User user;
	private BookDetails bookDetails;

	public CartId(User user, BookDetails bookDetails) {
		super();
		this.user = user;
		this.bookDetails = bookDetails;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public BookDetails getBookDetails() {
		return bookDetails;
	}

	public void setBookDetails(BookDetails bookDetails) {
		this.bookDetails = bookDetails;
	}

	public CartId() {
		super();
	}

}
