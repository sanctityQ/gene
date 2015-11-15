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
			+ "#if(:customerFlagStr != '') { and o.`customer_code` IN ( select `code` from `customer` where `customer_flag`= :customerFlagStr ) }"
			+ " order by o.`create_time` desc ")
	Page<Order> queryDeliveryDeal(@Param("orderNo") String orderNo,
			@Param("customerCode") String customerCode,
			@Param("comCode") String comCode, 
			@Param("customerFlagStr") String customerFlagStr, 
			Pageable pageable);
	
	@SQL("select * from `order` where `order_no` in (select distinct `order_no` from `primer_product` where `board_no`= :boardNo )")
	List<Order> getOrdersByBoardNo(@Param("boardNo") String boardNo);
	
	@SQL("select * from `order` where `order_no` in " +
			"(select distinct `order_no` from `primer_product` where (`product_no`= :productNo or `out_product_no`= :productNo ))")
	List<Order> getOrdersByProductNo(@Param("productNo") String productNo);
	
	
	@SQL("select * from `order` o  where `status` = '1' "
			+ "#if(:createStartTime != '') { and o.`create_time` >= STR_TO_DATE( :createStartTime , '%m/%d/%Y %H:%i:%S') } "
			+ "#if(:createEndTime != '') { and o.`create_time`   <= STR_TO_DATE( :createEndTime   , '%m/%d/%Y %H:%i:%S') } "
			+ "#if(:customerFlag == '0') { and o.`customer_code` IN ( select `code` from `customer` where `customer_flag`= '2' ) }"
			+ "#if(:customerFlag == '1') { and o.`customer_code` = :customerCode }"
			+ "#if(:customerFlag == '2') { and o.`customer_code` = :customerCode }"
			)
	List<Order>  queryYinWuJInDu(@Param("createStartTime") String createStartTime,@Param("createEndTime") String createEndTime,
			@Param("customerFlag") String customerFlag,@Param("customerCode") String customerCode);
	
	@SQL("select * from `order` o  where `status` = '1' "
			+ "#if(:createStartTime != '') { and o.`create_time` >= STR_TO_DATE( :createStartTime , '%m/%d/%Y %H:%i:%S') } "
			+ "#if(:createEndTime != '') { and o.`create_time`   <= STR_TO_DATE( :createEndTime   , '%m/%d/%Y %H:%i:%S') } "
			+ "#if(:comLevel != '1') { and o.`com_code` = :comCode }"
			+ "#if(:customerFlag != '0') { and o.`customer_code` = :customerCode }"
			+ "#if(:customerFlag == '0' && :customerName !='') { and o.`customer_name` = :customerName }"
			+ "#if(:orderNo != '') { and o.`order_no` = :orderNo }"
			+ "#if(:customerFlagStr != '') { and o.`customer_code` IN ( select `code` from `customer` where `customer_flag`= :customerFlagStr ) }"
			+ "#if(:productNoPrefix != '') { and exists ( select 1 from `primer_product` pp where o.`order_no` = pp.`order_no` and pp.`product_no` like :productNoPrefix ) }"
			+ " order by o.`create_time` desc "
			)
	Page<Order> queryDeliveryList(
			@Param("createStartTime") String createStartTime,
			@Param("createEndTime") String createEndTime,
			@Param("comLevel") String comLevel,
			@Param("comCode") String comCode,
			@Param("customerFlag") String customerFlag,
			@Param("customerCode") String customerCode,
			@Param("customerName") String customerName,
			@Param("orderNo") String orderNo,
			@Param("customerFlagStr") String customerFlagStr, 
			@Param("productNoPrefix") String productNoPrefix, 
			Pageable pageable
			);
	
	@SQL("select * from `order` o  where `status` = '1' "
			+ "#if(:createStartTime != '') { and o.`create_time` >= STR_TO_DATE( :createStartTime , '%m/%d/%Y %H:%i:%S') } "
			+ "#if(:createEndTime != '') { and o.`create_time`   <= STR_TO_DATE( :createEndTime   , '%m/%d/%Y %H:%i:%S') } "
			+ "#if(:comLevel != '1') { and o.`com_code` = :comCode }"
			+ "#if(:customerCode != '') { and o.`customer_code` = :customerCode }"
			+ "#if(:orderNo != '') { and o.`order_no` = :orderNo }"
			+ "#if(:customerFlagStr != '') { and o.`customer_code` IN ( select `code` from `customer` where `customer_flag`= :customerFlagStr ) }"
			+ "#if(:productNoPrefix != '') { and exists ( select 1 from `primer_product` pp where o.`order_no` = pp.`order_no` and pp.`product_no` like :productNoPrefix ) }"
			+ " order by o.`create_time` desc "
			)
	Page<Order> printReportQuery(
			@Param("createStartTime") String createStartTime,
			@Param("createEndTime") String createEndTime,
			@Param("comLevel") String comLevel,
			@Param("comCode") String comCode,
			@Param("customerCode") String customerCode,
			@Param("orderNo") String orderNo,
			@Param("customerFlagStr") String customerFlagStr, 
			@Param("productNoPrefix") String productNoPrefix, 
			Pageable pageable
			);
	
	@SQL("select * from `order` o  where 1=1 "
			+ "#if(:createStartTime != '') { and o.`create_time` >= STR_TO_DATE( :createStartTime , '%m/%d/%Y %H:%i:%S') } "
			+ "#if(:createEndTime != '') { and o.`create_time`   <= STR_TO_DATE( :createEndTime   , '%m/%d/%Y %H:%i:%S') } "
			+ "#if(:comLevel != '1') { and o.`com_code` = :comCode }"
			+ "#if(:customerCode != '') { and o.`customer_code` = :customerCode }"
			+ "#if(:orderNo != '') { and o.`order_no` = :orderNo }"
			+ "#if(:orderStatus == 'examine') { and o.`status` in (0,3) }"
			+ "#if(:orderStatus == '1') { and o.`status` = 1 }"
			+ "#if(:outOrderNo != '') { and o.`out_order_no` = :outOrderNo }"
			+ "#if(:primeName != '') { and exists ( select 1 from `primer_product` pp where o.`order_no` = pp.`order_no` and pp.`prime_name` = :primeName ) }"
			+ " order by o.`create_time` desc "
			)
	Page<Order> orderListQuery(
			@Param("createStartTime") String createStartTime,
			@Param("createEndTime") String createEndTime,
			@Param("comLevel") String comLevel,
			@Param("comCode") String comCode,
			@Param("customerCode") String customerCode,
			@Param("orderNo") String orderNo,
			@Param("orderStatus") String orderStatus,
			@Param("outOrderNo") String outOrderNo,
			@Param("primeName") String primeName,
			Pageable pageable
			);
}

