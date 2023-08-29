package com.training.library.dto.response;

public class BookDetailsResponseDto {

	private Long bookDetailsId;
	private String title;
	private Long isbn;
	private String author;
	private Long authorId;
	private Long totalCopies;
	private Long availableCopies;
	private boolean addedToFavourite;
	private boolean isReserved;
	private Long totalReserved;
	private Long acceptedReserved;
	public Long getBookDetailsId() {
		return bookDetailsId;
	}

	public void setBookDetailsId(Long bookDetailsId) {
		this.bookDetailsId = bookDetailsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isReserved() {
		return isReserved;
	}

	public void setReserved(boolean isReserved) {
		this.isReserved = isReserved;
	}

	public Long getIsbn() {
		return isbn;
	}

	public void setIsbn(Long isbn) {
		this.isbn = isbn;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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

	public BookDetailsResponseDto() {
		super();
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public boolean isAddedToFavourite() {
		return addedToFavourite;
	}

	public void setAddedToFavourite(boolean addedToFavourite) {
		this.addedToFavourite = addedToFavourite;
	}

	
	public Long getTotalReserved() {
		return totalReserved;
	}

	public void setTotalReserved(Long totalReserved) {
		this.totalReserved = totalReserved;
	}

	public Long getAcceptedReserved() {
		return acceptedReserved;
	}

	public void setAcceptedReserved(Long acceptedReserved) {
		this.acceptedReserved = acceptedReserved;
	}

	public BookDetailsResponseDto(Long bookDetailsId, String title, Long isbn, String author, Long authorId,
			Long totalCopies, Long availableCopies) {
		super();
		this.bookDetailsId = bookDetailsId;
		this.title = title;
		this.isbn = isbn;
		this.author = author;
		this.authorId = authorId;
		this.totalCopies = totalCopies;
		this.availableCopies = availableCopies;
	}

}
