package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import java.util.List;

import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.BoardHole;
import org.one.gene.domain.entity.PrimerProduct;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardHoleRepository extends PagingAndSortingRepository<BoardHole, Integer> {
	
	List<BoardHole> findByBoard(Board board);
	
	List<BoardHole> findByPrimerProduct(PrimerProduct primerProduct);
	
}

