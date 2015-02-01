package org.one.gene.web.order;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.google.common.io.Files;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.service.OrderService;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.annotation.rest.Post;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Json;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Text;

@Path
public class OrderController {


    private static Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PrimerProductRepository primerProductRepository;


    @Get("import")
    public String orderImport(Invocation inv){
    	//输出模版下载地址
    	String fileName = "orderTemplate.xls";
		String templateFilePath= File.separator+"gene"+File.separator+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator+fileName;
		inv.addModel("templateFilePath", templateFilePath);
        return "orderImport";
    }
    @Get("orderList")
    @Post("orderList")
    public String orderList(){
        return "orderList";
    }
    @Get("orderExamine")
    public String orderExamine(){
        return "orderExamine";
    }
    
    
    @Post("upload")
    public String upload(@Param("customerCode") String customerCode, @Param("file") MultipartFile file, Invocation inv) throws Exception {

    	ArrayList<String> errors = new ArrayList<String>();
    	
    	if("".equals(customerCode)){
    		throw new Exception("客户代码或名称未录入，请您录入！");
    	}
    	if (file.isEmpty()) {
    		throw new Exception("请您选择要上传的文件！");
    	}
    	
    	//取得当前所在年份
    	// 用于时间的格式化
    	SimpleDateFormat sFormat = new SimpleDateFormat("yyyyMMdd");
     	// 用于时间的处理
    	Calendar calendar = Calendar.getInstance();
    	// 格式化新的时间
    	String dateString = sFormat.format(calendar.getTime());

		String realpathdir = "";
    	String path="";//暂时按照只上传一个文件定义
    	String filename = "";
    	if (!file.isEmpty()) { 
        	filename = file.getOriginalFilename();
        	realpathdir = inv.getServletContext().getRealPath("/")+"upExcel"+File.separator+dateString+File.separator;
        	path = realpathdir+filename;
        	
    	    // 创建文件目录
    	    File savedir = new File(realpathdir);
    	    // 如果目录不存在就创建
    	    if (!savedir.exists()) {
    	      savedir.mkdirs();
    	    }
    	    
        	System.out.println(path);
        	file.transferTo(new File(path));
	        
	        if(!"".equals(path)){
		        errors = orderService.getExcelPaseErrors(path,1,2);
	        }
    	}
        if(errors.size()>0){
        	inv.addModel("errors", errors);
        	return "importError";
        }else{
        	//获取客户信息
        	Customer customer = orderService.findCustomer(customerCode);
        	if(customer==null){
        		throw new Exception("无此客户信息，请您确认后重新上传！");
        	}
        	//组织订单对象
        	Order order = new Order();
        	order = orderService.ReadExcel(path, 1,"2-",order);
        	orderService.convertOrder(customer,filename,order);
        	//保存订单信息
        	orderService.save(order);
        	inv.getResponse().setContentType("text/html");
    		inv.addModel("customer", customer);
    		inv.addModel("order", order);
    		//jsp获取未生效生成datagrid表格，后续优化
    		inv.addModel("total", order.getPrimerProducts().size());
    		inv.addModel("reSultdata", JSONObject.toJSONString(order.getPrimerProducts()));
        	return "orderInfo";
        }
        
    }
    
    @Post("modifyQuery")
    @Get("modifyQuery")
    public String modifyQuery(@Param("orderNo") String orderNo, Invocation inv) throws Exception {
    	 Order order = orderRepository.findByOrderNo(orderNo);
    	 String coustomerCode = "";
     	 coustomerCode = order.getCustomerCode();
     	 Customer customer = orderService.findCustomer(coustomerCode);
     	 inv.addModel("customer", customer);
		 inv.addModel("order", order);
    	 return "orderInfo";
    }

    
    /**
     * 订单导入列表展示
     * @param orderNo
     * @param inv
     * @return
     * @throws Exception
     */
    @Post("productQuery")
    public Reply productQuery(@Param("orderNo") String orderNo,Invocation inv) throws Exception {
        
        Order order = orderRepository.findByOrderNo(orderNo);
        
        BigDecimal orderTotalValue = new BigDecimal("0");
        String coustomerCode = "";
    	coustomerCode = order.getCustomerCode();
    	for(PrimerProduct product:order.getPrimerProducts()){
    		product.setTbn(new BigDecimal(product.getGeneOrder().length()));
    		orderTotalValue = orderTotalValue.add(product.getTotalVal());
    	}
    	order.setTotalValue(orderTotalValue);
    	
	    Customer customer = orderService.findCustomer(coustomerCode);
	    
    	 for(PrimerProduct primerProduct:order.getPrimerProducts()){
    		orderService.addNewValue(primerProduct);
    	 }

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCustomer(customer);
        orderInfo.setOrder(order);
        return Replys.with(orderInfo).as(Json.class);
    }
    
    
    @Post("save")
    public Reply save(@Param("primerProducts") List<PrimerProduct> primerProducts,@Param("orderNo") String orderNo,Invocation inv) throws IllegalStateException, IOException {
    	Order order = orderRepository.findByOrderNo(orderNo);
        Map<Long, PrimerProduct> newPrimerProductMap = Maps.newHashMap();
        for (PrimerProduct primerProduct : primerProducts) {
            newPrimerProductMap.put(primerProduct.getId(),primerProduct);
        }
        for (PrimerProduct primerProduct : order.getPrimerProducts()) {
            PrimerProduct newPrimerProduct = newPrimerProductMap.get(primerProduct.getId());
            if (newPrimerProduct != null) {
                primerProduct.setNmolTB(newPrimerProduct.getNmolTB());
                primerProduct.setNmolTotal(newPrimerProduct.getNmolTotal());

                newPrimerProductMap.remove(primerProduct.getId());
            }
        }
        for (PrimerProduct newPrimerProduct : newPrimerProductMap.values()) {
            newPrimerProduct.setOrder(order);
            //order.getPrimerProducts().add(newPrimerProduct);
        }

        //order.setPrimerProducts(primerProducts);
       // orderService.save(order);
        orderService.saveOrderAndPrimerProduct(order,newPrimerProductMap.values());
    	return Replys.with("sucess").as(Text.class);  
    }
    
