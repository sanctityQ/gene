package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import org.one.gene.domain.entity.PrimerProductValue;
import org.springframework.stereotype.Repository;

import com.sinosoft.one.data.jade.annotation.SQL;

@Repository
public interface PrimerProductValueRepository extends PagingAndSortingRepository<PrimerProductValue, Long> {
	
	
    @SQL("select p.* from `primer_product_value` p where p.`primer_product_id` =  :primerProductId ")
    List<PrimerProductValue> selectValueByPrimerProductId(@Param("primerProductId") Long primerProductId);
	
}

