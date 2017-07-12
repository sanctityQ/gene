package org.one.gene.domain.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
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
import org.one.gene.domain.entity.PrimerType;
import org.one.gene.domain.entity.PrimerValueType;
import org.one.gene.domain.entity.ProductMolecular;
import org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser;
import org.one.gene.excel.OrderCaculate;
import org.one.gene.excel.OrderExcelPase;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.one.gene.repository.ProductMolecularRepository;
import org.one.gene.web.order.AtomicLongUtil;
import org.one.gene.web.order.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

@Component
public class OrderService {

	@Autowired
    private AtomicLongUtil atomicLongUtil;
	
	@Autowired
    private CustomerRepository customerRepository;
	
	@Autowired
    private OrderRepository orderRepository;
	
	@Autowired
    private PrimerProductRepository primerProductRepository;
	
	@Autowired
	private PrimerProductValueRepository primerProductValueRepository;
	
	@Autowired
    private OrderExcelPase orderExcelPase;
	
    @Autowired
    private OrderCaculate orderCaculate;
    
    @Autowired
    private PropotiesService propotiesService;
	
	@Autowired
	private ProductMolecularRepository productMolecularRepository;
	
	/**
	 * 订单入库
	 * @param order
	 * @return
	 * @throws Exception 
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@Transactional
    public void save(ArrayList<Order> orders, Integer status) throws Exception{

        //所有分子量
        List<ProductMolecular> productMoleculars = (List<ProductMolecular>) productMolecularRepository.findAll();
        Map<String, BigDecimal> productMolecularMap = Maps.newHashMap();
        for(ProductMolecular pm:productMoleculars){
			if ("1".equals(pm.getValidate())) {
				productMolecularMap.put(pm.getProductCategories()+"_"+pm.getProductCode(), pm.getModifiedMolecular());
			}
        }
        
		for(Order order :orders){
			
			//存储订单数据
			if(order.getOutOrderNo()==null || "".equals(order.getOutOrderNo())){
				order.setOutOrderNo(order.getOrderNo());
			}else{
				order.setOutOrderNo(order.getOutOrderNo());
			}
			order.setModifyTime(new Date());
			order.setStatus(status);//初始状态:0  初始化，1 审核通过，2 审核不通过， 3 订单审核通过后修改过，需要再次审核
			if(order.getCreateTime() == null){
				order.setCreateTime(new Date());
			}
			
			
			int index=0;
			//定义订单集合中第一个生产代码
			String firstProductNO = "";
			//定义订单集合中最后一个生产代码
			String lastProductNO  = "";
			BigDecimal tbnTotal = new BigDecimal("0");
			//存储生产数据
			for (PrimerProduct primerProduct : order.getPrimerProducts()) {
				int count = primerProductRepository.countByProductNo(primerProduct.getProductNo());
				if(count>0&&primerProduct.getId()==null){
					throw new Exception("您提交的生产编号存在重复，请修改后重新提交！");
				}
				//后续补充，获取登录操作人员的归属机构。
				if(status!=3){
					primerProduct.setOperationType(PrimerType.PrimerStatusType.orderInit);//!!!是初始状态不是审核通过状态
					primerProduct.setBackTimes(0);
				}
				primerProduct.setComCode(order.getComCode());
				primerProduct.setOrder(order);
				primerProduct.setModifyTime(new Date());//最新修改时间
				primerProduct.setProductMolecularMap(productMolecularMap);//map放入分子量
				
				
				//获取碱基数
				String tbnStr = orderCaculate.getAnJiShu(primerProduct.getGeneOrder());
				//总价格:修饰单价+碱基单价*碱基数+纯化价格(9+10*碱基数+11)
				BigDecimal totalVal = primerProduct.getModiPrice().add(primerProduct.getBaseVal().multiply(new BigDecimal(tbnStr))).add(primerProduct.getPurifyVal());
				primerProduct.setTotalVal(totalVal);
				
				index++;
				if(index==1) {
					firstProductNO = primerProduct.getProductNo();
				}
				if(index==order.getPrimerProducts().size()) {
					lastProductNO = "~"+primerProduct.getProductNo();
				}
				//汇总碱基数
				tbnTotal= tbnTotal.add(new BigDecimal(tbnStr));
				
				//初始化数据 先实现功能后续优化
				if(primerProduct.getId() == null){
					List<PrimerProductValue> primerProductValueList = new ArrayList<PrimerProductValue>();
					for (PrimerValueType type : PrimerValueType.values()) {
						PrimerProductValue primerProductValue = type.create(primerProduct);
						primerProductValue.setPrimerProduct(primerProduct);
						primerProductValueList.add(primerProductValue);
					}
					primerProduct.setPrimerProductValues(primerProductValueList);
				}
				
				primerProduct.init();
				
				  //分装体积：
				  //干粉： 1.2*ODTB/(导入值/30) == 上传测试附件时
				  if (primerProduct.getLiquid() !=null && !"".equals(primerProduct.getLiquid())) {
					  primerProduct.setMeasureVolume(measureVolume.intValue());
				  }
				
				if (primerProduct.getPrimerProductOperations().isEmpty()) {
					primerProduct.getPrimerProductOperations().add(createPrimerProductOperation(primerProduct));
				}
				for (PrimerProductOperation po : primerProduct.getPrimerProductOperations()) {
					po.setPrimerProduct(primerProduct);
				}
			}
			order.setProductNoMinToMax(firstProductNO+lastProductNO);
			order.setTbnTotal(tbnTotal);
			orderRepository.save(order);
		}

    }

	public List<Customer> vagueSeachCustomer(String customerCode, String comCodeSQL){
    	String sql="";
    	if (!StringUtils.isBlank(customerCode)) {
    	  sql = "%" + customerCode + "%";
        }
		List<Customer> customers = customerRepository.vagueSeachCustomer(sql, comCodeSQL);
		return customers;
	}
	
	public Customer findCustomer(String customerCode){
		Customer customer = new Customer();
    	if(orderExcelPase.isIncludedChinese(customerCode)) {
    		customer = customerRepository.findByName(customerCode);
    	}else{
    		customer = customerRepository.findByCode(customerCode);
    	}
    	
    	return customer;
	}
	
	public Page<OrderInfo>  convertOrderList(Page<Order> orderPage,Pageable pageable) throws IllegalStateException, IOException {
		//生产编号（头尾）、碱基总数
		OrderInfo orderInfo = new OrderInfo();
		List<OrderInfo> orderInfoList = new ArrayList<OrderInfo>();
		
		for(Order order:orderPage.getContent()){
			orderInfo = getOrderInfos(order);
			orderInfoList.add(orderInfo);
		}
		
		Page<OrderInfo> orderListPage = new PageImpl<OrderInfo>(orderInfoList,pageable,orderPage.getTotalElements());
		
        return orderListPage;
    }
	
	public OrderInfo getOrderInfos(Order order){
		OrderInfo orderInfo = new OrderInfo();

		//组织订单列表对象
		orderInfo.setOrderNo(order.getOrderNo());
		orderInfo.setOutOrderNO(order.getOutOrderNo());
		orderInfo.setCustomerName(order.getCustomerName());
		orderInfo.setCreateTime(order.getCreateTime());
		orderInfo.setModifyTime(order.getModifyTime());
		orderInfo.setStatus(String.valueOf(order.getStatus()));
		orderInfo.setProductNoMinToMax(order.getProductNoMinToMax());
		orderInfo.setTbnTotal(order.getTbnTotal());
		orderInfo.setContactsName(order.getContactsName());
		orderInfo.setDeliveryRemark(order.getDeliveryRemark());
		
		return orderInfo;
	}
	
	
	/**
     * 组织订单List对象
     * @param customer
     * @param fileName
     * @return
     * @throws ParseException 
     */
	public ArrayList<Order> convertOrder(Customer customer, String fileName,
			ArrayList<Order> orders, String contactsname) throws ParseException {
		
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	String comCode = user.getUser().getCompany().getComCode();
    	
		for (Order order : orders) {
			order.setOrderNo(atomicLongUtil.getOrderSerialNo());
			order.setCustomerCode(customer.getCode());
			order.setCustomerName(customer.getName());
			order.setContactsName(contactsname);
			order.setComCode(comCode);
			order.setHandlerCode(customer.getHandlerCode());
			order.setHandlerName(customer.getHandlerName());
			order.setType("00");
			order.setFileName(fileName);
			order.setCreateTime(new Date());
			order.setValidate(true);
		}
    	
    	return orders;
    }
    

