package com.training.library.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.request.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.models.Librarians;
import com.training.library.models.Members;
import com.training.library.repositories.LibrariansRepository;
import com.training.library.repositories.MemberRepository;

@Service
public class LibrarianService {

	@Autowired
	private LibrariansRepository librarianRepository;
	
	public ResponseDto saveLibrarians(MultipartFile file) throws IOException, CustomExceptionHandler, URISyntaxException {
		DataFormatter df = new DataFormatter();
		if (!file.getContentType().equals("application/vnd.ms-excel")
				&& !file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return null;
		}
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet librarians = workbook.getSheet("librarians");
		XSSFRow headerRow = librarians.getRow(0);
		int totalCols = headerRow.getPhysicalNumberOfCells();

		
		XSSFWorkbook errorWorkbook = new XSSFWorkbook();

		XSSFSheet librarianErrorSheet = errorWorkbook.createSheet("librarianErrorSheet");
		XSSFRow header = librarianErrorSheet.createRow(0);
		header.createCell(0).setCellValue(df.formatCellValue(headerRow.getCell(0)));
		header.createCell(1).setCellValue(df.formatCellValue(headerRow.getCell(1)));
		header.createCell(2).setCellValue(df.formatCellValue(headerRow.getCell(2)));
		int rowcounter = 1;
		
		for (int row = 1; row <= librarians.getLastRowNum(); row++) {
			Librarians librarian = new Librarians();
			XSSFRow dataRow = librarians.getRow(row);
			for (int i = 0; i < totalCols; i++) {
				
				String cellHeader = df.formatCellValue(headerRow.getCell(i));
				String cell = df.formatCellValue(dataRow.getCell(i));
				switch (cellHeader) {

				case "NAME":
					librarian.setName(cell);
					break;
				case "EMAIL":
					librarian.setEmail(cell);
					break;
				case "PHONE":
					librarian.setPhone(Long.parseLong(cell));
					break;
				default:
					break;
				}

			}
			try {
				librarianRepository.save(librarian);
			} catch (Exception e) {

				XSSFRow errorRow = librarianErrorSheet.createRow(rowcounter);

				errorRow.createCell(0).setCellValue(df.formatCellValue(dataRow.getCell(0)));
				errorRow.createCell(1).setCellValue(df.formatCellValue(dataRow.getCell(1)));
				errorRow.createCell(2).setCellValue(df.formatCellValue(dataRow.getCell(2)));
				rowcounter++;
			}

		}
		if (librarianErrorSheet.getLastRowNum() > 0) {
			File currDir = new File(".");
			String path = currDir.getAbsolutePath();
			String fileLocation = path.substring(0, path.length() - 1) + "\\src\\main\\resources\\static\\error\\librarianError.xlsx";
			FileOutputStream out = new FileOutputStream(fileLocation);
			errorWorkbook.write(out);
			URI uri = currDir.toURI();
			out.close();
			throw new CustomExceptionHandler((new URI("http://localhost:8091/error/librarianError.xlsx")).toString(), "file of unregister librarians!!");
		}

		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		return result;
	}


	}


