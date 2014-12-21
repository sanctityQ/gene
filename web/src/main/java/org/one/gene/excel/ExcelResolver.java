package org.one.gene.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

@Component
public class ExcelResolver {
	
	/**
	 * 获取制定sheet页制定行数据
	 * @param filePath
	 * @param sheetIndex
	 * @param rows
	 */
	public ArrayList<ArrayList<String>> ReadExcel(String filePath, int sheetIndex, String rows) {
		ExcelHelper helper = getPoiExcelHelper(filePath);
		
		// 读取excel文件数据
		ArrayList<ArrayList<String>> dataList = helper.readExcel(filePath, sheetIndex, rows);
		
		return dataList;
	}
	
	
	/**
	 * 读取订单制定sheet页制定列数据
	 * @param filePath
	 * @param sheetIndex
	 * @param rows
	 * @param columns
	 */
	public ArrayList<ArrayList<String>> ReadExcel(String filePath, int sheetIndex, String rows, String[] columns) {
		ExcelHelper helper = getPoiExcelHelper(filePath);
		
		// 读取excel文件数据
		ArrayList<ArrayList<String>> dataList = helper.readExcel(filePath, sheetIndex, rows, columns);
		
		return dataList;
	}
	
	public  String[][]  getDataValide(String filePath, int ignoreRows, int sheetIndex) throws FileNotFoundException, IOException{
		
		ExcelHelper helper = getPoiExcelHelper(filePath);
		String[][] date = helper.getDataBySheet(new File(filePath), ignoreRows, sheetIndex);
		
		return date;
	}
	
	/**
	 * 判断Excel版本
	 * @param filePath
	 * @return
	 */
	public ExcelHelper getPoiExcelHelper(String filePath) {
		ExcelHelper helper;
		if(filePath.indexOf(".xlsx")!=-1) {
			helper = new Excel2k7Helper();
		}else {
			helper = new Excel2k3Helper();
		}
		return helper;
	}
		
}
