package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import java.util.List;

import org.one.gene.domain.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sinosoft.one.data.jade.annotation.SQL;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long>,JpaSpecificationExecutor<Customer>  {
	
	public Customer findByCode(String customerCode);
	
	public Customer findByName(String customerName);
	
	@SQL("select * from `customer` where (`code` like :customerSQL or `name` like :customerSQL) " +
			"#if(:comCodeSQL != '') { and (`com_code` = :comCodeSQL or customer_flag = '0') }")
	List<Customer> vagueSeachCustomer(@Param("customerSQL") String customerSQL,@Param("comCodeSQL") String comCodeSQL);
	
	@SQL("select * from `customer` where ( customer_flag = '2' " +
			"#if(:comCodeSQL != '') { and `com_code` = :comCodeSQL  } ) or customer_flag = '0' ")
	List<Customer> vagueSeachCustomerZhiJie(@Param("comCodeSQL") String comCodeSQL);
	
	@SQL("select * from `customer` where `unit` like :unitSQL")
	List<Customer> vagueSeachUnit(@Param("unitSQL") String unitSQL);
	
	@SQL("select * from `customer` where `customer_Flag` ='0'")
	List<Customer> seachHaveZiXi();
	
	@SQL("select * from `customer` c "
			+ " where c.`com_code` = :comCode "
			+ "#if(:customerName != '') { and c.`name` = :customerName } "
			+ "#if(:customerFlag != '') { and c.`customer_flag` = :customerFlag } "
			+ "#if(:handlerName != '') { and c.`handler_name` = :handlerName } "
			+ "#if(:contactName != '') { and exists ( select * from `customer_contacts` d where c.`id`= d.`customer_id` and d.`name` = :contactName  ) } " 
			+ " ")
	Page<Customer> queryCustomers(
			@Param("customerName") String customerName,
			@Param("customerFlag") String customerFlag, 
			@Param("handlerName") String handlerName,
			@Param("contactName") String contactName,
			@Param("comCode") String comCode,
			Pageable pageable);
	
	
}

