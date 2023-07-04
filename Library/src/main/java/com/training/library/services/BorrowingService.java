package com.training.library.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.Optional;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.request.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.models.Books;
import com.training.library.models.Borrowings;
import com.training.library.models.Members;
import com.training.library.models.Reservations;
import com.training.library.repositories.BookRepository;
import com.training.library.repositories.BorrowingRepository;
import com.training.library.repositories.MemberRepository;

@Service
public class BorrowingService {

	@Autowired
	private BorrowingRepository borrowingRepository;
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private MemberRepository memberRepository;

	public ResponseDto saveBorrowings(MultipartFile file)
			throws IOException, CustomExceptionHandler, URISyntaxException {
		DataFormatter df = new DataFormatter();
		if (!file.getContentType().equals("application/vnd.ms-excel")
				&& !file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return null;
		}
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet borrowings = workbook.getSheet("borrowings");
		XSSFRow headerRow = borrowings.getRow(0);
		int totalCols = headerRow.getPhysicalNumberOfCells();

		XSSFWorkbook errorWorkbook = new XSSFWorkbook();

		XSSFSheet borrowingErrorSheet = errorWorkbook.createSheet("reservationErrorSheet");
		XSSFRow header = borrowingErrorSheet.createRow(0);
		header.createCell(0).setCellValue(df.formatCellValue(headerRow.getCell(0)));
		header.createCell(1).setCellValue(df.formatCellValue(headerRow.getCell(1)));
		header.createCell(2).setCellValue(df.formatCellValue(headerRow.getCell(2)));
		header.createCell(3).setCellValue(df.formatCellValue(headerRow.getCell(3)));
		header.createCell(4).setCellValue(df.formatCellValue(headerRow.getCell(4)));
		int rowcounter = 1;

		for (int row = 1; row <= borrowings.getLastRowNum(); row++) {

			Borrowings borrowing = new Borrowings();

			XSSFRow dataRow = borrowings.getRow(row);
			try {
				for (int i = 0; i < totalCols; i++) {

					String cellHeader = df.formatCellValue(headerRow.getCell(i));
					String cell = df.formatCellValue(dataRow.getCell(i));
					switch (cellHeader) {

					case "BOOK_ISBN":
						Optional<Books> bookOptional = bookRepository.findByIsbn(Long.parseLong(cell));
						borrowing.setBook(bookOptional.get());
						break;
					case "MEMBER_EMAIL":
						Optional<Members> memberOptional = memberRepository.findByEmail(cell);
						borrowing.setMember(memberOptional.get());
						break;
					case "RETURN_DATE":
						borrowing.setReturnDate(Date.valueOf(cell));
						break;
					case "DUE_DATE":
						borrowing.setDueDate(Date.valueOf(cell));
						break;
					case "BORROWING_DATE":
						borrowing.setBorrowingDate(Date.valueOf(cell));
						break;
					default:
						break;
					}
				}
				borrowingRepository.save(borrowing);
			} catch (Exception e) {
				XSSFRow errorRow = borrowingErrorSheet.createRow(rowcounter);
				errorRow.createCell(0).setCellValue(df.formatCellValue(dataRow.getCell(0)));
				errorRow.createCell(1).setCellValue(df.formatCellValue(dataRow.getCell(1)));
				errorRow.createCell(2).setCellValue(df.formatCellValue(dataRow.getCell(2)));
				errorRow.createCell(3).setCellValue(df.formatCellValue(dataRow.getCell(3)));
				errorRow.createCell(4).setCellValue(df.formatCellValue(dataRow.getCell(4)));
			}
		}

		if (borrowingErrorSheet.getLastRowNum() > 0) {
			File currDir = new File(".");
			String path = currDir.getAbsolutePath();
			String fileLocation = path.substring(0, path.length() - 1)
					+ "\\src\\main\\resources\\static\\error\\borrowingError.xlsx";
			FileOutputStream out = new FileOutputStream(fileLocation);
			errorWorkbook.write(out);
			URI uri = currDir.toURI();
			out.close();
			throw new CustomExceptionHandler((new URI("/error/borrowingError.xlsx")).toString(),
					"file of unregister reservations!!");
		}

		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		return result;
	}
}