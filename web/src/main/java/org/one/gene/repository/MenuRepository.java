package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import org.one.gene.domain.entity.Menu;
import org.springframework.stereotype.Repository;

import com.sinosoft.one.data.jade.annotation.SQL;

@Repository
public interface MenuRepository
    extends PagingAndSortingRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {

  @SQL("select * from `menu` where `valid` = '1' order by `order_no` ")
  List<Menu> getAllMenu();
  
}