    public PrimerProductOperation createPrimerProductOperation(PrimerProduct primerProduct) {
    	PrimerProductOperation primerProductOperation = new PrimerProductOperation();
    	primerProductOperation.setPrimerProduct(primerProduct);
    	primerProductOperation.setType(PrimerType.PrimerOperationType.orderInit);
    	primerProductOperation.setTypeDesc(PrimerType.PrimerOperationType.orderInit.desc());
    	primerProductOperation.setBackTimes(0);
    	ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
    	primerProductOperation.setUserCode(user.getUser().getCode());
    	primerProductOperation.setUserName(user.getUser().getName());
    	primerProductOperation.setCreateTime(new Date());
    	primerProductOperation.setFailReason("");
        return primerProductOperation;
    }
    
    @Transactional(readOnly=false)
    public void copy(String productNo) throws Exception{
    	PrimerProduct primerProductTemp = new PrimerProduct();
    	PrimerProduct primerProduct = primerProductRepository.findByProductNo(productNo);
//    	PropertyUtils.copyProperties(primerProductTemp, primerProduct);
    	primerProductTemp.setProductNo(primerProduct.getProductNo()+"1");
    	primerProductTemp.setOrder(primerProduct.getOrder());
    	primerProductTemp.setFromProductNo(primerProduct.getProductNo());
    	primerProductTemp.setPrimeName(primerProduct.getPrimeName());
    	primerProductTemp.setGeneOrder(primerProduct.getGeneOrder());
    	primerProductTemp.setPurifyType(primerProduct.getPurifyType());
    	primerProductTemp.setModiFiveType(primerProduct.getModiFiveType());
    	primerProductTemp.setModiThreeType(primerProduct.getModiThreeType());
    	primerProductTemp.setModiMidType(primerProduct.getModiMidType());
    	primerProductTemp.setModiSpeType(primerProduct.getModiSpeType());
    	primerProductTemp.setModiPrice(primerProduct.getModiPrice());
    	primerProductTemp.setBaseVal(primerProduct.getBaseVal());
    	primerProductTemp.setPurifyVal(primerProduct.getPurifyVal());
    	primerProductTemp.setTotalVal(primerProduct.getTotalVal());
    	primerProductTemp.setRemark(primerProduct.getRemark());
    	primerProductTemp.setOperationType(primerProduct.getOperationType());
    	primerProductTemp.setBoardNo(primerProduct.getBoardNo());
    	primerProductTemp.setComCode(primerProduct.getComCode());
    	primerProductTemp.setBackTimes(primerProduct.getBackTimes());
    	primerProductTemp.setReviewFileName(primerProduct.getReviewFileName());
    	primerProductRepository.save(primerProductTemp);
    }
    
