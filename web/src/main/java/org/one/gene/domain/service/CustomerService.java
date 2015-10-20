package org.one.gene.domain.service;

import java.util.Date;
import java.util.List;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.CustomerContacts;
import org.one.gene.domain.entity.CustomerPrice;
import org.one.gene.repository.CustomerContactsRepository;
import org.one.gene.repository.CustomerPriceRepository;
import org.one.gene.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CustomerService {
	@Autowired
    private CustomerRepository customerRepository;
	
	@Autowired
    private CustomerContactsRepository customerContactsRepository;
	
	@Autowired
    private CustomerPriceRepository customerPriceRepository;
	
	
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
		
		//remove CustomerContactss 中null的对象
		for (int i = customer.getCustomerContactss().size() - 1; i >= 0; i--) {
			CustomerContacts cc = customer.getCustomerContactss().get(i);
			if(cc == null || cc.getName()==null){
				customer.getCustomerContactss().remove(i);
			}else{
				cc.setCustomer(customer);
			}
		}
        
		//删除数据库中需要删除的数据
		if (customer.getId() != null){
			List<CustomerContacts> ccsOld= customerContactsRepository.findByCustomer(customer);
			if (ccsOld != null) {
				for(CustomerContacts ccOld:ccsOld){
					boolean match = false;//数据库中的联系人id没有匹配上页面的值，需要删除
					for(CustomerContacts ccNew:customer.getCustomerContactss()){
						if (ccOld.getId() == ccNew.getId()) {
							match = true;
						}
					}
					
					if (!match) {
						customerContactsRepository.delete(ccOld);
					}
				}
			}
			
			customerPriceRepository.deleteByCustomerId(customer.getId());
			
		}
		
		//remove CustomerPrices 中null的对象
		for (int i = customer.getCustomerPrices().size() - 1; i >= 0; i--) {
			CustomerPrice cp = customer.getCustomerPrices().get(i);
			if(cp == null || cp.getPurifyType()==null){
				customer.getCustomerPrices().remove(i);
			}else{
				cp.setCustomer(customer);
			}
		}
		
		if (customer.getId() != null) {
			customerPriceRepository.deleteByCustomerId(customer.getId());
		}

		customerRepository.save(customer);
	}
}
