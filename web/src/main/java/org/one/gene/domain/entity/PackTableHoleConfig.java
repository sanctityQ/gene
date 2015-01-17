package org.one.gene.domain.entity;

public class PackTableHoleConfig {

    private String holeNo;//孔号
	private int row;//行
	private int column;//列
	private PrimerProduct primerProduct;//生产数据
	
	public String getHoleNo() {
		return holeNo;
	}
	public void setHoleNo(String holeNo) {
		this.holeNo = holeNo;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getColumn() {
		return column;
	}
	public void setColumn(int column) {
		this.column = column;
	}
	public PrimerProduct getPrimerProduct() {
		return primerProduct;
	}
	public void setPrimerProduct(PrimerProduct primerProduct) {
		this.primerProduct = primerProduct;
	}
    
}


