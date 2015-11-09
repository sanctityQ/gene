package org.one.gene.web.print;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.service.OrderService;
import org.one.gene.domain.service.PrintService;
import org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.web.order.OrderInfo;
import org.one.gene.web.order.PrimerProductList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import com.alibaba.fastjson.JSON;
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
import com.sinosoft.one.mvc.web.instruction.reply.transport.Text;


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
    @Get("productionLabel")
    public String productionLabel(){
    	
    	return "productionLabel";
    }
    
    
    /**
     * 打印标签查询
     * */
	public Reply printLabelQuery(@Param("boardNo") String boardNo,@Param("noType") String noType, Invocation inv) {

		List<PrimerProduct> primerProducts = printService.getPrimerProducts(boardNo, noType, inv);
		List<Order> orders = null;
		try {
			orders = printService.getPrintLabelList(primerProducts, inv);
		} catch (Exception e) {
			e.printStackTrace();
			return Replys.with("{\"success\":false,\"mess\":\""+e.getMessage()+"\"}").as(Json.class);
		}
    	
    	return Replys.with(orders).as(Json.class);
    }

    /**
     * 进入出库单打印查询页面
     * 
     * */
    @Get("printOutBoundList")
    public String printOutBoundList(){
    	
    	return "outboundPrinting";
    }
	
    @Post("printOutBoundQuery")
    public Reply printOutBoundQuery(@Param("orderNo") String orderNo, @Param("customerName") String customerName,@Param("status") String status,@Param("pageNo")Integer pageNo,
                        @Param("pageSize")Integer pageSize,Invocation inv) throws Exception {

    	if(pageNo == null || pageNo <=0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 20;
        }

        Pageable pageable = new PageRequest(pageNo-1,pageSize);
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchFilter.Operator.EQ+"_orderNo",orderNo);
        searchParams.put(SearchFilter.Operator.EQ+"_customerName",customerName);
        searchParams.put(SearchFilter.Operator.EQ+"_status",status);//审核通过
        
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Order> spec = DynamicSpecifications.bySearchFilter(filters.values(), Order.class);
        
        Page<Order> orderPage = orderRepository.findAll(spec,pageable);
        Page<OrderInfo> orderListPage = orderService.convertOrderList(orderPage,pageable);
        
    	return Replys.with(orderListPage).as(Json.class);
    }
    
    @Post("printOutBound")
    public EntityReply<File> printOutBound(@Param("orderInfoList")String orderInfoList,Invocation inv) throws Exception {
    	 List<OrderInfo> orderInfoLists = JSON.parseArray(orderInfoList, OrderInfo.class);
    	
    	 EntityReply<File> fileStr = null; //form 提交使用该类型返回
     	try {
      		fileStr = printService.printOutbound(orderInfoLists,inv);
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
     	return fileStr;
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
//    		fileStr = printService.exportLabel(primerProducts, customerCode, inv);
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
    @Get("createReport")
    public String createReport(Invocation inv){
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String customerFlag = user.getUser().getCustomer().getCustomerFlag();
		inv.addModel("customerFlag", customerFlag);// 用户归属公司标识，0-梓熙，1-代理公司，2-直接客户
    	return "createReport";
    }

    /**
     * 进入打印信封查询页面
     * 
     * */
    @Get("envelopePrint")
    public String envelopePrint(Invocation inv){
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String customerFlag = user.getUser().getCustomer().getCustomerFlag();
		inv.addModel("customerFlag", customerFlag);// 用户归属公司标识，0-梓熙，1-代理公司，2-直接客户
    	return "envelopePrint";
    }
    
    /**
     * 打印报告单查询
     * @throws IOException 
     * @throws Exception 
     * */
	public Reply printReportQuery(@Param("orderNo") String orderNo,
			@Param("customerCode") String customerCode,
			@Param("modifyTime") String modifyTime,
			@Param("pageNo") Integer pageNo,
			@Param("productNoPrefix") String productNoPrefix,
			@Param("customerFlagStr") String customerFlagStr,
			@Param("pageSize") Integer pageSize, Invocation inv) throws Exception {
    	
        if(pageNo == null || pageNo <= 0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 20;
        }
        
		if (customerFlagStr == null) {
			customerFlagStr = "";
		}
		
		if (productNoPrefix == null) {
			productNoPrefix = "";
		}
		
		if (!StringUtils.isBlank(productNoPrefix)) {
			productNoPrefix = productNoPrefix.toUpperCase() + "%";
        }
		
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String comCode = user.getUser().getCompany().getComCode();
    	String customerFlag = user.getUser().getCustomer().getCustomerFlag();
    	String comLevel     = user.getUser().getCompany().getComLevel();
    	
        Pageable pageable = new PageRequest(pageNo-1,pageSize);
        
        String createStartTime = "";
        String createEndTime = "";
        
		if (!"".equals(modifyTime)) {
        	createStartTime = modifyTime+" 00:00:00";
        	createEndTime = modifyTime+" 23:59:59";
        }
	
		
		if (!"0".equals(customerFlag)) {//代理公司和直接客户，只能查自己公司的业务
			customerCode = user.getUser().getCustomer().getCode();
		}
        
		Page<Order> orderPage = orderRepository.printReportQuery(
				createStartTime, createEndTime, comLevel, comCode,
				customerCode, orderNo, customerFlagStr, productNoPrefix,
				pageable);
        Page<OrderInfo> orderListPage = orderService.convertOrderList(orderPage,pageable);
        
    	return Replys.with(orderListPage).as(Json.class);
    }
    
    
    /**
     * 打印报告单
     * @throws IOException 
     * */
	@Post("exportFile/{orderNoStr}/{flag}/")
	public void exportFile( @Param("flag") String flag,@Param("orderNoStr") String orderNo, Invocation inv) throws IOException {
    	
		printService.exportFile(orderNo, flag, inv);

    }
	
	
	
    
    
    
    
}
