package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import java.util.List;

import org.one.gene.domain.entity.CustomerContacts;
import org.one.gene.domain.entity.CustomerPrice;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sinosoft.one.data.jade.annotation.SQL;

@Repository
public interface CustomerPriceRepository extends PagingAndSortingRepository<CustomerContacts, Long>,JpaSpecificationExecutor<CustomerContacts>  {
	
	@SQL("delete from `customer_price` where `customer_id` = :customerid ")
	public void deleteByCustomerId(@Param("customerid") long customerid);
	
	
	@SQL("select * from `customer_price` where `customer_id` = :customerid ")
	public List<CustomerPrice> selectByCustomerId(@Param("customerid") long customerid);
	
}

