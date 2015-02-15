package org.one.gene.repository;
// Generated Dec 31, 2014 3:02:51 PM by One Data Tools 1.0.0

import org.one.gene.domain.entity.PrimerLabelConfigSub;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sinosoft.one.data.jade.annotation.SQL;

@Repository
public interface PrimerLabelConfigSubRepository extends PagingAndSortingRepository<PrimerLabelConfigSub, Long> {

	@SQL("delete from `primer_label_config_sub` where `id` = :num")
	public void deleteById(@Param("num") Long id);
}

