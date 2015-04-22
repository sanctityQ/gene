package org.one.gene.repository;
// Generated Apr 8, 2015 12:02:09 AM by One Data Tools 1.0.0

import org.one.gene.domain.entity.ModifiedPrice;
import org.one.gene.domain.entity.ProductMolecular;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ModifiedPriceRepository
    extends PagingAndSortingRepository<ModifiedPrice, Long> , JpaSpecificationExecutor<ModifiedPrice>{

	public ModifiedPrice findById(Integer id);
	public ModifiedPrice findByModiType(String modiType);
}

