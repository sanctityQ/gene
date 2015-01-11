package org.one.gene.web.user;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.User;
import org.springframework.data.domain.Page;

public class UserInfo {

	private Customer customer;
	private Page<User> userPage;
	private String userName;
	private String comCode ;
	
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public Page<User> getUserPage() {
		return userPage;
	}
	public void setUserPage(Page<User> userPage) {
		this.userPage = userPage;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
}
