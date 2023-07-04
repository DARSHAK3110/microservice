package com.training.library.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.poi.ss.usermodel.CellCopyPolicy;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.training.library.dto.request.ResponseDto;
import com.training.library.exceptions.CustomExceptionHandler;
import com.training.library.models.Members;
import com.training.library.repositories.MemberRepository;

@Service
public class MemberService {

	@Autowired
	private MemberRepository memberRepository;

	public ResponseDto saveMembers(MultipartFile file) throws CustomExceptionHandler, IOException, URISyntaxException {
		DataFormatter df = new DataFormatter();
		if (!file.getContentType().equals("application/vnd.ms-excel")
				&& !file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			throw new CustomExceptionHandler();
		}
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet members = workbook.getSheet("members");
		XSSFRow headerRow = members.getRow(0);
		int totalCols = headerRow.getPhysicalNumberOfCells();

		XSSFWorkbook errorWorkbook = new XSSFWorkbook();

		XSSFSheet memberErrorSheet = errorWorkbook.createSheet("memberErrorSheet");
		XSSFRow header = memberErrorSheet.createRow(0);
		header.createCell(0).setCellValue(df.formatCellValue(headerRow.getCell(0)));
		header.createCell(1).setCellValue(df.formatCellValue(headerRow.getCell(1)));
		header.createCell(2).setCellValue(df.formatCellValue(headerRow.getCell(2)));
		header.createCell(3).setCellValue(df.formatCellValue(headerRow.getCell(3)));
		int rowcounter = 1;

		for (int row = 1; row <= members.getLastRowNum(); row++) {
			Members member = new Members();
			XSSFRow dataRow = members.getRow(row);
			for (int i = 0; i < totalCols; i++) {

				String cellHeader = df.formatCellValue(headerRow.getCell(i));
				String cell = df.formatCellValue(dataRow.getCell(i));
				switch (cellHeader) {

				case "NAME":
					member.setName(cell);
					break;
				case "EMAIL":
					member.setEmail(cell);
					break;
				case "ADDRESS":
					member.setAddress(cell);
					break;
				case "PHONE":
					member.setPhone(Long.parseLong(cell));
					break;
				default:
					break;
				}

			}
			try {
				memberRepository.save(member);
			} catch (Exception e) {

				XSSFRow errorRow = memberErrorSheet.createRow(rowcounter);

				errorRow.createCell(0).setCellValue(df.formatCellValue(dataRow.getCell(0)));
				errorRow.createCell(1).setCellValue(df.formatCellValue(dataRow.getCell(1)));
				errorRow.createCell(2).setCellValue(df.formatCellValue(dataRow.getCell(2)));
				errorRow.createCell(3).setCellValue(df.formatCellValue(dataRow.getCell(3)));
				rowcounter++;
			}

		}
		if (memberErrorSheet.getLastRowNum() > 0) {
			File currDir = new File(".");
			String path = currDir.getAbsolutePath();
			String fileLocation = path.substring(0, path.length() - 1) + "\\src\\main\\resources\\static\\error\\memberError.xlsx";
			FileOutputStream out = new FileOutputStream(fileLocation);
			errorWorkbook.write(out);
			URI uri = currDir.toURI();
			out.close();

			throw new CustomExceptionHandler((new URI("http://localhost:8091/error/memberError.xlsx")).toString(), "file of unregister members!!");
		}

		ResponseDto result = new ResponseDto();
		result.setMessage("Succesfully done your work!!");
		return result;
	}

}
