package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import java.util.List;

import org.one.gene.domain.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.sinosoft.one.data.jade.annotation.SQL;
import org.springframework.data.repository.query.Param;

@Repository
public interface OrderRepository extends PagingAndSortingRepository<Order, Long> , JpaSpecificationExecutor<Order> {

    @SQL("select * from `order` where id=1")
    public Order test();
    
    @SQL("select * from `order` order by id desc limit 1")
    public Order getLastOrder();
    
    public Order findByOrderNo(String orderNo);
    
    public Order findByOutOrderNo(String outOrderNo);

    public List<Order> findByCustomerCode(String customerCode);

    
	@SQL("select * from `order` where (`order_no` like :orderNoSQL or `out_order_no` like :orderNoSQL ) ")
	List<Order> vagueSeachOrder(@Param("orderNoSQL") String orderNoSQL);
	
	@SQL("select * from `order` o  where exists (select 1 from `primer_product` p where o.`order_no` = p.`order_no` and p.`operation_type` = 'delivery' )"
			+ "#if(:orderNo != '') { and o.`order_no` = :orderNo } "
			+ "#if(:customerCode != '') { and o.`customer_code` = :customerCode }"
			+ "#if(:comCode != '') { and o.`com_code` = :comCode }"
			+ " order by o.`create_time` desc ")
	Page<Order> queryDeliveryDeal(@Param("orderNo") String orderNo,
			@Param("customerCode") String customerCode,
			@Param("comCode") String comCode, Pageable pageable);
	
}

