package org.one.gene.domain.service;

import java.util.Date;

import org.one.gene.domain.entity.Customer;
import org.one.gene.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomerService {
	@Autowired
    private CustomerRepository customerRepository;
	
	@Transactional(readOnly = false)
	public void save(Customer customer){
		//Customer customerOld = customerRepository.findByCode(customer.getCode());
		//生产编号开头大写
		customer.setPrefix(customer.getPrefix().toUpperCase());
		customer.setModifyTime(new Date());
		/*customer.getCustomerPrice().setCustomer(customer);
		if(customerOld!=null){
		  customer.getCustomerPrice().setId(customerOld.getCustomerPrice().getId());
		}*/
		customerRepository.save(customer);
	}
}
