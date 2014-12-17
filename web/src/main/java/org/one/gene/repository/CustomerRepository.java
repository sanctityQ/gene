package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import org.springframework.data.repository.PagingAndSortingRepository;

import org.one.gene.domain.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<Customer, Long> {
}

