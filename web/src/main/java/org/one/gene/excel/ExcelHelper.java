package org.one.gene.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;


/**
 * Excel统一POI处理类（针对2003以前和2007以后两种格式的兼容处理）
 * @note	PoiHelper
 */
public abstract class ExcelHelper {
	public static final String SEPARATOR = ",";
	public static final String CONNECTOR = "-";

	/** 获取sheet列表，子类必须实现 */
	public abstract ArrayList<String> getSheetList(String filePath);
	
	/** 读取Excel文件数据 */
	public ArrayList<ArrayList<String>> readExcel(String filePath, int sheetIndex) {
		return readExcel(filePath, sheetIndex, "1-", "1-");
	}
	
	/** 读取Excel文件数据 */
	public ArrayList<ArrayList<String>> readExcel(String filePath, int sheetIndex, String rows) {
		return readExcel(filePath, sheetIndex, rows, "1-");
	}
	
	/** 读取Excel文件数据 */
	public ArrayList<ArrayList<String>> readExcel(String filePath, int sheetIndex, String[] columns) {
		return readExcel(filePath, sheetIndex, "1-", columns);
	}
	
	/** 读取Excel文件数据，子类必须实现 */
	public abstract ArrayList<ArrayList<String>> readExcel(String filePath, int sheetIndex, String rows, String columns);

	/** 读取Excel文件数据 */
	public ArrayList<ArrayList<String>> readExcel(String filePath, int sheetIndex, String rows, String[] columns) {
		int[] cols = getColumnNumber(columns);
		
		return readExcel(filePath, sheetIndex, rows, cols);
	}

	/** 读取Excel文件数据，子类必须实现 */
	public abstract ArrayList<ArrayList<String>> readExcel(String filePath, int sheetIndex, String rows, int[] cols);
	
	/** 读取Excel文件内容 */
	protected ArrayList<ArrayList<String>> readExcel(Sheet sheet, String rows, int[] cols) {
		ArrayList<ArrayList<String>> dataList = new ArrayList<ArrayList<String>> ();
		// 处理行信息，并逐行列块读取数据
		String[] rowList = rows.split(SEPARATOR);
		for (String rowStr : rowList) {
			if (rowStr.contains(CONNECTOR)) {
				String[] rowArr = rowStr.trim().split(CONNECTOR);
				int start = Integer.parseInt(rowArr[0]) - 1;
				int end;
				if (rowArr.length == 1) {
					end = sheet.getLastRowNum();
				} else {
					end = Integer.parseInt(rowArr[1].trim()) - 1;
				}
				dataList.addAll(getRowsValue(sheet, start, end, cols));
			} else {
				dataList.add(getRowValue(sheet, Integer.parseInt(rowStr) - 1, cols));
			}
		}
		return dataList;
	}

	/** 获取连续行、列数据 */
	protected ArrayList<ArrayList<String>> getRowsValue(Sheet sheet, int startRow, int endRow,
			int startCol, int endCol) {
		if (endRow < startRow || endCol < startCol) {
			return null;
		}
		
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		for (int i = startRow; i <= endRow; i++) {
			data.add(getRowValue(sheet, i, startCol, endCol));
		}
		return data;
	}

	/** 获取连续行、不连续列数据 */
	private ArrayList<ArrayList<String>> getRowsValue(Sheet sheet, int startRow, int endRow, int[] cols) {
		if (endRow < startRow) {
			return null;
		}
		
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		for (int i = startRow; i <= endRow; i++) {
			data.add(getRowValue(sheet, i, cols));
		}
		return data;
	}
	
	/** 获取行连续列数据 */
	private ArrayList<String> getRowValue(Sheet sheet, int rowIndex, int startCol, int endCol) {
		if(endCol < startCol) {
			return null;
		}
		
		Row row = sheet.getRow(rowIndex);
		ArrayList<String> rowData = new ArrayList<String>();
		for (int i = startCol; i <= endCol; i++) {
			rowData.add(getCellValue(row, i));
		}
		return rowData;
	}
	
