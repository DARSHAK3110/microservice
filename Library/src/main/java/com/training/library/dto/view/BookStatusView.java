package com.training.library.dto.view;

import org.springframework.beans.factory.annotation.Value;

public interface BookStatusView {
			Long getBookStatusId();
			@Value("#{target.location.locationId}")
			Long getLocation();
			Boolean getIsAvailable();
}
