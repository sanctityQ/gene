package org.one.gene.domain.entity;

public class PackTableHoleConfig {

    private String holeNo;//孔号
	private int row;//行
	private int column;//列
	private PrimerProduct primerProduct;//生产数据
    private String boardType;//板类型:0-横排 1-竖排.
    private int status;//状态
	private String reason;//失败原因

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
	public String getBoardType() {
		return boardType;
	}
	public void setBoardType(String boardType) {
		this.boardType = boardType;
	}
    public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
}