package org.one.gene.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelCreateUtil {
	
	private String srcXlsPath = "";// // excel模板路径  
    private String desXlsPath = "";  
    private String sheetName = "";  
    private POIFSFileSystem fs = null;
	private Workbook wb = null;
	private Sheet sheet = null;
	private CellStyle cellStyle = null;

	public ExcelCreateUtil() {
	}
	
	/**
	 * @param wb
	 * @param sheet
	 */
	public ExcelCreateUtil(Workbook wb, HSSFSheet sheet) {
		super();
		this.wb = wb;
		this.sheet = sheet;
	}

	 /** 
	 * 设置excel模板路径 
	 * @param srcXlsPath 
	 */  
	public void setSrcPath(String srcXlsPath) {  
	    this.srcXlsPath = srcXlsPath;  
	}  

	/** 
	 * 设置要生成excel文件路径 
	 * @param desXlsPath 
	 */  
	public void setDesPath(String desXlsPath) {  
	    this.desXlsPath = desXlsPath;  
	}  

	/** 
	 * 设置模板中哪个Sheet列 
	 * @param sheetName 
	 */  
	public void setSheetName(String sheetName) {  
	    this.sheetName = sheetName;  
	}  

	/** 
	 * 获取所读取excel模板的对象 
	 */  
	public void getSheetBySheetName() {  
	    try {  
	        File fi = new File(srcXlsPath);  
	        if(!fi.exists()){  
	            System.out.println("模板文件:"+srcXlsPath+"不存在!");  
	            return;  
	        }  
	        
	        
	        if(srcXlsPath.indexOf(".xlsx")!=-1) {
	        	wb = new XSSFWorkbook(new FileInputStream(fi));
	        	sheet = (XSSFSheet) wb.getSheet(sheetName); 
	        	cellStyle = wb.createCellStyle();
			}else {
				fs = new POIFSFileSystem(new FileInputStream(fi));  
				wb = new HSSFWorkbook(fs);
				sheet = (HSSFSheet) wb.getSheet(sheetName); 
				cellStyle = wb.createCellStyle();
			}
	        
	        
	    } catch (FileNotFoundException e) {  
	        e.printStackTrace();  
	    } catch (IOException e) {  
	        e.printStackTrace();  
	    }  
	} 
	
	/**
	 * @return the sheet
	 */
	public Sheet getSheet() {
		return sheet;
	}

	/**
	 * @param sheet the sheet to set
	 */
	public void setSheet(Sheet sheet) {
		this.sheet = sheet;
	}

	/**
	 * @return the wb
	 */
	public Workbook getWb() {
		return wb;
	}

	/**
	 * @param wb the wb to set
	 */
	public void setWb(Workbook wb) {
		this.wb = wb;
	}


	public CellStyle getCellStyle() {
		return cellStyle;
	}

	public void setCellStyle(CellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}

	/**
	 * 输入EXCEL文件
	 * 
	 * @param fileName 文件名
	 */
	public void outputExcel() {
		FileOutputStream fos = null;
		try {
			File file = new File(desXlsPath);
			fos = new FileOutputStream(file);
			wb.write(fos);
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param fileNames 被打包的文件list
	 * @param strFilePath 文件输出路径
	 * @param zipFileName zip包名称
	 * @throws Exception
	 */
	public void outputExcelZip(List<String> fileNames,String strFilePath,String zipFileName) throws Exception {
		//打包
		ZipOutputStream zipWrit = new ZipOutputStream(new FileOutputStream(strFilePath+zipFileName));
		for (String fileName : fileNames) {
			File file = new File(strFilePath +fileName);
			FileInputStream fileIn = new FileInputStream(file);
			zipWrit.putNextEntry(new ZipEntry(file.getName()));
			byte[] bytes = new byte[1204];
			while (fileIn.read(bytes) > 0) {
				zipWrit.write(bytes);
			}
			zipWrit.closeEntry();
			fileIn.close();
			file.delete();
		}
		zipWrit.flush();
		zipWrit.close();
	}
}
