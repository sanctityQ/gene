package org.one.gene.web.print;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.service.OrderService;
import org.one.gene.domain.service.PrintService;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.web.order.OrderInfoList;
import org.one.gene.web.order.PrimerProductList;
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


@Path
public class PrintController {
    
	@Autowired
    private PrimerProductRepository primerProductRepository;
	@Autowired
    private OrderRepository orderRepository;
	@Autowired
	private PrintService printService;
    @Autowired
    private OrderService orderService;
    
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
	public String printLabelQuery(@Param("boardNo") String boardNo,	@Param("productNo") String productNo, Invocation inv) {

		List<PrimerProduct> primerProducts = printService.getPrimerProducts(boardNo, productNo, inv);
		List<Order> orders = printService.getOrderListFromPrimerProductList(primerProducts);
    	
		inv.addModel("primerProducts", primerProducts);
    	inv.addModel("orders", orders);
    	
    	return "printLabelList";
    }

    /**
     * 进入打印标签配置页面
     * 
     * */
    @Get("printLabelConfig")
    public String printLabelConfig(){
    	return "printLabelConfig";
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
        //searchParams.put(SearchFilter.Operator.EQ+"_primerproduct.operationType","delivery");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Order> spec = DynamicSpecifications.bySearchFilter(filters.values(), Order.class);
        
        Page<Order> orderPage = orderRepository.findAll(spec,pageable);
        Page<OrderInfoList> orderListPage = printService.convertOutbound(orderPage,pageable);
        
    	inv.addModel("page", orderListPage);
    	inv.addModel("pageSize", pageSize);
        return "printOutboundQuery";
    }
    
    @Post("printOutBound")
    public String printOutBound(@Param("outboundList")String outboundJson,Invocation inv) throws Exception {
    		System.out.println(outboundJson);
        
        return "";
    }

    /**
     * 导出打印标签
     * */
    @Post("exportLabel/{customerCode}")
	public EntityReply<File> exportLabel(
			@Param("primerProductList") PrimerProductList primerProductList,
			@Param("customerCode") String customerCode, Invocation inv) {
    	
        List<PrimerProduct> primerProducts = primerProductList.getPrimerProducts();
		for (int i = primerProducts.size() - 1; i >= 0; i--) {
			//如果与客户代码不相等，则移除
			if (!((PrimerProduct)primerProducts.get(i)).getOrder().getCustomerCode().equals(customerCode)) {
				primerProducts.remove(i);
			}
		}
		
    	EntityReply<File> fileStr = null;
    	try {
    		fileStr = printService.exportLabel(primerProducts, customerCode, inv);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return fileStr;
    }
	
	
    /**
     * 进入打印报告单查询页面
     * 
     * */
    @Get("prePrintReportQuery")
    public String prePrintReportQuery(){
    	
    	return "printReportQuery";
    }
    
    /**
     * 打印报告单查询
     * @throws IOException 
     * @throws Exception 
     * */
	public String printReportQuery(@Param("orderNo") String orderNo,
			@Param("customerCode") String customerCode,
			@Param("pageNo") Integer pageNo,
			@Param("pageSize") Integer pageSize, Invocation inv) throws Exception {
    	
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
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Order> spec = DynamicSpecifications.bySearchFilter(filters.values(), Order.class);
        
        Page<Order> orderPage = orderRepository.findAll(spec,pageable);
        Page<OrderInfoList> orderListPage = orderService.convertOrderList(orderPage,pageable);
        
    	inv.addModel("page", orderListPage);
    	inv.addModel("pageSize", pageSize);
    	
    	return "printReportList";
    }
    
    
    /**
     * 打印报告单
     * */
	@Post("exportReport/{orderNo}")
	public EntityReply<File> exportReport(@Param("orderNo") String orderNo, Invocation inv) {
    	
    	EntityReply<File> fileStr = null;
    	try {
    		fileStr = printService.exportReport(orderNo, inv);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return fileStr;
    }
	
	
	
    
    
    
    
}
