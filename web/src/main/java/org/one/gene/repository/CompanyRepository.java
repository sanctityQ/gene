package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import org.one.gene.domain.entity.Company;
import org.springframework.stereotype.Repository;

import com.sinosoft.one.data.jade.annotation.SQL;

@Repository
public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {

    Company findByComCode(String comCode);
  
	@SQL("select * from `company` where `com_code` like :companySQL or `com_name` like :companySQL ")
	List<Company> vagueSeachCompany(@Param("companySQL") String companySQL);
	
}

