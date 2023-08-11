package com.training.library.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "upload")
public class Upload {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long uploadId;

	@ManyToOne
	private User user;

	@Column(name = "file_name")
	private String fileName;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at", nullable = false)
	private Date createdAt;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "upload", orphanRemoval = true)
	private List<BookDetails> bookDetails = new ArrayList<>();

	public Long getUploadId() {
		return uploadId;
	}

	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public List<BookDetails> getBookDetails() {
		return bookDetails;
	}

	public void setBookDetails(List<BookDetails> bookDetails) {
		this.bookDetails = bookDetails;
	}

	public Upload() {
		super();
	}

	public void addBookDetails(BookDetails bd) {
		bd.setUpload(this);
		this.bookDetails.add(bd);
	}
	
	public void addAllBookDetails(List<BookDetails> bsList) {
		bsList.forEach(bd->{
			bd.setUpload(this);
			this.bookDetails.add(bd);
	});
		
	}
}

