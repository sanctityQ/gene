package org.one.gene.web.delivery;

import java.util.List;

import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Post;

import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Json;


import org.one.gene.domain.entity.BoardHole;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.service.DeliveryService;
import org.one.gene.domain.service.SynthesisService;
import org.one.gene.repository.BoardHoleRepository;
import org.one.gene.repository.BoardRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Path
public class DeliveryController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PrimerProductRepository primerProductRepository;
    
    @Autowired
    private PrimerProductValueRepository primerProductValueRepository;
    
    @Autowired
    private DeliveryService deliveryService;
    
   
    /**
     * 进入发货查询页面
     * 
     * */
    @Get("deliveryResults")
    public String deliveryResults(){
    	
    	return "deliveryResults";
    }
    
    /**
     * 保存发货信息
     * */
    @Post("saveDelivery")
	public Reply saveDelivery(@Param("primerProducts") List<PrimerProduct> primerProducts, Invocation inv) {
    	
    	deliveryService.saveDelivery(primerProducts);
    	
    	return Replys.with("{\"success\":true,\"mesg\":\"success\"}").as(Json.class);
    }
    
    
}
