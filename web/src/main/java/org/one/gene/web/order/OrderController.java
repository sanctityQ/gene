package org.one.gene.web.order;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;
import org.one.gene.domain.service.OrderService;
import org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
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
    @Autowired
    private CustomerRepository customerRepository;

    @Get("import")
    public String orderImport(Invocation inv){
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String customerFlag = user.getUser().getCustomer().getCustomerFlag();
		inv.addModel("customerFlag", customerFlag);// 用户归属公司标识，0-梓熙，1-代理公司，2-直接客户
        return "orderImport";
    }
    
    @Get("orderList")
    @Post("orderList")
    public String orderList(Invocation inv){
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String customerFlag = user.getUser().getCustomer().getCustomerFlag();
		inv.addModel("customerFlag", customerFlag);// 用户归属公司标识，0-梓熙，1-代理公司，2-直接客户
        return "orderList";
    }
    
    @Get("orderExamine")
    public String orderExamine(Invocation inv){
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String customerFlag = user.getUser().getCustomer().getCustomerFlag();
		inv.addModel("customerFlag", customerFlag);// 用户归属公司标识，0-梓熙，1-代理公司，2-直接客户
        return "orderExamine";
    }
    
    @Post("upload")
    public String upload(@Param("customerid") String customerid, @Param("file") MultipartFile file, Invocation inv) throws Exception {
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String customerFlag = user.getUser().getCustomer().getCustomerFlag();
    	
    	ArrayList<String> errors = new ArrayList<String>();
    	
		if ("".equals(customerid) && "0".equals(customerFlag)) {
    		//刷新时赋值客户类型
    		inv.addModel("customerFlag", customerFlag);
    		inv.addModel("userExp", "无此客户信息，请您确认后重新上传！");
    		return "orderImport";
    		//throw new Exception("客户代码或名称未录入，请您录入！");
    	}
    	try{
	    	if (file.isEmpty()) {
	    		//刷新时赋值客户类型
	    		inv.addModel("customerFlag", customerFlag);
	    		inv.addModel("userExp", "请您选择要上传的文件！");
	    		return "orderImport";
	    	}
    	}catch(Exception e){
    		//刷新时赋值客户类型
    		inv.addModel("customerFlag", customerFlag);
    		inv.addModel("userExp", "请您选择要上传的文件！");
    		return "orderImport";
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
		        errors = orderService.getExcelPaseErrors(path,3,1);
	        }
    	}
        if(errors.size()>0){
        	inv.addModel("errors", errors);
        	return "importError";
        }else{
        	//获取客户信息
        	Customer customer = null;
			if ("0".equals(customerFlag)) {
        		customer = customerRepository.findOne(Long.parseLong(customerid));
        	}else{
        		customer = user.getUser().getCustomer();
        	}
        			
        	if(customer==null){
        		//刷新时赋值客户类型
        		inv.addModel("customerFlag", customerFlag);
        		inv.addModel("userExp", "无此客户信息，请您确认后重新上传！");
        		return "orderImport";
        		//throw new Exception("无此客户信息，请您确认后重新上传！");
        	}
        	//组织订单对象
        	Order order = new Order();
        	//获取外部订单号
        	String outOrderNo = orderService.getOutOrderNo(path,1,1);
        	order.setOutOrderNo(outOrderNo);
        	order = orderService.ReadExcel(path, 0,"4-",order,customer);
        	orderService.convertOrder(customer,filename,order);
        	//保存订单信息
        	try{
        	  orderService.save(order);
        	}catch(Exception e){
        		//刷新时赋值客户类型
        		inv.addModel("customerFlag", customerFlag);
        		inv.addModel("userExp",e.getMessage());
        		return "orderImport";
        	}
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
    public String modifyQuery(@Param("orderNo") String orderNo, @Param("forwordName") String forwordName,Invocation inv) throws Exception {
    	 Order order = orderRepository.findByOrderNo(orderNo);
    	 String coustomerCode = order.getCustomerCode();
     	 Customer customer = orderService.findCustomer(coustomerCode);
     	 inv.addModel("customer", customer);
		 inv.addModel("order", order);
    	 return forwordName;
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
	    if(customer==null){
	    	customer = new Customer();
	    }
	    
    	 for(PrimerProduct primerProduct:order.getPrimerProducts()){
    		orderService.addNewValue(primerProduct);
    		primerProduct.setOperationTypeDesc(primerProduct.getOperationType().desc());
    		//修饰
			String midi = "";
			if (!"".equals(primerProduct.getModiFiveType())) {
				midi += primerProduct.getModiFiveType()+",";
			}
			if (!"".equals(primerProduct.getModiMidType())) {
				midi += primerProduct.getModiMidType()+",";
			}
			if (!"".equals(primerProduct.getModiSpeType())) {
				midi += primerProduct.getModiSpeType()+",";
			}
			if (!"".equals(primerProduct.getModiThreeType())) {
				midi += primerProduct.getModiThreeType()+",";
			}
			if(!"".equals(midi)){
				midi = "("+midi.substring(0, midi.length()-1)+")";
				primerProduct.setMidi(midi);
			}
			//状态处理
			for (PrimerStatusType type : PrimerStatusType.values()) {
				if (primerProduct.getOperationType() == type) {
					primerProduct.setOperationTypeDesc(type.desc());
					break;
				}
			}
    	 }

        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCustomer(customer);
        orderInfo.setOrder(order);
        return Replys.with(orderInfo).as(Json.class);
    }
    
    
    @Post("save")
    public Reply save(@Param("primerProducts") List<PrimerProduct> primerProducts,@Param("orderNo") String orderNo,Invocation inv) throws Exception{
    	Order order = orderRepository.findByOrderNo(orderNo);
        Map<Long, PrimerProduct> newPrimerProductMap = Maps.newHashMap();
        for (PrimerProduct primerProduct : primerProducts) {
        	int count = primerProductRepository.countByProductNo(primerProduct.getProductNo());
        	if(count>0&&primerProduct.getId()==null){
        		throw new Exception("您提交的生产编号存在重复，请修改后重新提交！");
        	}
            newPrimerProductMap.put(primerProduct.getId(),primerProduct);
        }
        for (PrimerProduct primerProduct : order.getPrimerProducts()) {
            PrimerProduct newPrimerProduct = newPrimerProductMap.get(primerProduct.getId());
            if (newPrimerProduct != null) {
                primerProduct.setNmolTB(newPrimerProduct.getNmolTB());
                primerProduct.setNmolTotal(newPrimerProduct.getNmolTotal());
                primerProduct.setProductNo(newPrimerProduct.getProductNo());
                primerProduct.setPrimeName(newPrimerProduct.getPrimeName());
                primerProduct.setGeneOrder(newPrimerProduct.getGeneOrder());
                primerProduct.setTbn(newPrimerProduct.getTbn());
                primerProduct.setPurifyType(newPrimerProduct.getPurifyType());
                primerProduct.setModiFiveType(newPrimerProduct.getModiFiveType());
                primerProduct.setModiThreeType(newPrimerProduct.getModiThreeType());
                primerProduct.setModiMidType(newPrimerProduct.getModiMidType());
                primerProduct.setModiSpeType(newPrimerProduct.getModiSpeType());
                primerProduct.setModiPrice(newPrimerProduct.getModiPrice());
                primerProduct.setBaseVal(newPrimerProduct.getBaseVal());
                primerProduct.setPurifyVal(newPrimerProduct.getPurifyVal());
                primerProduct.setTotalVal(newPrimerProduct.getTotalVal());
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
    public Reply query(@Param("orderNo") String orderNo, 
    		@Param("customerCode") String customerCode,
			@Param("createStartTime") String createStartTime,
			@Param("createEndTime") String createEndTime,
			@Param("productNo") String productNo,
			@Param("status") String status,
    		@Param("pageNo")Integer pageNo,
            @Param("pageSize")Integer pageSize,
            Invocation inv) throws Exception {

        if(pageNo == null || pageNo ==0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 20;
        }
        
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String comCode = user.getUser().getCompany().getComCode();
    	String customerFlag = user.getUser().getCustomer().getCustomerFlag();
    	
        if(productNo == null || "".equals(productNo)){
        	Sort s=new Sort(Direction.DESC, "createTime");
        	Pageable pageable = new PageRequest(pageNo-1,pageSize,s);
        	Map<String,Object> searchParams = Maps.newHashMap();
        	searchParams.put(SearchFilter.Operator.EQ+"_orderNo",orderNo);
			if (!"0".equals(customerFlag)) {//代理公司和直接客户，只能查自己公司的业务
				searchParams.put(SearchFilter.Operator.EQ+"_customerCode",user.getUser().getCustomer().getCode());
			} else {
				searchParams.put(SearchFilter.Operator.EQ+"_customerCode",customerCode);
			}
        	searchParams.put(SearchFilter.Operator.EQ+"_status",status);
        	if(!"1".equals(user.getUser().getCompany().getComLevel())){
        		searchParams.put(SearchFilter.Operator.EQ+"_comCode",comCode);
        	}
        	if (createStartTime != null && !"".equals(createStartTime)) {
        		searchParams.put(SearchFilter.Operator.GT+"_createTime",new Date(createStartTime+" 00:00:00"));
        	}
        	if (createEndTime != null && !"".equals(createEndTime)) {
        		searchParams.put(SearchFilter.Operator.LT+"_createTime",new Date(createEndTime+" 59:59:59"));
        	}
        	
        	Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        	Specification<Order> spec = DynamicSpecifications.bySearchFilter(filters.values(), Order.class);
        	
        	Page<Order> orderPage = orderRepository.findAll(spec,pageable);
        	
        	return Replys.with(orderPage).as(Json.class);
        }else{
        	 //生产编码不为空，只查询生产信息表的数据
        	PrimerProduct primerProduct = primerProductRepository.findByProductNo(productNo);
        	if (!"0".equals(customerFlag)) {//代理公司和直接客户，只能查自己公司的业务
        		if (primerProduct != null && primerProduct.getOrder().getCustomerCode().equals(user.getUser().getCustomer().getCode())) {
        			orderService.addNewValue(primerProduct);
        			primerProduct.setOperationTypeDesc(primerProduct.getOperationType().desc());
        			return Replys.with(primerProduct).as(Json.class);
        		} else {
        			return Replys.with("error").as(Json.class);
        		}
        	} else {
        		if (primerProduct != null) {
        			orderService.addNewValue(primerProduct);
        			primerProduct.setOperationTypeDesc(primerProduct.getOperationType().desc());
        			if(!"1".equals(user.getUser().getCompany().getComLevel())){//梓熙 总公司，可以查
        				return Replys.with(primerProduct).as(Json.class);
        			}else{
						if (primerProduct.getComCode().equals(comCode)) {//梓熙 分公司，只可以查分公司的业务
							return Replys.with(primerProduct).as(Json.class);
        				}else{
        					return Replys.with("error").as(Json.class);
        				}
        			}
        		} else {
        			return Replys.with("error").as(Json.class);
        		}
        		
        	}
        }
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
    
    @Post("lookQuery")
    @Get("lookQuery")
    public String lookQuery(@Param("orderNo") String orderNo, Invocation inv) throws Exception {
    	 Order order = orderRepository.findByOrderNo(orderNo);
    	 String coustomerCode = "";
     	 coustomerCode = order.getCustomerCode();
     	 Customer customer = orderService.findCustomer(coustomerCode);
     	 inv.addModel("customer", customer);
		 inv.addModel("order", order);
    	 return "orderView";
    }

    
    /**
     * 订单查询 用于发货处理列表
     * @param orderNo
     * @param customerCode
     * @param pageNo
     * @param pageSize
     * @param inv
     * @return
     * @throws Exception
     */
    @Post("queryDeliveryDeal")
	public Reply queryDeliveryDeal(@Param("orderNo") String orderNo,
			@Param("customerCode") String customerCode,
			@Param("pageNo") Integer pageNo,
			@Param("pageSize") Integer pageSize, Invocation inv)
			throws Exception {

        if(pageNo == null || pageNo ==0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 20;
        }
        
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String comCode = user.getUser().getCompany().getComCode();
        if("1".equals(user.getUser().getCompany().getComLevel())){
          comCode = "";
        }
        
        Pageable pageable = new PageRequest(pageNo-1,pageSize);
        
        Page<Order> orderPage = orderRepository.queryDeliveryDeal(orderNo, customerCode, comCode, pageable);
        //循环查询生产数据表
        for (Order order : orderPage.getContent()) {
        	order.setPrimerProducts(primerProductRepository.findByOrder(order));
        }
        Page<OrderInfo> orderListPage = orderService.convertOrderList(orderPage,pageable);
        
        return Replys.with(orderListPage).as(Json.class);
    }
    
    /**
     * 发货清单 查询列表
     * @param orderNo
     * @param customerCode
     * @param createStartTime
     * @param createEndTime
     * @param pageNo
     * @param pageSize
     * @param inv
     * @return
     * @throws Exception
     */
    @Post("queryDeliveryList")
    public Reply queryDeliveryList(
			@Param("orderNo") String orderNo,
			@Param("customerName") String customerName,
			@Param("createStartTime") String createStartTime,
			@Param("createEndTime") String createEndTime,
    		@Param("pageNo")Integer pageNo,
            @Param("pageSize")Integer pageSize,Invocation inv) throws Exception {

        if(pageNo == null || pageNo ==0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 20;
        }
        
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String comCode = user.getUser().getCompany().getComCode();
        String customerFlag = user.getUser().getCustomer().getCustomerFlag();
        
        Sort s=new Sort(Direction.DESC, "createTime");
        Pageable pageable = new PageRequest(pageNo-1,pageSize,s);
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchFilter.Operator.EQ+"_orderNo",orderNo);
		if (!"0".equals(customerFlag)) {//代理公司和直接客户，只能查自己公司的业务
			searchParams.put(SearchFilter.Operator.EQ+"_customerCode",user.getUser().getCustomer().getCode());
		} else {
			searchParams.put(SearchFilter.Operator.EQ+"_customerName",customerName);
		}
        searchParams.put(SearchFilter.Operator.EQ+"_status","1");//订单审核通过
		if (!"".equals(createStartTime)) {
        	searchParams.put(SearchFilter.Operator.GT+"__createTime",new Date(createStartTime+" 00:00:00"));
        }
		if (!"".equals(createEndTime)) {
        	searchParams.put(SearchFilter.Operator.LT+"__createTime",new Date(createEndTime+" 59:59:59"));
        }
        if(!"1".equals(user.getUser().getCompany().getComLevel())){
            searchParams.put(SearchFilter.Operator.EQ+"__comCode",comCode);
         }
        
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Order> spec = DynamicSpecifications.bySearchFilter(filters.values(), Order.class);
        
        Page<Order> orderPage = orderRepository.findAll(spec,pageable);
        Page<OrderInfo> orderListPage = orderService.convertOrderList(orderPage,pageable);
        
        return Replys.with(orderListPage).as(Json.class);
    }
    
    
}
