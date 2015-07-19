package org.one.gene.domain.service;

import org.one.gene.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyService {
	@Autowired
    private CompanyRepository companyRepository;
	

}
