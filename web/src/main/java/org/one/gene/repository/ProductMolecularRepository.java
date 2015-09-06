package org.one.gene.repository;
// Generated Apr 8, 2015 12:02:09 AM by One Data Tools 1.0.0

import java.util.List;

import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.ProductMolecular;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.sinosoft.one.data.jade.annotation.SQL;


public interface ProductMolecularRepository
        extends PagingAndSortingRepository<ProductMolecular, Long> , JpaSpecificationExecutor<ProductMolecular>{

	
	public ProductMolecular findById(Integer id);
	
	public List<ProductMolecular> findByProductCategories(String productCategories);
	
	@SQL("select * from `product_molecular`  where `product_categories` = :categories and `product_code` =:code ")
	List<ProductMolecular> getMoleculars(@Param("categories") String categories, @Param("code") String code);
	
}

