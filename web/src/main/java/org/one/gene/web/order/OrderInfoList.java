package org.one.gene.web.order;

import java.util.Date;
import java.util.List;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;

/**
 * 订单列表信息
 * @author ThinkPad User
 *
 */
public class OrderInfoList {

	//订单列表:订单号、客户姓名、生产编号（头尾）、碱基总数、状态、导⼊时间，修改时间
	private String orderNo;
	private String customerName;
	private String productNoMinToMax;
	private String tbnTotal;
	private String status;
	private Date createTime;
	private Date modifyTime;
	//客户对象
	private Customer customer;
	//订单对象
	private Order order;
	//生产数据对象集合
	private List<PrimerProduct> primerProducts;
	
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
	public List<PrimerProduct> getPrimerProducts() {
		return primerProducts;
	}
	public void setPrimerProducts(List<PrimerProduct> primerProducts) {
		this.primerProducts = primerProducts;
	}
	
}
