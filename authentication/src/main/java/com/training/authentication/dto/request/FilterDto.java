package com.training.authentication.dto.request;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.training.authentication.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


public class FilterDto implements Specification<User> {
	@JsonProperty
	private String firstName;
	@JsonProperty
	private String lastName;
	@JsonProperty
	private String phoneNumber;
	@JsonProperty
	private int pageNumber;
	@JsonProperty
	private int setSize;

	
	public String getFirstName() {
		return firstName;
	}


	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}


	public void setLastName(String lastName) {
		this.lastName = lastName;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public int getPageNumber() {
		return pageNumber;
	}


	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}


	public int getSetSize() {
		return setSize;
	}


	public void setSetSize(int setSize) {
		this.setSize = setSize;
	}

	public FilterDto() {
		super();
	}

	@Override
	public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> pred = new ArrayList<>();
		if (getFirstName() != null && !getFirstName().trim().equalsIgnoreCase("")) {
			pred.add(criteriaBuilder.like(root.get("firstName").as(String.class),
					"%" + getFirstName().trim().toLowerCase() + "%"));

		}
		if (getLastName() != null && !getLastName().trim().equalsIgnoreCase("")) {
					pred.add(criteriaBuilder.like(root.get("lastName").as(String.class),
					"%" + getLastName().trim().toLowerCase() + "%"));

		}
		if (getPhoneNumber() != null & !getPhoneNumber().trim().equalsIgnoreCase("")) {
					pred.add(criteriaBuilder.like(root.get("phoneNumber").as(String.class),
					"%" + getPhoneNumber().trim().toLowerCase() + "%"));

		}
		pred.add(criteriaBuilder.isNull(root.get("deletedAt")));
		return criteriaBuilder.and(pred.toArray(new Predicate[pred.size()]));
	}

}
