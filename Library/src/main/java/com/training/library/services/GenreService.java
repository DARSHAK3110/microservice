package com.training.library.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.dto.request.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.models.Genres;
import com.training.library.repositories.GenreRepository;

@Service
public class GenreService {

	@Autowired
	private GenreRepository genreRepository;
	public ResponseDto saveGenres(MultipartFile file) throws IOException, CustomExceptionHandler {
		DataFormatter df = new DataFormatter();
		if(!file.getContentType().equals("application/vnd.ms-excel") && !file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
			throw new CustomExceptionHandler("Please use .xlsx format!!");
		}
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet genres = workbook.getSheet("genres");
		XSSFRow headerRow = genres.getRow(0);
		int totalCols = headerRow.getPhysicalNumberOfCells();
		if(totalCols>1) {
			throw new CustomExceptionHandler("File syntax not right!!");
		}
		
		
		for(int row=1;row<=genres.getLastRowNum();row++) {
			Genres author = new Genres();
				author.setGenreName(df.formatCellValue(genres.getRow(row).getCell(0)));
			genreRepository.save(author);
		}
		
		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		return result;
	}

}