	public ArrayList<Order> ReadExcel(String path, int sheetIndex, String rows,
			String prefix, String newCusFlag, Customer customer,
			List<CustomerPrice> customerPrices,
			Map<String, String> modiFiveMap, Map<String, String> modiThreeMap,
			Map<String, String> modiMidMap, Map<String, String> modiSpeMap)
			throws Exception {
    	return orderExcelPase.ReadExcel(path, sheetIndex,rows,prefix,newCusFlag,customer,customerPrices, modiFiveMap, modiThreeMap, modiMidMap, modiSpeMap);
    }
    
    public ArrayList<String> getExcelPaseErrors(String path,int ignoreRows, int sheetIndex) throws FileNotFoundException, IOException{
    	return orderExcelPase.getExcelPaseErrors(path,ignoreRows,sheetIndex);
    }
    
    public String getOutOrderNo(String path, int sheetIndex, int ignoreRows) throws FileNotFoundException, IOException {
    	return orderExcelPase.getOutOrderNo(path,sheetIndex,ignoreRows);
    }
    
    /**
     * 订单审核
     * @param orderNo
     */
    public void examine(String orderNo,String failReason){
    	Order order = orderRepository.findByOrderNo(orderNo);

    	for (PrimerProduct primerProduct : order.getPrimerProducts()) {
    		
    		primerProduct.setModifyTime(new Date());//最新修改时间
    		
    		if(order.getStatus()==0){//订单初始化状态
    			if(!"".equals(failReason)){
    				primerProduct.setOperationType(PrimerType.PrimerStatusType.orderCheck);
    			}else{
    				primerProduct.setOperationType(PrimerType.PrimerStatusType.makeBoard);//订单审核通过，生产数据到可制板状态
    				primerProduct.setBackTimes(0);
    			}
    			
    			for (PrimerProductOperation primerProductOperation : primerProduct.getPrimerProductOperations()) {
    				if(!"".equals(failReason)){
    					primerProductOperation.setType(PrimerType.PrimerOperationType.orderCheckFailure);
    					primerProductOperation.setTypeDesc(PrimerType.PrimerOperationType.orderCheckFailure.desc());
    					primerProductOperation.setFailReason(failReason);
    				}else{
    					primerProductOperation.setType(PrimerType.PrimerOperationType.orderCheckSuccess);
    					primerProductOperation.setTypeDesc(PrimerType.PrimerOperationType.orderCheckSuccess.desc());
    				}
    			}
    		}
    	}
    	/**
    	 * 0代表初始 1代表审核通过 2代表审核不通过 3审核通过已修改
    	 */
    	if(!"".equals(failReason)){
    		order.setStatus(2);
    	}else{
    		order.setStatus(1);
    	}
    	orderRepository.save(order);
    }

