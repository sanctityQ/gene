package org.one.gene.repository;
// Generated Apr 8, 2015 12:02:09 AM by One Data Tools 1.0.0

import java.util.List;

import org.one.gene.domain.entity.ProductMolecular;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface ProductMolecularRepository
        extends PagingAndSortingRepository<ProductMolecular, Long> , JpaSpecificationExecutor<ProductMolecular>{

	
	public ProductMolecular findById(Integer id);
	
	public List<ProductMolecular> findByProductCategories(String productCategories);
}

