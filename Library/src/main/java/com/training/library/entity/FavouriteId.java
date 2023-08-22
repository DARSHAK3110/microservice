package com.training.library.entity;

public class FavouriteId {
	private User user;
	private BookDetails bookDetails;

	public FavouriteId(User user, BookDetails bookDetails) {
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

	public FavouriteId() {
		super();
	}

}
