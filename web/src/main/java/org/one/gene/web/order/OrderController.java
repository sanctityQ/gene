package org.one.gene.web.order;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.CustomerPrice;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductOperation;
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.domain.entity.PrimerValueType;
import org.one.gene.domain.entity.PrimerType.PrimerStatusType;
import org.one.gene.domain.entity.ProductMolecular;
import org.one.gene.domain.service.OrderService;
import org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.CustomerPriceRepository;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductOperationRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.ProductMolecularRepository;
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
    @Autowired
    private PrimerProductOperationRepository primerProductOperationRepository;
    @Autowired
    private CustomerPriceRepository customerPriceRepository;
    @Autowired
    private ProductMolecularRepository productMolecularRepository;
    
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
		inv.addModel("orderStatus", "");//区分普通信息查询或者审核通过后的查询
        return "orderList";
    }
    
    //已审核通过的订单，修改信息
    @Get("orderAudit")
    @Post("orderAudit")
    public String orderAudit(Invocation inv){
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String customerFlag = user.getUser().getCustomer().getCustomerFlag();
		inv.addModel("customerFlag", customerFlag);// 用户归属公司标识，0-梓熙，1-代理公司，2-直接客户
		inv.addModel("orderStatus", "1");//区分普通信息查询或者审核通过后的查询
        return "orderList";
    }
    
    @Get("orderExamine")
    public String orderExamine(Invocation inv){
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String customerFlag = user.getUser().getCustomer().getCustomerFlag();
		inv.addModel("customerFlag", customerFlag);// 用户归属公司标识，0-梓熙，1-代理公司，2-直接客户
        return "orderExamine";
    }
    
    //上传文件方法
    @Post("upload")
	public String upload(@Param("customerid") String customerid,
			@Param("contactsname") String contactsname,
			@Param("file") MultipartFile file, Invocation inv) throws Exception {
    	
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String customerFlag = user.getUser().getCustomer().getCustomerFlag();
    	
		if (contactsname == null) {
			contactsname = "";
		}
    	
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
		        errors = orderService.getExcelPaseErrors(path,1,1);
	        }
    	}
        if(errors.size()>0){
        	inv.addModel("errors", errors);
        	return "importError";
        }else{
        	//获取客户信息
        	Customer customer = null;
			if ("0".equals(customerFlag)) {//梓熙用户，需要现查客户信息
        		customer = customerRepository.findOne(Long.parseLong(customerid));
        	}else{//代理或直接用户，直接使用客户信息
        		customer = user.getUser().getCustomer();
        	}
        			
        	if(customer==null){
        		//刷新时赋值客户类型
        		inv.addModel("customerFlag", customerFlag);
        		inv.addModel("userExp", "无此客户信息，请您确认后重新上传！");
        		return "orderImport";
        	}
        	List<CustomerPrice> customerPrices = customerPriceRepository.selectByCustomerId(customer.getId());
        	
        	if (customerPrices == null || customerPrices.size() == 0) {
        		inv.addModel("customerFlag", customerFlag);
        		inv.addModel("userExp", "客户价格信息缺失，请联系管理员进行配置！");
        		return "orderImport";
        	}
        	
        	String prefix = customer.getPrefix().toUpperCase();//生产编号开头
        	//直接客户使用梓熙的配置
			if ("2".equals(customer.getCustomerFlag())) {
				List<Customer> customers = customerRepository.seachHaveZiXi();
				if (customers != null && customers.size() > 0) {
					Customer customerTemp = (Customer)customers.get(0);
					prefix = customerTemp.getPrefix().toUpperCase();
				}
			}
        	
			//组织修饰基础数据
        	Map<String,String> modiFiveMap = new HashMap<String,String>();
        	Map<String,String> modiThreeMap = new HashMap<String,String>();
			Map<String,String> modiMidMap = new HashMap<String,String>();  
			Map<String,String> modiSpeMap = new HashMap<String,String>();
			
			List<ProductMolecular> productMoleculars = (List<ProductMolecular>) productMolecularRepository.findAll();
			
			String modiMidArr = "";
			String modiSpeArr = "";
			
			if (productMoleculars != null && productMoleculars.size() > 0) {
				for (ProductMolecular pm : productMoleculars) {
					if (pm.getValidate().equals("1")) {
						if (pm.getProductCategories().equals("modiFiveType")) {
							modiFiveMap.put(pm.getProductCode(), pm.getModifiedMolecular()+"");
						}else if (pm.getProductCategories().equals("modiThreeType")) {
							modiThreeMap.put(pm.getProductCode(), pm.getModifiedMolecular()+"");
						}else if (pm.getProductCategories().equals("modiMidType")) {
							modiMidArr = modiMidArr + pm.getProductCode() + ",";
							modiMidMap.put(pm.getProductCode(), pm.getModifiedMolecular()+"");
						} else if (pm.getProductCategories().equals("modiSpeType")) {
							modiSpeArr = modiSpeArr + pm.getProductCode() + ",";
							modiSpeMap.put(pm.getProductCode(), pm.getModifiedMolecular()+"");
						}
					}
				}
			}
        	
        	Order order = new Order();
        	int index = 1;
        	String orderNoStr = "";
        	
			try {
				// 获取外部订单号
				ArrayList<Order> orders = orderService.ReadExcel(path, 0, "2-",
						prefix, customerPrices, modiFiveMap, modiThreeMap,	modiMidMap, modiSpeMap);
				orderService.convertOrder(customer, filename, orders, contactsname);
				
				// 保存订单信息
				orderService.save(orders, 0);
                
				//组织页面多个订单串
				for (Order o : orders) {
					orderNoStr += o.getOrderNo() + "^";
					if (index == 1) {
						order = o;
					}
					index++;
				}
				orderNoStr = orderNoStr.substring(0, orderNoStr.length() - 1);

			} catch (Exception e) {
				// 刷新时赋值客户类型
				inv.addModel("customerFlag", customerFlag);
				inv.addModel("userExp", e.getMessage());
				return "orderImport";
			}

			inv.getResponse().setContentType("text/html");
    		inv.addModel("modiMidArr", modiMidArr);
    		inv.addModel("modiSpeArr", modiSpeArr);
    		inv.addModel("orderNoStr", orderNoStr);
    		inv.addModel("customer", customer);
    		inv.addModel("order", order);
    		inv.addModel("orderStatus", "0");
    		//jsp获取未生效生成datagrid表格，后续优化
    		inv.addModel("total", order.getPrimerProducts().size());
    		inv.addModel("reSultdata", JSONObject.toJSONString(order.getPrimerProducts()));
        	return "orderInfo";
        }
        
    }
    
    @Post("modifyQuery")
    @Get("modifyQuery")
	public String modifyQuery(@Param("orderNo") String orderNo,
			@Param("orderNoStr") String orderNoStr,
			@Param("forwordName") String forwordName,
			@Param("orderStatus") String orderStatus,
			Invocation inv)
			throws Exception {
    	 Order order = orderRepository.findByOrderNo(orderNo);
    	 String coustomerCode = order.getCustomerCode();
     	 Customer customer = orderService.findCustomer(coustomerCode);
     	 
			List<ProductMolecular> productMoleculars = (List<ProductMolecular>) productMolecularRepository.findAll();
			
			String modiMidArr = "";
			String modiSpeArr = "";
			
			if (productMoleculars != null && productMoleculars.size() > 0) {
				for (ProductMolecular pm : productMoleculars) {
					if (pm.getValidate().equals("1")) {
						if (pm.getProductCategories().equals("modiMidType")) {
							modiMidArr = modiMidArr + pm.getProductCode() + ",";
						} else if (pm.getProductCategories().equals("modiThreeType")) {
							modiSpeArr = modiSpeArr + pm.getProductCode() + ",";
						}
					}
				}
			}
			
     	 inv.addModel("customer", customer);
		 inv.addModel("order", order);
 		 inv.addModel("modiMidArr", modiMidArr);
 		 inv.addModel("modiSpeArr", modiSpeArr);
 		 inv.addModel("orderStatus", order.getStatus()+"");
		 if (orderNoStr == null || "".equals(orderNoStr)) {
			inv.addModel("orderNoStr", order.getOrderNo());
		 } else {
			 inv.addModel("orderNoStr", orderNoStr);
		 }
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
    
    /**
     * @param pps_insert  页面新增数据
     * @param pps_update  页面修改数据
     * @param orderNo     订单号
     * @param orderStatus 
     * @param inv
     * @return
     * @throws Exception
     */
    @Post("save")
	public Reply save(
			@Param("pps_insert") List<PrimerProduct> pps_insert,
            @Param("pps_update") List<PrimerProduct> pps_update,
			@Param("orderNo") String orderNo,
			@Param("orderStatus") String orderStatus, 
			@Param("customerid") Long customerid,
			@Param("customerid_old") Long customerid_old,
			Invocation inv)
			throws Exception {
    	
    	if(orderStatus==null){
   		orderStatus = "";
    	}
    	
    	Order order = orderRepository.findByOrderNo(orderNo);
    	
    	//校验页面生产编号是否重复
        for (PrimerProduct primerProduct : pps_insert) {
        	int count = primerProductRepository.countByProductNo(primerProduct.getProductNo());
        	if(count>0){
        		throw new Exception("您提交的复制后的生产编号存在重复，请修改后重新提交！");
        	}
        }
        for (PrimerProduct primerProduct : pps_update) {
        	int count = primerProductRepository.countByProductNo(primerProduct.getProductNo());
        	if(count>0&&primerProduct.getId()==null){
        		throw new Exception("您提交的生产编号存在重复，请修改后重新提交！");
        	}
        }
        
    	//如果修改过客户，查询出最新的客户信息，赋值order
		if (customerid != customerid_old) {
			Customer customer = customerRepository.findOne(customerid);
			if (customer != null) {
				order.setCustomerCode(customer.getCode());
				order.setCustomerName(customer.getName());
			}
		}
		
        //页面修改的数据
        for (PrimerProduct primerProduct : order.getPrimerProducts()) {
        	for(PrimerProduct pp_update:pps_update){
        		
        		if((primerProduct.getId()+"").equals(pp_update.getId()+"")){
        			
        			for(PrimerProductValue primerProductValue:primerProduct.getPrimerProductValues()){
						PrimerValueType type = primerProductValue.getType();
						if (type.equals(PrimerValueType.odTB)) {// OD/Tube
							primerProductValue.setValue(pp_update.getOdTB());
						}else if(type.equals(PrimerValueType.odTotal)){//od总量
							primerProductValue.setValue(pp_update.getOdTotal());
						}else if(type.equals(PrimerValueType.nmolTB)){//nmole/Tube
							primerProductValue.setValue(pp_update.getNmolTB());
						}else if(type.equals(PrimerValueType.nmolTotal)){//nmole总量
							primerProductValue.setValue(pp_update.getNmolTotal());
						}else if(type.equals(PrimerValueType.baseCount)){//碱基数
							primerProductValue.setValue(pp_update.getTbn());
						}
        			}
        			primerProduct.setOdTB(pp_update.getOdTB());
        			primerProduct.setOdTotal(pp_update.getOdTotal());
                    primerProduct.setNmolTB(pp_update.getNmolTB());
                    primerProduct.setNmolTotal(pp_update.getNmolTotal());
                    primerProduct.setProductNo(pp_update.getProductNo());
                    primerProduct.setPrimeName(pp_update.getPrimeName());
                    primerProduct.setGeneOrder(pp_update.getGeneOrder());
                    primerProduct.setGeneOrderMidi(pp_update.getGeneOrderMidi());
                    primerProduct.setTbn(pp_update.getTbn());
                    primerProduct.setPurifyType(pp_update.getPurifyType());
                    primerProduct.setModiFiveType(pp_update.getModiFiveType());
                    primerProduct.setModiThreeType(pp_update.getModiThreeType());
                    primerProduct.setModiMidType(pp_update.getModiMidType());
                    primerProduct.setModiSpeType(pp_update.getModiSpeType());
                    primerProduct.setModiPrice(pp_update.getModiPrice());
                    primerProduct.setBaseVal(pp_update.getBaseVal());
                    primerProduct.setPurifyVal(pp_update.getPurifyVal());
                    primerProduct.setTotalVal(pp_update.getTotalVal());
        		}
        	}
        }
        
        //页面新增数据
        for (PrimerProduct newPrimerProduct : pps_insert) {
            newPrimerProduct.setOrder(order);
        }

        orderService.saveOrder(order,pps_insert, orderStatus);
        
        int count = primerProductRepository.getCountByOrderNo(orderNo);
        
        return Replys.with("{\"success\":true,\"mess\":\""+count+"条引物数据已保存！\"}").as(Json.class);
    }
    
    /**
     * 订单查询列表:订单信息，订单审核和修改订单信息都使用该方法
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
			@Param("orderStatus") String orderStatus,
			@Param("outOrderNo") String outOrderNo,
			@Param("primeName") String primeName,
    		@Param("pageNo")Integer pageNo,
            @Param("pageSize")Integer pageSize,
            Invocation inv) throws Exception {

        if(pageNo == null || pageNo ==0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 20;
        }
        //orderStatus:examine,订单审核查询，1 已审核通过查询，空 订单信息 查询
        if(orderStatus == null){
        	orderStatus = "";
        }
        if(orderNo == null){
        	orderNo = "";
        }
    	if(customerCode==null){
    		customerCode = "";
    	}
    	if(outOrderNo==null){
    		outOrderNo = "";
    	}
    	if(primeName==null){
    		primeName = "";
    	}
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String comLevel     = user.getUser().getCompany().getComLevel();
    	String comCode = user.getUser().getCompany().getComCode();
    	String customerFlag = user.getUser().getCustomer().getCustomerFlag();


		if (!"0".equals(customerFlag)) {//代理公司和直接客户，只能查自己公司的业务
			customerCode = user.getUser().getCustomer().getCode();
		}
		
    	if (createStartTime != null && !"".equals(createStartTime)) {
    		createStartTime = createStartTime+" 00:00:00";
    	}else{
    		createStartTime = "";
    	}
    	if (createEndTime != null && !"".equals(createEndTime)) {
    		createEndTime = createEndTime+" 23:59:59";
    	}else{
    		createEndTime = "";
    	}
    	Pageable pageable = new PageRequest(pageNo-1,pageSize);
    	
		Page<Order> orderPage = orderRepository.orderListQuery(createStartTime,
				createEndTime, comLevel, comCode, customerCode, orderNo,
				orderStatus, outOrderNo, primeName, pageable);
		
		if ("1".equals(orderStatus)) {//查询已审核通过的订单
			for (Order order : orderPage.getContent()) {
				String flag = "0";//不可以刪除
				int count = primerProductRepository.countByOrderNoAndNoTType(order.getOrderNo(), PrimerStatusType.makeBoard.toString());
				if (count == 0) {
					flag = "1";// 可以刪除
				}
				order.setSelectFlag(flag);
			}
		}
		
    	return Replys.with(orderPage).as(Json.class);
    }

    /**
     * 查询生产数据信息
     * @param productNo
     * @param inv
     * @return
     * @throws Exception
     */
    @Post("primerProductInfo/{productNoStr}/")
    public String primerProductInfo(@Param("productNoStr") String productNo,Invocation inv) throws Exception {

    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String comCode = user.getUser().getCompany().getComCode();
    	String customerFlag = user.getUser().getCustomer().getCustomerFlag();
    	String userExp = "无此生产编号信息，请您重新输入条件！";
    	
    	inv.addModel("customerFlag", customerFlag);// 用户归属公司标识，0-梓熙，1-代理公司，2-直接客户
    	inv.addModel("orderStatus", "");//区分普通信息查询或者审核通过后的查询
    	
    	PrimerProduct primerProduct = primerProductRepository.findByProductNo(productNo);
    	if (!"0".equals(customerFlag)) {//代理公司和直接客户，只能查自己公司的业务
    		if (primerProduct != null && primerProduct.getOrder().getCustomerCode().equals(user.getUser().getCustomer().getCode())) {
    			List<PrimerProductOperation> ppos= primerProductOperationRepository.getInfoByPrimerProductID(primerProduct.getId());
    			primerProduct.setPrimerProductOperations(ppos);
    			orderService.addNewValue(primerProduct);
    			primerProduct.setOperationTypeDesc(primerProduct.getOperationType().desc());
    			inv.addModel("primerProduct", primerProduct);
    			return "primerProductInfo";
    		} else {
    			inv.addModel("userExp", userExp);
    			return "orderList";
    		}
    	} else {
    		if (primerProduct != null) {
    			List<PrimerProductOperation> ppos= primerProductOperationRepository.getInfoByPrimerProductID(primerProduct.getId());
    			primerProduct.setPrimerProductOperations(ppos);
    			orderService.addNewValue(primerProduct);
    			primerProduct.setOperationTypeDesc(primerProduct.getOperationType().desc());
    			if("1".equals(user.getUser().getCompany().getComLevel())){//梓熙 总公司，可以查
    				inv.addModel("primerProduct", primerProduct);
    				return "primerProductInfo";
    			}else{
					if (primerProduct.getComCode().equals(comCode)) {//梓熙 分公司，只可以查分公司的业务
						inv.addModel("primerProduct", primerProduct);
						return "primerProductInfo";
    				}else{
    	    			inv.addModel("userExp", userExp);
    	    			return "orderList";
    				}
    			}
    		} else {
    			inv.addModel("userExp", userExp);
    			return "orderList";
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
	public Object examine(@Param("orderNo") String orderNo,
			@Param("failReason") String failReason, Invocation inv)
			throws Exception {
        orderService.examine(orderNo,failReason);
    	return Replys.with("sucess").as(Text.class);
    }
    
    @Post("vagueSeachCustomer") 
    public Reply vagueSeachCustomer( @Param("seachCustom") String customerCode,Invocation inv){
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String comCodeSQL = user.getUser().getCompany().getComCode();
        if("1".equals(user.getUser().getCompany().getComLevel())){
        	comCodeSQL = "";
        }
    	List<Customer> customers = orderService.vagueSeachCustomer(customerCode,comCodeSQL);
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
			@Param("customerFlagStr") String customerFlagStr,
			@Param("createStartTime") String createStartTime,
			@Param("createEndTime") String createEndTime,
			@Param("pageNo") Integer pageNo,
			@Param("pageSize") Integer pageSize, Invocation inv)
			throws Exception {

        if(pageNo == null || pageNo ==0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 20;
        }
		if (customerFlagStr == null) {
			customerFlagStr = "";
		}
		if (createStartTime == null) {
			createStartTime = "";
		}
		if (createEndTime == null) {
			createEndTime = "";
		}
        
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String comCode = user.getUser().getCompany().getComCode();
        if("1".equals(user.getUser().getCompany().getComLevel())){
          comCode = "";
        }
        
        Pageable pageable = new PageRequest(pageNo-1,pageSize);
        
		Page<Order> orderPage = orderRepository.queryDeliveryDeal(orderNo,
				customerCode, comCode, customerFlagStr, createStartTime,
				createEndTime, pageable);
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
			@Param("productNoPrefix") String productNoPrefix,
			@Param("customerName") String customerName,
			@Param("customerFlagStr") String customerFlagStr,
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
		if (customerFlagStr == null) {
			customerFlagStr = "";
		}
		if (customerName == null) {
			customerName = "";
		}
		if (productNoPrefix == null) {
			productNoPrefix = "";
		}
		
		if (!StringUtils.isBlank(productNoPrefix)) {
			productNoPrefix = productNoPrefix.toUpperCase() + "%";
        }
		
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String comCode      = user.getUser().getCompany().getComCode();
        String customerFlag = user.getUser().getCustomer().getCustomerFlag();
        String comLevel     = user.getUser().getCompany().getComLevel();
        
        Pageable pageable = new PageRequest(pageNo-1,pageSize);

		if (!"".equals(createStartTime)) {
        	createStartTime = createStartTime+" 00:00:00";
        }
		if (!"".equals(createEndTime)) {
			createEndTime = createEndTime+" 23:59:59";
        }
			
		Page<Order> orderPage = orderRepository.queryDeliveryList(
				createStartTime, createEndTime, comLevel, comCode,
				customerFlag, user.getUser().getCustomer().getCode(),
				customerName, orderNo, customerFlagStr, productNoPrefix,pageable);
        
        Page<OrderInfo> orderListPage = orderService.convertOrderList(orderPage,pageable);
        
        return Replys.with(orderListPage).as(Json.class);
    }
    
    
}
