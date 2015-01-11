package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.one.gene.domain.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> , JpaSpecificationExecutor<User> {


    User findByCode(String username);

	List<User> findByName(String name);
	
	List<User> findByComCode(String comCode);
	
	List<User> findByNameAndComCode(String name, String comCode);

}