	/** 获取行不连续列数据 */
	private ArrayList<String> getRowValue(Sheet sheet, int rowIndex, int[] cols) {
		Row row = sheet.getRow(rowIndex);
		ArrayList<String> rowData = new ArrayList<String>();
		for (int colIndex : cols) {
			rowData.add(getCellValue(row, colIndex));
		}
		return rowData;
	}
	
	/**
	 * 获取单元格内容
	 * 
	 * @param row
	 * @param column
	 *            a excel column string like 'A', 'C' or "AA".
	 * @return
	 */
	protected String getCellValue(Row row, String column) {
		return getCellValue(row,getColumnNumber(column));
	}

	/**
	 * 获取单元格内容
	 * 
	 * @param row
	 * @param col
	 *            a excel column index from 0 to 65535
	 * @return
	 */
	private String getCellValue(Row row, int col) {
		if (row == null) {
			return "";
		}
		Cell cell = row.getCell(col);
		return getCellValue(cell);
	}

	/**
	 * 获取单元格内容
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}
		String value = "";
		switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC:
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					if (date != null) {
						value = new SimpleDateFormat("yyyy-MM-dd").format(date);
					} else {
						value = "";
					}
				} else {
					value = new DecimalFormat("0").format(cell
							.getNumericCellValue());
				}
				break;
			case HSSFCell.CELL_TYPE_FORMULA:
				// 导入时如果为公式生成的数据直接输出公式
				FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
				evaluator.evaluateFormulaCell(cell);
				org.apache.poi.ss.usermodel.CellValue cellValue = evaluator.evaluate(cell);
				value = String.valueOf((int) cellValue.getNumberValue());
/* 此方式处理不了。				
                try{
					value = String.valueOf(cell.getStringCellValue());
				}catch(IllegalStateException e){
					value = String.valueOf(cell.getNumericCellValue());
				}*/
				break;
			case HSSFCell.CELL_TYPE_STRING:
				value = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				value = "";
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				value = (cell.getBooleanCellValue() == true ? "Y" : "N");
				break;
			default:
				value = "";   
		}
		
		value = value.toString().trim();
		try {
			// This step is used to prevent Integer string being output with
			// '.0'.
			Float.parseFloat(value);
			value=value.replaceAll("\\.0$", "");
			value=value.replaceAll("\\.0+$", "");
			return value;
		} catch (NumberFormatException ex) {
			return value;
		}
	}

	/**
	 * Change excel column letter to integer number
	 * 
	 * @param columns
	 *            column letter of excel file, like A,B,AA,AB
	 * @return
	 */
	private int[] getColumnNumber(String[] columns) {
		int[] cols = new int[columns.length];
		for(int i=0; i<columns.length; i++) {
			cols[i] = getColumnNumber(columns[i]);
		}
		return cols;
	}

	/**
	 * Change excel column letter to integer number
	 * 
	 * @param column
	 *            column letter of excel file, like A,B,AA,AB
	 * @return
	 */
	private int getColumnNumber(String column) {
		int length = column.length();
		short result = 0;
		for (int i = 0; i < length; i++) {
			char letter = column.toUpperCase().charAt(i);
			int value = letter - 'A' + 1;
			result += value * Math.pow(26, length - i - 1);
		}
		return result - 1;
	}

	/**
	 * Change excel column string to integer number array
	 * 
	 * @param sheet
	 *            excel sheet
	 * @param columns
	 *            column letter of excel file, like A,B,AA,AB
	 * @return
	 */
	protected int[] getColumnNumber(Sheet sheet, String columns) {
		// 拆分后的列为动态，采用List暂存
		ArrayList<Integer> result = new ArrayList<Integer> ();
		String[] colList = columns.split(SEPARATOR);
		for(String colStr : colList){
			if(colStr.contains(CONNECTOR)){
				String[] colArr = colStr.trim().split(CONNECTOR);
				int start = Integer.parseInt(colArr[0]) - 1;
				int end;
				if(colArr.length == 1){
					end = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum() - 1;
				}else{
					end = Integer.parseInt(colArr[1].trim()) - 1;
				}
				for(int i=start; i<=end; i++) {
					result.add(i);
				}
			}else{
				result.add(Integer.parseInt(colStr) - 1);
			}
		}
		
		// 将List转换为数组
		int len = result.size();
		int[] cols = new int[len]; 
		for(int i = 0; i<len; i++) {
			cols[i] = result.get(i).intValue();
		}

		return cols;
	}
	
	/**
     * 从Excel文件得到指定sheet页面的二维数组
     * @param file Excel文件
     * @param ignoreRows 忽略的行数，通常为每个sheet的标题行数
     * @param sheetIndex sheet的序号
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String[][] getDataBySheet(File file, int ignoreRows, int sheetIndex)
    throws FileNotFoundException, IOException {
    	ArrayList<Object> result = new ArrayList<Object>();
    	int rowSize = 0;
    	BufferedInputStream in = new BufferedInputStream(new FileInputStream(
    			file));
    	// 打开HSSFWorkbook
    	POIFSFileSystem fs = new POIFSFileSystem(in);
    	HSSFWorkbook wb = new HSSFWorkbook(fs);
    	HSSFCell cell = null;
    	//for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
    		HSSFSheet st = wb.getSheetAt(sheetIndex - 1);
    		System.out.println("Row size is " + st.getLastRowNum());
    		// 第一行为标题，不取
    		for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
    			HSSFRow row = st.getRow(rowIndex);
    			if (row == null) {
    				continue;
    			}
    			int tempRowSize = row.getLastCellNum() + 1;
    			if (tempRowSize > rowSize) {
    				rowSize = tempRowSize;
    			}
    			String[] values = new String[rowSize];
    			Arrays.fill(values, "");
    			boolean hasValue = false;
    			for (int columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
    				String value = "";
    				cell = row.getCell(columnIndex);
    				if (cell != null) {
    					// 注意：一定要设成这个，否则可能会出现乱码
//    					cell.setEncoding(HSSFCell.ENCODING_UTF_16);
    					switch (cell.getCellType()) {
	    					case HSSFCell.CELL_TYPE_NUMERIC:
	    						if (HSSFDateUtil.isCellDateFormatted(cell)) {
	    							Date date = cell.getDateCellValue();
	    							if (date != null) {
	    								value = new SimpleDateFormat("yyyy-MM-dd").format(date);
	    							} else {
	    								value = "";
	    							}
	    						} else {
	    							value = new DecimalFormat("0").format(cell
	    									.getNumericCellValue());
	    						}
	    						break;
	    					case HSSFCell.CELL_TYPE_FORMULA:
	    						// 导入时如果为公式生成的数据直接输出公式
	    						FormulaEvaluator evaluator = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
	    						evaluator.evaluateFormulaCell(cell);
	    						org.apache.poi.ss.usermodel.CellValue cellValue = evaluator.evaluate(cell);
	    						value = String.valueOf((int) cellValue.getNumberValue());
	    						break;
	    					case HSSFCell.CELL_TYPE_STRING:
	    						value = cell.getStringCellValue();
	    						break;
	    					case HSSFCell.CELL_TYPE_BLANK:
	    						break;
	    					case HSSFCell.CELL_TYPE_ERROR:
	    						value = "";
	    						break;
	    					case HSSFCell.CELL_TYPE_BOOLEAN:
	    						value = (cell.getBooleanCellValue() == true ? "Y" : "N");
	    						break;
	    					default:
	    						value = "";   
	    				}
    				}
    				values[columnIndex] = rightTrim(value);
    				hasValue = true;
    			}

    			if (hasValue) {
    				result.add(values);
    			}
    		}
    	//}
    	in.close();
    	String[][] returnArray = new String[result.size()][rowSize];
    	for (int i = 0; i < returnArray.length; i++) {
    		returnArray[i] = (String[]) result.get(i);
    		for (int j = 0; j < returnArray[i].length; j++) {
            	returnArray[i][j] = returnArray[i][j].replaceAll("\n", "").trim();
            }
    	}
    	return returnArray;
    }
    
    public static String rightTrim(String str)
    {
      if (str == null) {
        return "";
      }
      int length = str.length();
      for (int i = length - 1; i >= 0; i--) {
        if (str.charAt(i) != ' ') {
          break;
        }
        length--;
      }
      return str.substring(0, length);
    }
}