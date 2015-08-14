package org.one.gene.domain.service;

import java.util.Date;
import java.util.List;

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
    	String prefix = customer.getPrefix().toUpperCase();//生产编号开头
    	//直接客户使用梓熙的配置
		if ("2".equals(customer.getCustomerFlag())) {
			List<Customer> customers = customerRepository.seachHaveZiXi();
			if (customers != null && customers.size() > 0) {
				Customer customerTemp = (Customer)customers.get(0);
				prefix = customerTemp.getPrefix().toUpperCase();
			}
		}
		customer.setPrefix(prefix);
		customer.setModifyTime(new Date());
		/*customer.getCustomerPrice().setCustomer(customer);
		if(customerOld!=null){
		  customer.getCustomerPrice().setId(customerOld.getCustomerPrice().getId());
		}*/
		customerRepository.save(customer);
	}
}
