package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import org.one.gene.domain.entity.User;
import org.springframework.stereotype.Repository;

import com.sinosoft.one.data.jade.annotation.SQL;

@Repository
public interface UserRepository
    extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {


  User findByCode(String username);

  List<User> findByName(String name);

  //List<User> findByComCode(String comCode);

  //List<User> findByNameAndComCode(String name, String comCode);

  @SQL("select * from `user` where `staff_flag` = '0' and (`code` like :customerSQL or `name` like :customerSQL ) ")
  List<User> vagueSeachCustomer(@Param("customerSQL") String customerSQL);
}

