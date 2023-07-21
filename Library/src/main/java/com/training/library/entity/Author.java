package com.training.library.entity;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;

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
@Table(name = "author")
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long authorId;

	@Column(name = "name", nullable = false)
	private String authorName;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@Column(name = "date_of_birth")
	private Date authorDOB;

	
	@OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
	private List<BookDetails> books;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;

	@ManyToOne
	private User user;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deleted_at")
	private Date deletedAt;

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public Date getAuthorDOB() {
		return authorDOB;
	}

	public void setAuthorDOB(Date authorDOB) {
		this.authorDOB = authorDOB;
	}

	public List<BookDetails> getBooks() {
		return books;
	}

	public void setBooks(List<BookDetails> books) {
		this.books = books;
	}

	Date getCreatedAt() {
		return createdAt;
	}

	void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	Date getUpdatedAt() {
		return updatedAt;
	}

	void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	Date getDeletedAt() {
		return deletedAt;
	}

	void setDeletedAt(Date deletedAt) {
		this.deletedAt = deletedAt;
	}

	public Author() {
		super();
	}

}
