package com.training.library.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.request.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.models.Author;
import com.training.library.models.Books;
import com.training.library.models.BooksGenres;
import com.training.library.models.Genres;
import com.training.library.repositories.AuthorRepository;
import com.training.library.repositories.BookGenreRepository;
import com.training.library.repositories.BookRepository;
import com.training.library.repositories.GenreRepository;

import liquibase.exception.CustomChangeException;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private BookGenreRepository bookGenreRepository;

	public ResponseDto saveBooks(MultipartFile file) throws IOException, CustomExceptionHandler, URISyntaxException {
		DataFormatter df = new DataFormatter();
		if (!file.getContentType().equals("application/vnd.ms-excel")
				&& !file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return null;
		}
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet books = workbook.getSheet("books");
		XSSFRow headerRow = books.getRow(0);
		int totalCols = headerRow.getPhysicalNumberOfCells();

		XSSFWorkbook errorWorkbook = new XSSFWorkbook();

		XSSFSheet bookErrorSheet = errorWorkbook.createSheet("bookErrorSheet");
		XSSFRow header = bookErrorSheet.createRow(0);
		header.createCell(0).setCellValue(df.formatCellValue(headerRow.getCell(0)));
		header.createCell(1).setCellValue(df.formatCellValue(headerRow.getCell(1)));
		header.createCell(2).setCellValue(df.formatCellValue(headerRow.getCell(2)));
		header.createCell(3).setCellValue(df.formatCellValue(headerRow.getCell(3)));
		header.createCell(4).setCellValue(df.formatCellValue(headerRow.getCell(4)));
		header.createCell(5).setCellValue(df.formatCellValue(headerRow.getCell(5)));
		int rowcounter = 1;

		List<String> isbnDto = new ArrayList<>();
		for (int row = 1; row <= books.getLastRowNum(); row++) {
			Books book = new Books();
			XSSFRow dataRow = books.getRow(row);
			try {
				for (int i = 0; i < totalCols; i++) {

					String cellHeader = df.formatCellValue(headerRow.getCell(i));
					String cell = df.formatCellValue(dataRow.getCell(i));
					switch (cellHeader) {

					case "TITLE":
						book.setTitle(cell);
						break;
					case "PUBLICATION_DATE":
						book.setPublicationDate(Date.valueOf(cell));
						break;
					case "ISBN":
						book.setIsbn(Long.parseLong(cell));
						break;
					case "TOTAL_COPIES":
						book.setTotalCopies(Long.parseLong(cell));
						break;
					case "AVAILABLE_COPIES":
						book.setAvailableCopies(Long.parseLong(cell));
						break;
					case "AUTHOR_ID":
						Optional<Author> author = authorRepository.findById(Long.parseLong(cell));
						book.setAuthor(author.get());
						break;
					default:
						break;
					}

				}
				bookRepository.save(book);
			} catch (Exception e) {
				XSSFRow errorRow = bookErrorSheet.createRow(rowcounter);
				errorRow.createCell(0).setCellValue(df.formatCellValue(dataRow.getCell(0)));
				errorRow.createCell(1).setCellValue(df.formatCellValue(dataRow.getCell(1)));
				errorRow.createCell(2).setCellValue(df.formatCellValue(dataRow.getCell(2)));
				errorRow.createCell(3).setCellValue(df.formatCellValue(dataRow.getCell(3)));
				errorRow.createCell(4).setCellValue(df.formatCellValue(dataRow.getCell(4)));
				errorRow.createCell(5).setCellValue(df.formatCellValue(dataRow.getCell(5)));
				rowcounter++;
			}
		}

		XSSFSheet book_genre = workbook.getSheet("books_genres");
		XSSFRow headerRowBG = book_genre.getRow(0);

		XSSFSheet bgErrorSheet = errorWorkbook.createSheet("bgErrorSheet");
		XSSFRow headerBG = bgErrorSheet.createRow(0);
		headerBG.createCell(0).setCellValue(df.formatCellValue(headerRowBG.getCell(0)));
		headerBG.createCell(1).setCellValue(df.formatCellValue(headerRowBG.getCell(1)));
		int rowcounterBG = 1;

		for (int row = 1; row <= book_genre.getLastRowNum(); row++) {
			XSSFRow dataRowBG = book_genre.getRow(row);
			BooksGenres bg = new BooksGenres();
			try {
				Books book = bookRepository.findByIsbn(Long.parseLong(df.formatCellValue(dataRowBG.getCell(0)))).get();
				bg.setBook(book);
				Genres genre = genreRepository.findByGenreName(df.formatCellValue(dataRowBG.getCell(1))).get();
				bg.setGenre(genre);
				bookGenreRepository.save(bg);

			} catch (Exception e) {
				XSSFRow errorRowBG = bgErrorSheet.createRow(rowcounterBG);
				errorRowBG.createCell(0).setCellValue(df.formatCellValue(dataRowBG.getCell(0)));
				errorRowBG.createCell(1).setCellValue(df.formatCellValue(dataRowBG.getCell(1)));
				rowcounterBG++;
			}
		}
		
		if (bookErrorSheet.getLastRowNum() > 0 || bgErrorSheet.getLastRowNum() > 0) {
			File currDir = new File(".");
			String path = currDir.getAbsolutePath();
			String fileLocation = path.substring(0, path.length() - 1)
					+ "\\src\\main\\resources\\static\\error\\bookError.xlsx";
			FileOutputStream out = new FileOutputStream(fileLocation);
			errorWorkbook.write(out);
			out.close();
			throw new CustomExceptionHandler((new URI("http://localhost:8091/error/bookError.xlsx")).toString(),"file of unregister books!!");
		}

		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		return result;
	}
}