  //补充临时字段的值
  	public void addNewValue(PrimerProduct primerProduct) {
      	for (PrimerProductValue pv : primerProduct.getPrimerProductValues()) {
      		if(pv.getType().equals(PrimerValueType.odTotal)){
      			primerProduct.setOdTotal(pv.getValue());
      		}else if(pv.getType().equals(PrimerValueType.odTB)){
      			primerProduct.setOdTB(pv.getValue());
      		}else if(pv.getType().equals(PrimerValueType.nmolTotal)){
      			primerProduct.setNmolTotal(pv.getValue());
      		}else if(pv.getType().equals(PrimerValueType.nmolTB)){
      			primerProduct.setNmolTB(pv.getValue());
      		}else if(pv.getType().equals(PrimerValueType.baseCount)){
      			primerProduct.setTbn(pv.getValue());
      		}else if(pv.getType().equals(PrimerValueType.tb)){
      			primerProduct.setTb(pv.getValue());
      		}else if(pv.getType().equals(PrimerValueType.MW)){
      			primerProduct.setMw(pv.getValue());
      		}
      	}
  		
  	}
  	
    //保存订单信息方法
	@Transactional
	public void saveOrder(Order order,
			List<PrimerProduct> pps_insert, String orderStatus)
			throws Exception {
		
        //所有分子量
        List<ProductMolecular> productMoleculars = (List<ProductMolecular>) productMolecularRepository.findAll();
        Map<String, BigDecimal> productMolecularMap = Maps.newHashMap();
        for(ProductMolecular pm:productMoleculars){
			if ("1".equals(pm.getValidate())) {
				productMolecularMap.put(pm.getProductCategories()+"_"+pm.getProductCode(), pm.getModifiedMolecular());
			}
        }
        
		//新增数据时comcode赋值
		for (PrimerProduct primerProduct : pps_insert) {
			if("".equals(primerProduct.getComCode())||primerProduct.getComCode()==null){
				primerProduct.setComCode(order.getComCode());
			}
			if("".equals(primerProduct.getOperationType())||primerProduct.getOperationType()==null){
				primerProduct.setOperationType(order.getPrimerProducts().get(0).getOperationType());
			}
			  primerProduct.setBackTimes(0);
			  primerProduct.setModifyTime(new Date());//最新修改时间
			  primerProduct.setProductMolecularMap(productMolecularMap);//map放入分子量
			  order.getPrimerProducts().add(primerProduct);
		}
		
		ArrayList<Order>  orders = new ArrayList<Order>();
		orders.add(order);
		if ("0".equals(orderStatus)) {
			this.save(orders, 0);
		} else {
			this.save(orders, 3);
		}
		
		//this.primerProductRepository.save(values);
	}
}
