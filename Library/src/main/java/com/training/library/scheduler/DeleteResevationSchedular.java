package com.training.library.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.training.library.services.BookReservationService;

@Component
public class DeleteResevationSchedular {

	@Autowired
	private BookReservationService bookReservationService;
	
	@Scheduled(cron = "0 0 0 * * *")
		public void deleteReservationSchedulerMethod() {
			bookReservationService.deleteByReservationDate();
		}
}
