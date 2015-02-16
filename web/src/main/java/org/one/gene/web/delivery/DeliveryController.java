package org.one.gene.web.delivery;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;
import org.one.gene.domain.service.DeliveryService;
import org.one.gene.domain.service.PrintService;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.one.gene.web.order.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Maps;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.annotation.rest.Post;
import com.sinosoft.one.mvc.web.instruction.reply.EntityReply;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Json;

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
    @Autowired
	private PrintService printService;
    
   
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

    /**
     * 进入发货召回查询页面
     * 
     * */
    @Get("deliveryRecall")
    public String deliveryRecall(){
    	
    	return "deliveryRecall";
    }
    
    /**
     * 发货召回查询列表
     * @throws Exception
     */
    @Post("queryBack")
	public Reply queryBack(
			@Param("customerCode") String customerCode,
			@Param("createStartTime") String createStartTime,
			@Param("createEndTime") String createEndTime,
			@Param("boardNo") String boardNo,
			@Param("productNo") String productNo, 
			@Param("pageNo") Integer pageNo,
			@Param("pageSize") Integer pageSize, Invocation inv)
			throws Exception {

        if(pageNo == null || pageNo ==0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 10;
        }
        Pageable pageable = new PageRequest(pageNo-1,pageSize);
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchFilter.Operator.EQ+"_order.customerCode",customerCode);
		if (!"".equals(createStartTime)) {
        	searchParams.put(SearchFilter.Operator.GT+"__order.createTime",new Date(createStartTime+" 00:00:00"));
        }
		if (!"".equals(createEndTime)) {
        	searchParams.put(SearchFilter.Operator.LT+"__order.createTime",new Date(createEndTime+" 59:59:59"));
        }
        searchParams.put(SearchFilter.Operator.EQ+"__boardNo",boardNo);
        searchParams.put(SearchFilter.Operator.EQ+"__productNo",productNo);
        searchParams.put(SearchFilter.Operator.EQ+"__operationType",PrimerStatusType.finish);
        
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<PrimerProduct> spec = DynamicSpecifications.bySearchFilter(filters.values(), PrimerProduct.class);
        
        Page<PrimerProduct> primerProductPage = primerProductRepository.findAll(spec,pageable);
        Page<OrderInfo> primerProductListPage = deliveryService.convertPrimerProductList(primerProductPage,pageable);
        
        return Replys.with(primerProductListPage).as(Json.class);
    }
    
    /**
     * 发货召回查询列表
     * @throws Exception
     */
    @Post("saveBack")
	public Reply saveBack(@Param("orderInfos") List<OrderInfo> orderInfos,
			@Param("flag") String flag, @Param("text") String text,
			Invocation inv) throws Exception {

        deliveryService.saveBack(orderInfos, flag, text);
        
        return Replys.with("{\"success\":true,\"mesg\":\"success\"}").as(Json.class);
    } 
    
    
    /**
     * 发货标签
     * 
     * */
    @Get("deliveryLabel")
    public String deliveryLabel(){
    	return "deliveryLabel";
    }
    
    @Post("deliveryLabelPrint")
    public EntityReply<File> deliveryLabelPrint(@Param("boardNo") String boardNo,@Param("noType") String noType, Invocation inv) {
    	
		List<PrimerProduct> primerProducts = printService.getPrimerProducts(boardNo, noType, inv);
		EntityReply<File> fileStr = null; //form 提交使用该类型返回
		try {
			fileStr = deliveryService.deliveryLabel(primerProducts, inv);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return fileStr;
    }
}
