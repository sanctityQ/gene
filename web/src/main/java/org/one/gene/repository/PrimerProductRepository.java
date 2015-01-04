package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import com.sinosoft.one.data.jade.annotation.SQL;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrimerProductRepository extends PagingAndSortingRepository<PrimerProduct, Long>  , JpaSpecificationExecutor<PrimerProduct> {

	@SQL("select pp.* "
			+ " from `order` o , `primer_product` pp, `primer_product_value` ppv "
			+ " where o.`order_no` =  pp.`order_no` and pp.`id` = ppv.`primer_product_id` and ppv.`type` = 'tbn' "
			+ " and o.`status` = '2' and pp.`operation_type` = 'orderCheckSuccess' "
			+ "#if(:customer_code != '') { and o.`customer_code` = :customer_code } "
			+ "#if(:purifytype != '') { and pp.`purify_type` = :purifytype }"
			+ "#if(:tbn1 != '') { and ppv.`value` >= :tbn1 }"
			+ "#if(:tbn2 != '') { and ppv.`value` <= :tbn2 }"
			+ "#if(:modiFlag == '0') { and ( pp.`modi_five_type` is null and pp.`modi_three_type` is null and pp.`modi_mid_type` is null and pp.`modi_spe_type` is null) }"
			+ "#if(:modiFlag == '') { and ( pp.`modi_five_type` is not null or pp.`modi_three_type` is not null or pp.`modi_mid_type` is not null or pp.`modi_spe_type` is not null) }"
			+ "")
	Page<PrimerProduct> selectPrimerProduct(
			@Param("customer_code") String customer_code,
			@Param("modiFlag") String modiFlag, @Param("tbn1") String tbn1,
			@Param("tbn2") String tbn2,
			@Param("purifytype") String purifytype, Pageable pageable);
    
    
    
    PrimerProduct findByProductNo(String productNo);
    
    PrimerProduct findByProductNoOrOutProductNo(String productNo, String outProductNo);
    
    @SQL("select * from `primer_product` order by id desc limit 1")
    public PrimerProduct getLastProduct();
    
    public List<PrimerProduct> findByOrder(Order order);
    
    public List<PrimerProduct> findByBoardNo(String boardNo);
}

