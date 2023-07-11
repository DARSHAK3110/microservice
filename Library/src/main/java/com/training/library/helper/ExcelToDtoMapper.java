package com.training.library.helper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.training.library.exceptions.CustomExceptionHandler;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class ExcelToDtoMapper {

	private Map<String, Integer> header = new HashMap<>();
	private XSSFWorkbook workbook;
	Field[] fields;

	public ExcelToDtoMapper(MultipartFile file) throws CustomExceptionHandler, IOException {
		if (file != null) {
			String content = file.getContentType();
			if (content != null &&  (!content.equals("application/vnd.ms-excel")
						&& !content.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))) {
					throw new CustomExceptionHandler("Please use .xlsx format!!");
				
			}

			try {
				this.workbook = new XSSFWorkbook(file.getInputStream());
			} catch (Exception e) {
				throw new CustomExceptionHandler(e.getMessage());
			}
		}
	}

	public <T> List<T> mapToList(Class<T> cls) throws ClassNotFoundException, IllegalArgumentException,
			IllegalAccessException, CustomExceptionHandler, InstantiationException, InvocationTargetException,
			NoSuchMethodException, SecurityException, NoSuchFieldException {
		List<T> list = new ArrayList<>();
		Object headObj = Class.forName(cls.getName()).getDeclaredConstructor().newInstance();
		fields = headObj.getClass().getDeclaredFields();
		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFRow headRow = sheet.getRow(0);
		for (int i = 0; i < headRow.getLastCellNum(); i++) {
			String cell = headRow.getCell(i).getStringCellValue();
			this.header.put(cell, i);
		}

		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Object obj = Class.forName(cls.getName()).getDeclaredConstructor().newInstance();
			for (Field field : fields) {
				Field classField = obj.getClass().getDeclaredField(field.getName());
				int index = header.get(field.getName());
				XSSFCell cell = sheet.getRow(i).getCell(index);
				if (cell == null || cell.getCellType() == CellType.BLANK) {
				    continue;
				 }
				cellToField(obj, cell, classField);
			}
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<Object>> validate = validator.validate(obj);
			if (!validate.isEmpty()) {
				throw new ConstraintViolationException(i + "", validate);
			}
			list.add((T) obj);
		}
		return list;
	}

	private void cellToField(Object obj, XSSFCell cell, Field field)
			throws IllegalArgumentException, IllegalAccessException, CustomExceptionHandler {
		DataFormatter df = new DataFormatter();
		Class<?> type = field.getType();
		
		field.setAccessible(true);
		if (type == String.class) {
			field.set(obj, (cell.getStringCellValue()).trim());
		} else if (type == Date.class) {
			field.set(obj, cell.getDateCellValue());
		} else if (type == Boolean.class) {
			field.set(obj, cell.getBooleanCellValue());
		} else if (type == Float.class || type == Double.class) {
			
			field.set(obj, cell.getNumericCellValue());
		} else if(type == Integer.class || type == Long.class) {
			String result = df.formatCellValue(cell);
			
			if(type == Integer.class) {
				int cellVal = Integer.parseInt(result);
				field.set(obj, cellVal);
			}
			else {
				Long cellVal = Long.parseLong(result); 
				field.set(obj, cellVal);
			}
			
		}
		else {
			throw new CustomExceptionHandler("Data has wrong data type");
		}

	}

}
