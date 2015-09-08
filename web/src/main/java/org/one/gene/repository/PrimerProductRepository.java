package org.one.gene.repository;
// Generated Dec 14, 2014 1:21:34 AM by One Data Tools 1.0.0

import com.sinosoft.one.data.jade.annotation.SQL;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrimerProductRepository extends PagingAndSortingRepository<PrimerProduct, Long>  , JpaSpecificationExecutor<PrimerProduct> {

	@SQL("select pp.* "
			+ " from `order` o , `primer_product` pp, `primer_product_value` ppv "
			+ " where o.`order_no` =  pp.`order_no` and pp.`id` = ppv.`primer_product_id` and ppv.`type` = 'baseCount' "
			+ " and o.`status` = '1' and pp.`operation_type` = 'makeBoard' and (pp.`board_no` is null or pp.`board_no`='') "
			+ "#if(:customercode != '') { and o.`customer_code` = :customercode } "
			+ "#if(:purifytype != '') { and pp.`purify_type` = :purifytype }"
			+ "#if(:comCode != '') { and pp.`com_code` = :comCode }"
			+ "#if(:tbn1 != '') { and ppv.`value` >= :tbn1 }"
			+ "#if(:tbn2 != '') { and ppv.`value` <= :tbn2 }"
			+ "#if(:modiFlag == '0') { and ( pp.`modi_five_type` ='' and pp.`modi_three_type` ='' and pp.`modi_mid_type` ='' and pp.`modi_spe_type` ='') }"
			+ "#if(:modiFlag == '1') { and ( pp.`modi_five_type` !='' or pp.`modi_three_type` !='' or pp.`modi_mid_type` !='' or pp.`modi_spe_type` !='') }"
			+ "")
	Page<PrimerProduct> selectPrimerProduct(
			@Param("customercode") String customercode,
			@Param("modiFlag") String modiFlag, @Param("tbn1") String tbn1,
			@Param("tbn2") String tbn2,
			@Param("purifytype") String purifytype,
			@Param("comCode") String comCode,Pageable pageable);
    
	@SQL("select pp.* from `primer_product` pp where pp.`operation_type` = :operationType "
			+ "#if(:boardNo != '') { and pp.`board_no` = :boardNo }"
			+ "#if(:productNo != '') { and (pp.`product_no` = :productNo or pp.`out_product_no` = :productNo) }"
			+ "#if(:modiFiveType == '1') { and pp.`modi_five_type` !='' }"
			+ "#if(:modiFiveType == '0') { and pp.`modi_five_type` ='' }"
			+ "#if(:modiThreeType == '1') { and pp.`modi_three_type` !='' }"
			+ "#if(:modiThreeType == '0') { and pp.`modi_three_type` ='' }"
			+ "#if(:modiMidType == '1') { and pp.`modi_mid_type` !='' }"
			+ "#if(:modiMidType == '0') { and pp.`modi_mid_type` ='' }"
			+ "#if(:modiSpeType == '1') { and pp.`modi_spe_type` !='' }"
			+ "#if(:modiSpeType == '0') { and pp.`modi_spe_type` ='' }"
			+ "#if(:purifyType != '') { and pp.`purify_type` = :purifyType }"
			+ "#if(:comCode != '') { and pp.`com_code` = :comCode }"
			+ "")
	Page<PrimerProduct> resultsSelectQuery(
			@Param("boardNo") String boardNo,
			@Param("productNo") String productNo,
			@Param("operationType") String operationType,
			@Param("modiFiveType") String modiFiveType,
			@Param("modiThreeType") String modiThreeType,
			@Param("modiMidType") String modiMidType,
			@Param("modiSpeType") String modiSpeType,
			@Param("purifyType") String purifyType,
			@Param("comCode") String comCode,
			Pageable pageable);
	
	
    
    PrimerProduct findByProductNo(String productNo);
    
    PrimerProduct findByProductNoOrOutProductNo(String productNo, String outProductNo);
    
    @SQL("select * from `primer_product` where `out_product_no` is null and `from_product_no` is null order by id desc limit 1")
    public PrimerProduct getLastProduct();
    
    public List<PrimerProduct> findByOrder(Order order);
    
    public List<PrimerProduct> findByBoardNo(String boardNo);
    
	@SQL("select * from `primer_product`  where `product_no` like :productNo or `out_product_no` like :outProductNo ")
	List<PrimerProduct> vagueSeachPrimerProduct(@Param("productNo") String productNo, @Param("outProductNo") String outProductNo);
	
	 @SQL("select count(*) from `primer_product` where `product_no`=:productNo")
	int countByProductNo(@Param("productNo") String productNo);
	 
	@SQL("select * from `primer_product` where `board_no` = :boardNo and `order_no` = :orderNo order by product_no limit 1")
	public PrimerProduct getpPmerProductByBoardNoAndOrderNo(@Param("boardNo") String boardNo,@Param("orderNo") String orderNo);
}

