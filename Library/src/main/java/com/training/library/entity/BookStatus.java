package com.training.library.entity;

import java.util.Date;
import java.util.List;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "book_status")
public class BookStatus {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long bookStatusId;

	@ManyToOne
	private BookDetails bookDetails;

	@OneToOne(cascade = CascadeType.ALL, optional = true)
	private Location location;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "bookStatus")
	private List<BookBorrowing> bookBorrowing;

	@Column(name = "is_available")
	private boolean isAvailable;

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

	@ManyToOne
	private Upload upload;

	public BookStatus() {
		super();
	}

	public Long getBookStatusId() {
		return bookStatusId;
	}

	public void setBookStatusId(Long bookStatusId) {
		this.bookStatusId = bookStatusId;
	}

	public BookDetails getBookDetails() {
		return bookDetails;
	}

	public void setBookDetails(BookDetails bookDetails) {
		this.bookDetails = bookDetails;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<BookBorrowing> getBookBorrowing() {
		return bookBorrowing;
	}

	public void setBookBorrowing(List<BookBorrowing> bookBorrowing) {
		this.bookBorrowing = bookBorrowing;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
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

	@Override
	public String toString() {
		return "BookStatus [bookStatusId=" + bookStatusId 
				+ ", bookBorrowing=" + bookBorrowing + ", isAvailable=" + isAvailable + ", createdAt=" + createdAt
				+ ", updatedAt=" + updatedAt + ", deletedAt=" + deletedAt + ", upload=" + upload + "]";
	}

}
