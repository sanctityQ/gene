package org.one.gene.web.print;

import java.util.List;
import java.util.Map;

import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.service.PrintService;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.web.order.OrderInfoList;
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

@Path
public class PrintController {
    
	@Autowired
    private PrimerProductRepository primerProductRepository;
	@Autowired
    private OrderRepository orderRepository;
	@Autowired
	private PrintService printService;
	
    /**
     * 进入打印标签查询页面
     * 
     * */
    @Get("prePrintLabelQuery")
    public String prePrintLabelQuery(){
    	
    	return "printLabelQuery";
    }
    
    /**
     * 打印标签查询
     * */
    public String printLabelQuery(@Param("comcode") String comcode1, Invocation inv){
    	
    	System.out.println("打印标签选择的公司======="+comcode1);
    	
    	if(!"".equals(comcode1)){
    		comcode1 = comcode1+"%";
    	}
    	List<PrimerProduct> primerProducts = primerProductRepository.selectPrimerProductForLabel(comcode1);
    	
    	inv.addModel("primerProducts", primerProducts);
    	return "printLabelList";
    }
    
    @Post("printOutBoundQuery")
    public String printOutBoundQuery(@Param("orderNo") String orderNo, @Param("customerCode") String customerCode,@Param("pageNo")Integer pageNo,
                        @Param("pageSize")Integer pageSize,Invocation inv) throws Exception {

        if(pageNo == null){
            pageNo = 0;
        }

        if(pageSize == null){
            pageSize = 5;
        }

        Pageable pageable = new PageRequest(pageNo,pageSize);
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchFilter.Operator.EQ+"_orderNo",orderNo);
        searchParams.put(SearchFilter.Operator.EQ+"_customerCode",customerCode);
        //searchParams.put(SearchFilter.Operator.EQ+"_PrimerProduct.operationType","delivery");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Order> spec = DynamicSpecifications.bySearchFilter(filters.values(), Order.class);
        
        Page<Order> orderPage = orderRepository.findAll(spec,pageable);
        Page<OrderInfoList> orderListPage = printService.convertOutbound(orderPage,pageable);
        
    	inv.addModel("page", orderListPage);
    	inv.addModel("pageSize", pageSize);
        return "printOutboundQuery";
    }
    
    @Post("printOutBound")
    public String printOutBound(@Param("orderInfos") List<OrderInfoList> outboundList,Invocation inv) throws Exception {
    	for(OrderInfoList outbound:outboundList){
    		System.out.println(outbound.getCommodityCode());
    		System.out.println(outbound.getCommodityName());
    	}
        
        return "";
    }

}
