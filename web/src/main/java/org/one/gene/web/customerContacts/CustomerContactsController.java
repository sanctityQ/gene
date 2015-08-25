package org.one.gene.web.customerContacts;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.one.gene.domain.entity.Company;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.CustomerContacts;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.User;
import org.one.gene.domain.service.CustomerService;
import org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.CompanyRepository;
import org.one.gene.repository.CustomerContactsRepository;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Maps;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.annotation.rest.Post;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Json;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Text;

@Path
public class CustomerContactsController {

	@Autowired
    private CustomerService customerService;
	@Autowired
    private CustomerRepository customerRepository;
	@Autowired
    private UserRepository userRepository;
	@Autowired
    private CompanyRepository companyRepository;
	@Autowired
    private CustomerContactsRepository customerContactsRepository;
	
    /**
     * 模糊查询联系人信息
     * */
    @Post("vagueSeachContacts")
	public Reply vagueSeachContacts(@Param("contactsName") String contactsName,
			@Param("customerid") String customerid, Invocation inv) {
    	
        String contactsSQL = "%" + contactsName + "%";
		
        List<CustomerContacts> customerContactss = customerContactsRepository.vagueSeachContacts(contactsSQL, customerid);
    	return Replys.with(customerContactss).as(Json.class);
    }
   
    
}