    /**
     * 订单查询列表
     * @param orderNo
     * @param customerCode
     * @param pageNo
     * @param pageSize
     * @param inv
     * @return
     * @throws Exception
     */
    @Post("query")
    public Reply query(@Param("orderNo") String orderNo, @Param("customerCode") String customerCode,@Param("status") String status,@Param("pageNo")Integer pageNo,
                        @Param("pageSize")Integer pageSize,Invocation inv) throws Exception {

        if(pageNo == null || pageNo ==0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 10;
        }
        Pageable pageable = new PageRequest(pageNo,pageSize);
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchFilter.Operator.EQ+"_orderNo",orderNo);
        searchParams.put(SearchFilter.Operator.EQ+"_customerCode",customerCode);
        searchParams.put(SearchFilter.Operator.EQ+"_status",status);
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Order> spec = DynamicSpecifications.bySearchFilter(filters.values(), Order.class);
        
        Page<Order> orderPage = orderRepository.findAll(spec,pageable);
        Page<OrderInfo> orderListPage = orderService.convertOrderList(orderPage,pageable);
        
        return Replys.with(orderListPage).as(Json.class);
    }
    
    /**
     * 订单删除
     * @param orderNo
     * @param inv
     * @return
     */
    @Post("delete") 
    public Object delete(@Param("orderNo") String orderNo,Invocation inv){
    	Order order = orderRepository.findByOrderNo(orderNo);
    	orderRepository.delete(order);
    	return Replys.with("sucess").as(Text.class);  
    }
    
    /**
     * 订单审核查询
     * @param orderNo
     * @param inv
     * @return
     * @throws Exception
     */
    @Post("examine")
    public Object examine(@Param("orderNo") String orderNo,@Param("failReason") String failReason,Invocation inv) throws Exception {
        orderService.examine(orderNo,failReason);
    	return Replys.with("sucess").as(Text.class);
    }
    
    @Post("vagueSeachCustomer") 
    public Reply vagueSeachCustomer( @Param("seachCustom") String customerCode,Invocation inv){
    	List<Customer> customers = orderService.vagueSeachCustomer(customerCode);
    	return Replys.with(customers).as(Json.class);
    }
    
    
    @Get("downLoad") 
    @Post("downLoad") 
    public void downLoad(Invocation inv) throws IOException {
    	
		String fileName = "orderTemplate.xls";
		String filePath = inv.getServletContext().getRealPath("/")+"views"+File.separator+"downLoad"+File.separator+"template"+File.separator+fileName;
        

        inv.getResponse().reset();
        inv.getResponse().setContentType("application/x-msdownload");// 不同类型的文件对应不同的MIME类型
        inv.getResponse().setHeader("Content-Disposition", "attachment; filename="+fileName);
        Files.copy(new File(filePath),inv.getResponse().getOutputStream());
        inv.getResponse().flushBuffer();
        inv.getResponse().getOutputStream().flush();
    }

    public Reply test(@Param("id")Long id){
        logger.info("test order id is  {}", id);
        Order order = orderRepository.findOne(id);
        return Replys.with(order.toString()).as(Text.class);
    }
    
    //模糊查询订单号
    @Post("vagueSeachOrder")
    public Reply vagueSeachOrder( @Param("orderNo") String orderNo,Invocation inv){
    	String orderSQLStr = "%" + orderNo + "%";
    	List<Order> orders = orderRepository.vagueSeachOrder(orderSQLStr);
    	return Replys.with(orders).as(Json.class);
    }

}
