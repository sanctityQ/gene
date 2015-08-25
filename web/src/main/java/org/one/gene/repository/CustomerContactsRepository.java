package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import java.util.List;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.CustomerContacts;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sinosoft.one.data.jade.annotation.SQL;

@Repository
public interface CustomerContactsRepository extends PagingAndSortingRepository<CustomerContacts, Long>,JpaSpecificationExecutor<CustomerContacts>  {
	
	public List<CustomerContacts>  findByCustomer(Customer customer);
	
	@SQL("select * from `customer_contacts` where `name` like :contactsSQL  " +
			"#if(:customerid != '') { and (`customer_id` = :customerid ) }")
	List<CustomerContacts> vagueSeachContacts(@Param("contactsSQL") String contactsSQL,@Param("customerid") String customerid);
	
}

