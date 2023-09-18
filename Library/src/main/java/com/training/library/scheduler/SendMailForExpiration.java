package com.training.library.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.training.library.services.BookBorrowingService;
import com.training.library.services.BookReservationService;

@Component
public class SendMailForExpiration {


	@Autowired
	private BookBorrowingService bookBorrowingService;
	
	@Scheduled(cron = "0 0 0 * * *")
		public void sendMailBookBorrowingSchedulerMethod() {
		bookBorrowingService.sendMailForRememberBeforeExpiration();
		bookBorrowingService.sendMailForRememberAfterExpiration();
		}
}
