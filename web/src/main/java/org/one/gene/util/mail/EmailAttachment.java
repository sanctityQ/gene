package org.one.gene.util.mail;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * �ʼ���������Ĵ���
 * @author Administrator
 *
 */
public class EmailAttachment {
	String filePath="";//Ҫ���͵ĸ���λ��	
	String fileName="";//�������
	private HSSFWorkbook hSSFWorkbook=new HSSFWorkbook();
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public HSSFWorkbook getHSSFWorkbook() {
		return hSSFWorkbook;
	}
	public void setHSSFWorkbook(HSSFWorkbook workbook) {
		hSSFWorkbook = workbook;
	}
	
}
