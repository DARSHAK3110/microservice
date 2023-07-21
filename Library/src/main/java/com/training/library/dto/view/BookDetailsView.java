package com.training.library.dto.view;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;

public interface BookDetailsView {
	Long getBookDetailsId();
	String getTitle();
	Long getIsbn();
	@Value("#{target.author.authorName}")
	String getAuthor();
	Long getTotalCopies();
	Long getAvailableCopies();
	List<BookStatusView> getBookStatus();
	

}
