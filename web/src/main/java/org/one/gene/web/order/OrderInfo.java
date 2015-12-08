package org.one.gene.web.order;

import java.math.BigDecimal;
import java.util.Date;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;

public class OrderInfo {

	//订单导入属性
	private Customer customer;
	private Order order;
	
	//订单信息列表属性
	private String orderNo;
	private String outOrderNO;
	private String productNo;//生产编号
	private BigDecimal odTotal;
	private BigDecimal OdTB;
	private String customerName;
	private String productNoMinToMax;
	private BigDecimal tbnTotal;
	private String status;
	private Date createTime;
	private Date modifyTime;
    private String contactsName;
    private String boardNo;//板号
	private String midi;//修饰
    private String mw;//分子量
    private BigDecimal tbn;//碱基数
    private String comTbn;//复合碱基
    private String deliveryRemark;//发货备注
    

	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOutOrderNO() {
		return outOrderNO;
	}
	public void setOutOrderNO(String outOrderNO) {
		this.outOrderNO = outOrderNO;
	}
	public String getProductNo() {
		return productNo;
	}
	public void setProductNo(String productNo) {
		this.productNo = productNo;
	}
	public BigDecimal getOdTotal() {
		return odTotal;
	}
	public void setOdTotal(BigDecimal odTotal) {
		this.odTotal = odTotal;
	}
	public BigDecimal getOdTB() {
		return OdTB;
	}
	public void setOdTB(BigDecimal odTB) {
		OdTB = odTB;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getProductNoMinToMax() {
		return productNoMinToMax;
	}
	public void setProductNoMinToMax(String productNoMinToMax) {
		this.productNoMinToMax = productNoMinToMax;
	}
	public BigDecimal getTbnTotal() {
		return tbnTotal;
	}
	public void setTbnTotal(BigDecimal tbnTotal) {
		this.tbnTotal = tbnTotal;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 打印列表属性
	*/
	//客户电话
	private String customerPhoneNm;
	//业务员
	private String handlerCode;
	//单据编号
	private String makingNo;
	//制单人（当前系统操作人员）
	private String operatorCode;
	//联系人（客户管理中的联系人）
	private String linkName;
	//单位
	private String unit;
	//制单日期
	private Date makingDate;
	//商品编码
	private String commodityCode;
	//货物名称
	private String commodityName;
	
	
	public String getCustomerPhoneNm() {
		return customerPhoneNm;
	}
	public void setCustomerPhoneNm(String customerPhoneNm) {
		this.customerPhoneNm = customerPhoneNm;
	}
	public String getHandlerCode() {
		return handlerCode;
	}
	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}
	public String getMakingNo() {
		return makingNo;
	}
	public void setMakingNo(String makingNo) {
		this.makingNo = makingNo;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public String getLinkName() {
		return linkName;
	}
	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Date getMakingDate() {
		return makingDate;
	}
	public void setMakingDate(Date makingDate) {
		this.makingDate = makingDate;
	}
	public String getCommodityCode() {
		return commodityCode;
	}
	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}
	public String getCommodityName() {
		return commodityName;
	}
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	public String getContactsName() {
		return contactsName;
	}
	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}
	
    public String getBoardNo() {
		return boardNo;
	}
	public void setBoardNo(String boardNo) {
		this.boardNo = boardNo;
	}
	public String getMidi() {
		return midi;
	}
	public void setMidi(String midi) {
		this.midi = midi;
	}
	public String getMw() {
		return mw;
	}
	public void setMw(String mw) {
		this.mw = mw;
	}
	public BigDecimal getTbn() {
		return tbn;
	}
	public void setTbn(BigDecimal tbn) {
		this.tbn = tbn;
	}
	
	
	public String getComTbn() {
		return comTbn;
	}
	public void setComTbn(String comTbn) {
		this.comTbn = comTbn;
	}
	public String getDeliveryRemark() {
		return deliveryRemark;
	}
	public void setDeliveryRemark(String deliveryRemark) {
		this.deliveryRemark = deliveryRemark;
	}
	
}
