package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import org.one.gene.domain.entity.PrimerProductOperation;
import org.springframework.stereotype.Repository;

import com.sinosoft.one.data.jade.annotation.SQL;

@Repository
public interface PrimerProductOperationRepository extends PagingAndSortingRepository<PrimerProductOperation, Long> , JpaSpecificationExecutor<PrimerProductOperation>  {
	
    @SQL("select count(1) from `primer_product_operation` where `primer_product_id` = :ppID and `type`= :potType ")
    public int getCountWithType(@Param("ppID") Long ppID, @Param("potType") String potType);
   
    @SQL("select * from `primer_product_operation` where `primer_product_id` = :ppID  ORDER BY create_time ")
    List<PrimerProductOperation> getInfoByPrimerProductID(@Param("ppID") Long ppID);
    
    @SQL("select count(1) from `primer_product_operation` where `primer_product_id` = :ppID and `back_times`>0 ")
    public int getCountWithPPID(@Param("ppID") Long ppID);
}

