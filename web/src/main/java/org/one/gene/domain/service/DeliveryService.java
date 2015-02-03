package org.one.gene.domain.service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductOperation;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;
import org.one.gene.domain.entity.PrimerType.PrimerOperationType;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
   
		
		
		
}
