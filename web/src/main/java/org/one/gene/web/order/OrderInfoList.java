package org.one.gene.web.order;

import java.util.Date;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;

/**
 * 订单列表信息
 * @author ThinkPad User
 *
 */
public class OrderInfoList {

	//订单列表:订单号、客户姓名、生产编号（头尾）、碱基总数、状态、导入时间，修改时间
	private String orderNo;
	private String customerName;
	private String productNoMinToMax;
	private String tbnTotal;
	private String status;
	private Date createTime;
	private Date modifyTime;
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
	//制单日期
	private Date makingDate;
	//商品编码
	private String commodityCode;
	//货物名称
	private String commodityName;
	
	private Customer customer;
	private Order order;
	
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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
	public String getTbnTotal() {
		return tbnTotal;
	}
	public void setTbnTotal(String tbnTotal) {
		this.tbnTotal = tbnTotal;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
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
	
}
