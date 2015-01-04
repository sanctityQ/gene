package org.one.gene.repository;
// Generated Dec 31, 2014 3:02:51 PM by One Data Tools 1.0.0

import org.one.gene.domain.entity.PrimerLabelConfig;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrimerLabelConfigRepository extends PagingAndSortingRepository<PrimerLabelConfig, Long> , JpaSpecificationExecutor<PrimerLabelConfig> {
	
	PrimerLabelConfig findByCustomerCode(String customerCode);
	
	
}

