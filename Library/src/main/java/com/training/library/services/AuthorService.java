package com.training.library.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.training.library.dto.request.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.models.Author;
import com.training.library.repositories.AuthorRepository;
@Service

public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;
	DataFormatter df = new DataFormatter();
	public ResponseDto saveAuthors(MultipartFile file) throws IOException, CustomExceptionHandler {
		
		if(!file.getContentType().equals("application/vnd.ms-excel") && !file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
			throw new CustomExceptionHandler("Please use .xlsx format!!");

		}
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet authors = workbook.getSheet("authors");
		XSSFRow headerRow = authors.getRow(0);
		int totalCols = headerRow.getPhysicalNumberOfCells();
		if(totalCols>1) {
			throw new CustomExceptionHandler("File syntax not right!!");
		}
		
		for(int row=1;row<=authors.getLastRowNum();row++) {
			Author author = new Author();
				author.setAuthorName(df.formatCellValue(authors.getRow(row).getCell(0)));
			authorRepository.save(author);
		}
		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		return result;
	}
	
	public Author  getAuthor(Long id) {
		return authorRepository.findById(id).get();
	}

}
