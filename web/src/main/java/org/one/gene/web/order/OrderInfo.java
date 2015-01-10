package org.one.gene.web.order;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.springframework.data.domain.Page;

public class OrderInfo {

	private Customer customer;
	private Page<Order> orderPage;
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Page<Order> getOrderPage() {
		return orderPage;
	}
	public void setOrderPage(Page<Order> orderPage) {
		this.orderPage = orderPage;
	}
	
}
