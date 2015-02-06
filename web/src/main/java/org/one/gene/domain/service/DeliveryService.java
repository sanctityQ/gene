package org.one.gene.domain.service;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductOperation;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;
import org.one.gene.domain.entity.PrimerType.PrimerOperationType;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.web.order.OrderInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

//Spring Bean的标识.
@Component
//默认将类中的所有public函数纳入事务管理.
@Transactional(readOnly = true)
public class DeliveryService {

    private static Logger logger = LoggerFactory.getLogger(DeliveryService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PrimerProductRepository primerProductRepository;
    
    @Autowired
    private PrimerProductOperationRepository primerProductOperationRepository;

    @Autowired
    private SynthesisService synthesisService;
    
    
    
	//保存发货信息
    @Transactional(readOnly = false)
	public String saveDelivery(List<PrimerProduct> primerProducts) {
    	
		PrimerProduct primerProduct = new PrimerProduct(); 
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		List<PrimerProductOperation> primerProductOperations = new ArrayList<PrimerProductOperation>();
		PrimerOperationType type = null;
		String typeDesc = "";
		
		for (PrimerProduct pp:primerProducts) {
			primerProduct = primerProductRepository.findOne(pp.getId());
			if (primerProduct != null) {
				primerProduct.setOperationType(PrimerStatusType.finish);
				type = PrimerOperationType.deliverySuccess;
				typeDesc = PrimerOperationType.deliverySuccess.desc();
				
				
				//组装操作信息
				primerProductOperation = new PrimerProductOperation();
				primerProductOperation.setPrimerProduct(primerProduct);
				primerProductOperation.setUserCode("123");//后续从session取得
				primerProductOperation.setUserName("张三");//后续从session取得
				primerProductOperation.setCreateTime(new Date());
				primerProductOperation.setType(type);
				primerProductOperation.setTypeDesc(typeDesc);
				primerProductOperation.setBackTimes(primerProduct.getBackTimes());
				primerProductOperation.setFailReason("");
				
				primerProductOperations.add(primerProductOperation);
				
				
				//保存primer_product表数据
				primerProductRepository.save(primerProduct);
			}
			
    	}
		//更新操作信息
		primerProductOperationRepository.save(primerProductOperations);
    	
    	return "";
    }
   
		
	public Page<OrderInfo> convertPrimerProductList(
			Page<PrimerProduct> primerProductPage, Pageable pageable)
			throws IllegalStateException, IOException {

		OrderInfo orderInfo = new OrderInfo();
		List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
		
		for(PrimerProduct primerProduct:primerProductPage.getContent()){
			
			synthesisService.addNewValue(primerProduct);
			
			orderInfo = new OrderInfo();
			if(!"".equals(primerProduct.getProductNo())){
				orderInfo.setProductNoMinToMax(primerProduct.getProductNo());
			}else{
				orderInfo.setProductNoMinToMax(primerProduct.getOutProductNo());
			}
			orderInfo.setOrderNo(primerProduct.getOrder().getOrderNo());
			orderInfo.setCustomerName(primerProduct.getOrder().getCustomerName());
			orderInfo.setCreateTime(primerProduct.getOrder().getCreateTime());
			orderInfo.setModifyTime(primerProduct.getOrder().getModifyTime());
			orderInfo.setStatus(primerProduct.getOperationType().desc());
			orderInfo.setTbnTotal(primerProduct.getTbn()+"");
			orderInfoList.add(orderInfo);
		}
		
		Page<OrderInfo> primerProductListPage = new PageImpl<OrderInfo>(orderInfoList,pageable,primerProductPage.getTotalElements());
		
        return primerProductListPage;
    }
	
	//保存召回信息
    @Transactional(readOnly = false)
	public String saveBack(List<OrderInfo> orderInfos, String flag, String text ) {
    	
		PrimerProduct primerProduct = new PrimerProduct(); 
		PrimerProductOperation primerProductOperation = new PrimerProductOperation();
		List<PrimerProductOperation> primerProductOperations = new ArrayList<PrimerProductOperation>();
		PrimerOperationType type = null;
		String typeDesc = "";
		
		for (OrderInfo orderInfo : orderInfos) {
			primerProduct = primerProductRepository.findByProductNoOrOutProductNo(orderInfo.getProductNoMinToMax(), orderInfo.getProductNoMinToMax());
			if (primerProduct != null) {
				primerProduct.setBackTimes(primerProduct.getBackTimes()+1);
				if ("1".equals(flag)) {//安排合成
					primerProduct.setOperationType(PrimerStatusType.synthesis);
				}else if ("2".equals(flag)) {//重新分装
					primerProduct.setOperationType(PrimerStatusType.pack);
				}
				
				type = PrimerOperationType.backSuccess;
				typeDesc = PrimerOperationType.backSuccess.desc();
				
				
				//组装操作信息
				primerProductOperation = new PrimerProductOperation();
				primerProductOperation.setPrimerProduct(primerProduct);
				primerProductOperation.setUserCode("123");//后续从session取得
				primerProductOperation.setUserName("张三");//后续从session取得
				primerProductOperation.setCreateTime(new Date());
				primerProductOperation.setType(type);
				primerProductOperation.setTypeDesc(typeDesc);
				primerProductOperation.setBackTimes(primerProduct.getBackTimes());
				primerProductOperation.setFailReason(text);
				
				primerProductOperations.add(primerProductOperation);
				
				
				//保存primer_product表数据
				primerProductRepository.save(primerProduct);
			}
			
    	}
		//更新操作信息
		primerProductOperationRepository.save(primerProductOperations);
    	
    	return "";
    }
    
    
    
    
    
    
		
}
