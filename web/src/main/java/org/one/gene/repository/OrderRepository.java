package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import com.sinosoft.one.data.jade.annotation.SQL;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.one.gene.domain.entity.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> {

    @SQL("select * from `order` where id=1")
    public Order test();
    
    @SQL("select * from `order` order by id desc limit 1")
    public Order getLastOrder();
    
    public Order findByOrderNo(String orderNo);
    public Order findByCustomerCode(String customerCode);
}

