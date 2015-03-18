package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import java.util.List;

import com.sinosoft.one.data.jade.annotation.SQL;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.one.gene.domain.entity.Board;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends PagingAndSortingRepository<Board, Long> , JpaSpecificationExecutor<Board> {
   
	Board findByBoardNo(String boardNo);
    
	@SQL("select * from `board` a where a.id is not null  "
			+ " and exists(select * from `primer_product` b where a.`board_no` = b.`board_no` and b.`operation_type` in ('synthesisPrepare','synthesisFailure')) "
			+ "#if(:boardNo != '') { and a.`board_no` like :boardNo} ")
	List<Board> selectOrderByNo(@Param("boardNo") String boardNo);
	
	@SQL("select * from `board` a where a.`board_no` like :boardNo " +
			" and exists( select * from `board_hole` b,`primer_product` c " +
			" where a.`board_no`=b.`board_no` and b.`status`='0' and b.`product_id`=c.`id` " +
			" #if(:operationType != '') {  and c.`operation_type` = :operationType  } )")
	List<Board> vagueSeachBoard(@Param("boardNo") String boardNo, @Param("operationType") String operationType);
	
	@SQL("select DISTINCT a.`id`,a.`board_no`,a.`board_type`,a.`create_time`,c.`operation_type` " +
		 " from `board` a,`board_hole` b,`primer_product` c where " +
		 " a.`board_no`=b.`board_no` and b.`status`='0' and b.`product_id`=c.`id` " +
		 " and c.`operation_type` = :operationType  order by a.`create_time` desc ")
	Page<Board> initBoardNo(@Param("operationType") String operationType, Pageable pageable);
	
}

