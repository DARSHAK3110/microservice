package com.training.library.models;

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
import jakarta.persistence.OneToMany;

@Entity
public class Members {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MEMBER_ID")
	private Long memberId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "PHONE")
	private Long phone;

	@Column(name = "ADDRESS")
	private String address;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "member", fetch = FetchType.EAGER)
	private List<Borrowings> borrowings;

	@Fetch(value = FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "member", fetch = FetchType.EAGER)
	private List<Reservations> reservations;

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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
		return "Members [memberId=" + memberId + ", name=" + name + ", email=" + email + ", phone=" + phone
				+ ", address=" + address + ", borrowings=" + borrowings + ", reservations=" + reservations + "]";
	}

	
}
