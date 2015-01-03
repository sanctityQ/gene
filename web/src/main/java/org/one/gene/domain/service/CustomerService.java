package org.one.gene.domain.service;

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
		customerRepository.save(customer);
	}
}
