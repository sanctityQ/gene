package org.one.gene.web.delivery;

public class DeliveryInfo {

	//发货清单信息列表属性
	private int rowNo;//行号
	private String deliveryName;//货物名称
	private int countNum;//条数
	private double odTotal;//合成量
	private String measurement;//计量单位
	private int count;//数量
	private double price;//单价
	private String money;//金额
	
	
	public int getRowNo() {
		return rowNo;
	}
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}
	public String getDeliveryName() {
		return deliveryName;
	}
	public void setDeliveryName(String deliveryName) {
		this.deliveryName = deliveryName;
	}
	public int getCountNum() {
		return countNum;
	}
	public void setCountNum(int countNum) {
		this.countNum = countNum;
	}
	public double getOdTotal() {
		return odTotal;
	}
	public void setOdTotal(double odTotal) {
		this.odTotal = odTotal;
	}
	public String getMeasurement() {
		return measurement;
	}
	public void setMeasurement(String measurement) {
		this.measurement = measurement;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	
	
}
