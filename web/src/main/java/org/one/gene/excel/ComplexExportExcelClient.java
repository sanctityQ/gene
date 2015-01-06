package org.one.gene.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import com.alibaba.fastjson.JSON;

public class ComplexExportExcelClient {


	public static void main(String[] args) {

		System.out.println(System.getProperty("user.dir"));
		
		Person person1 = new Person("1","fastjson1",1);
		Person person2 = new Person("2","fastjson2",2);
		List<Person> persons = new ArrayList<Person>();
		persons.add(person1);
		persons.add(person2);
		String jsonString2 = JSON.toJSONString(persons);
		System.out.println("json字符串:"+jsonString2);
		
		/*
		ExcelCreateUtil exportExcel = new ExcelCreateUtil();
		exportExcel.setSrcPath("G:\\genef\\出库单模版.xlsx");
		exportExcel.setDesPath("d:\\出库单.xlsx");
		exportExcel.setSheetName("sheet1");
		exportExcel.getSheetBySheetName();
		Sheet sheet = exportExcel.getSheet();
		
		//在相应的单元格进行赋值  
		Cell cell23 = sheet.getRow(1).getCell(2);  
        cell23.setCellValue("测试1");  
        Cell cell26 = sheet.getRow(1).getCell(5);  
        cell26.setCellValue("测试2");  
        Cell cell33 = sheet.getRow(2).getCell(2);  
        cell33.setCellValue("测试3");  
        Cell cell36 = sheet.getRow(2).getCell(5);  
        cell36.setCellValue("测试4"); 
        Cell cell43 = sheet.getRow(3).getCell(2);  
        cell43.setCellValue("测试5");  
        Cell cell46 = sheet.getRow(3).getCell(5);  
        cell46.setCellValue("测试6");  
        Cell cell53 = sheet.getRow(4).getCell(2);  
        cell53.setCellValue("测试7"); 
        Cell cell56 = sheet.getRow(4).getCell(5);  
        cell56.setCellValue("测试8"); 
        Cell cell63 = sheet.getRow(5).getCell(2);  
        cell63.setCellValue("测试9");  

		exportExcel.outputExcel();*/

	}
	
}
