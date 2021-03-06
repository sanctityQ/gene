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
			+ " from `order` o , `primer_product` pp "
			+ " where o.`order_no` =  pp.`order_no` "
			+ " and o.`status` = '1' and pp.`operation_type` = 'makeBoard' and (pp.`board_no` is null or pp.`board_no`='') "
			+ "#if(:customerCode != '') { and o.`customer_code` = :customerCode } "
			+ "#if(:ptFlag == '1') { and pp.`purify_type` IN (?5) }"
			+ "#if(:comCode != '') { and pp.`com_code` = :comCode }"
			+ "#if(:tbn1 != '') { and pp.`tbn` >= :tbn1 }"
			+ "#if(:tbn2 != '') { and pp.`tbn` <= :tbn2 }"
			+ "#if(:odTotal1 != '') { and pp.`od_total` >= :odTotal1 }"
			+ "#if(:odTotal2 != '') { and pp.`od_total` <= :odTotal2 }"
			+ "#if(:liquidFlag == '1') { and ( pp.`liquid` is null or pp.`liquid`='' ) }"
			+ "#if(:liquidFlag == '2') { and pp.`liquid` !='' }"			
			+ "#if(:modiFlag == '0') { and ( pp.`modi_five_type` ='' and pp.`modi_three_type` ='' and pp.`modi_mid_type` ='' and pp.`modi_spe_type` ='')  order by pp.`product_no` }"
			+ "#if(:modiFlag == '1') { and ( pp.`modi_five_type` !='' or pp.`modi_three_type` !='' or pp.`modi_mid_type` !='' or pp.`modi_spe_type` !='') " 
			+ "#if(:productNoPrefix != '') { and pp.`product_no` like :productNoPrefix }" 
			+ "  order by pp.`modi_five_type`, pp.`modi_three_type`, pp.`modi_mid_type`, pp.`modi_spe_type`, pp.`product_no` }"
			+ " ")
	Page<PrimerProduct> selectPrimerProduct(
			@Param("customerCode") String customerCode,
			@Param("modiFlag") String modiFlag, 
			@Param("tbn1") String tbn1,
			@Param("tbn2") String tbn2,
			@Param("purifytype") String[] purifytype,
			@Param("ptFlag") String ptFlag,
			@Param("comCode") String comCode,
			@Param("productNoPrefix") String productNoPrefix,
			@Param("odTotal1") String odTotal1,
			@Param("odTotal2") String odTotal2,
			@Param("liquidFlag") String liquidFlag,
			Pageable pageable);
    
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

	@SQL("select pp.* from `primer_product` pp where pp.`operation_type` = :operationType "
			+ "#if(:boardNo != '') { and pp.`board_no` = :boardNo }"
			+ " and (pp.`modi_five_type` !='' or pp.`modi_three_type` !='' or pp.`modi_mid_type` !='' or pp.`modi_spe_type` !='' ) "
			+ "#if(:comCode != '') { and pp.`com_code` = :comCode }"
			+ "")
	Page<PrimerProduct> resultsSelectQueryDecorate(@Param("boardNo") String boardNo, @Param("operationType") String operationType,
			                                       @Param("comCode") String comCode, Pageable pageable);
	
    
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
	public PrimerProduct getPmerProductByBoardNoAndOrderNo(@Param("boardNo") String boardNo,@Param("orderNo") String orderNo);
	
	@SQL("select pp.* from `primer_product` pp "
			+ "where (pp.`modi_five_type` !='' or pp.`modi_three_type` !='' or pp.`modi_mid_type` !='' or pp.`modi_spe_type` !='' ) "
			+ "#if(:modiFiveType != '')  { and pp.`modi_five_type` =:modiFiveType }"
			+ "#if(:modiThreeType != '') { and pp.`modi_three_type` =:modiThreeType  }"
			+ "#if(:midType != '')   { and pp.`modi_mid_type` like :midType  }"
			+ "#if(:speType != '')   { and pp.`modi_spe_type` like :speType  }"
			+ "#if(:productNo != '') { and pp.`product_no` =:productNo  }"
			+ " and exists(select 1 from `order` o where o.`order_no` =  pp.`order_no` "
			+ "#if(:createStartTime != '') { and o.`create_time` >= STR_TO_DATE( :createStartTime , '%m/%d/%Y %H:%i:%S') } "
			+ "#if(:createEndTime != '')   { and o.`create_time` <= STR_TO_DATE( :createEndTime   , '%m/%d/%Y %H:%i:%S') } "
			+ "#if(:customerFlag == '0')   { and o.`customer_code` IN ( select `code` from `customer` where `customer_flag`= '2' ) }"
			+ "#if(:customerFlag == '1')   { and o.`customer_code` = :customerCode }"
			+ "#if(:customerFlag == '2')   { and o.`customer_code` = :customerCode }"
			+ "#if(:outOrderNo != '')      { and o.`out_order_no`  = :outOrderNo }"
			+ "#if(:contactsName != '')    { and o.`contacts_name` = :contactsName }"
			+ " )")
	List<PrimerProduct> queryXiuShiJInDu(
			@Param("createStartTime") String createStartTime,
			@Param("createEndTime") String createEndTime,
			@Param("outOrderNo") String outOrderNo,
			@Param("productNo") String productNo,
			@Param("contactsName") String contactsName,
			@Param("customerFlag") String customerFlag,
			@Param("customerCode") String customerCode,
			@Param("modiFiveType") String modiFiveType,
			@Param("modiThreeType") String modiThreeType,
			@Param("midType") String midType,
			@Param("speType") String speType);
	
	@SQL("select count(*) from `primer_product` where `operation_type` != :operationType and  `order_no`=:orderNo ")
	int countByOrderNoAndNoTType(@Param("orderNo") String orderNo, @Param("operationType") String operationType);
	
	@SQL("select count(*) from `primer_product` where `order_no`=:orderNo ")
	int getCountByOrderNo(@Param("orderNo") String orderNo);
	
	@SQL("select * from `primer_product##(:orderYear)` where `order_no`=:orderNo ")
	List<PrimerProduct> getprimersByOrderNo(@Param("orderNo") String orderNo, @Param("orderYear") String orderYear);
	
}

