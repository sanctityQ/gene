package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import com.sinosoft.one.data.jade.annotation.SQL;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrimerProductRepository extends PagingAndSortingRepository<PrimerProduct, Long> {

    @SQL("select pp.* " +
    		" from `order` o , `primer_product` pp, `primer_product_value` ppv " +
    		" where o.`order_no` =  pp.`order_no` and pp.`id` = ppv.`primer_product_id` and ppv.`type` = 'TBN' " +
    		" and o.`status` = '02' and pp.`operation_type` = 'MakeTable' " +
            "#if(:customer_code != '') { and o.`customer_code` = :customer_code } " +
            "#if(:purifytype != '') { and pp.`purify_type` = :purifytype }" +
            "#if(:tbn1 != '') { and ppv.`value` >= :tbn1 }" +
            "#if(:tbn2 != '') { and ppv.`value` <= :tbn2 }" +
            "")
    List<PrimerProduct> selectPrimerProduct(@Param("customer_code") String customer_code, @Param("purifytype") String purifytype, @Param("tbn1") String tbn1, @Param("tbn2") String tbn2);
    
    
    
    PrimerProduct findByProductNo(String productNo);
    
    @SQL("select * from `primer_product` order by id desc limit 1")
    public PrimerProduct getLastProduct();
}

