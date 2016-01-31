package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import org.one.gene.domain.entity.Menu;
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

  @SQL("select * from `user` where `validate` = '1' and (`code` like :userSQL or `name` like :userSQL ) " +
  		"#if(:comCodeSQL != '') { and `com_code` = :comCodeSQL }")
  List<User> vagueSeachUser(@Param("userSQL") String userSQL,@Param("comCodeSQL") String comCodeSQL);
  
  @SQL("select * from `user` where `customer_id` = :customerId ")
  List<User> getUserByCustomerId(@Param("customerId") String customerId);
  
  @SQL("select * from `menu` where `valid` = '1' order by `order_no` ")
  List<Menu> getAllMenu();
  
}

