package org.one.gene.domain.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerProductOperation;
import org.one.gene.domain.entity.PrimerType;
import org.one.gene.excel.OrderCaculate;
import org.one.gene.excel.OrderExcelPase;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.one.gene.web.order.AtomicLongUtil;
import org.one.gene.web.order.OrderInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
	
	/**
	 * 订单入库
	 * @param customer
	 * @param order
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@Transactional(readOnly=false)
    public void save(Order order){

    	//存储订单数据
    	//外部订单号何处收集,这里不能设置外部订单号！！！
    	order.setOutOrderNo(order.getOrderNo());
    	order.setModifyTime(new Date());
    	order.setStatus(Byte.parseByte("0"));//初始状态
        if(order.getCreateTime() == null){
            order.setCreateTime(new Date());
        }
    	
    	//存储生产数据
        for (PrimerProduct primerProduct : order.getPrimerProducts()) {
            //后续补充，获取登录操作人员的归属机构。
            primerProduct.setComCode("11000000");
            primerProduct.setOperationType(PrimerType.PrimerStatusType.orderInit);//!!!是初始状态不是审核通过状态
            primerProduct.setOrder(order);
            primerProduct.getPrimerProductOperations().add(createPrimerProductOperation(primerProduct));
        }
       
        
        orderRepository.save(order);

    }

	
	public Customer findCustomer(String customerCode){
		Customer customer = new Customer();
    	//解析数据展示列表,第一个sheet客户信息不解析获取
    	//orderExcelPase.ReadExcel(customerCode,path, 0, "4-",new String[] {"b","i"});
    	//excel验证通过，根据客户ID查询客户信息，并解析订单数据存储
    	if(orderExcelPase.isIncludedChinese(customerCode)) {
    		customer = customerRepository.findByNameLike(customerCode);
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
		OrderInfo OrderInfo = new OrderInfo();
		List<PrimerProduct> PrimerProducts = new ArrayList<PrimerProduct>();
		
		//组织订单列表对象
		OrderInfo.setOrderNo(order.getOrderNo());
		OrderInfo.setCustomerName(order.getCustomerName());
		OrderInfo.setCreateTime(order.getCreateTime());
		OrderInfo.setModifyTime(order.getModifyTime());
		OrderInfo.setStatus(String.valueOf(order.getStatus()));
		PrimerProducts = order.getPrimerProducts();
		BigDecimal tbnTotal = new BigDecimal("0");
		//定义订单集合中第一个生产代码
		String firstProductNO = "";
		//定义订单集合中最后一个生产代码
		String LastProductNO  = "";
		int count = 0;
		for(PrimerProduct primerProduct:PrimerProducts){
			count++;
			 if(count==1) {
				 firstProductNO = primerProduct.getProductNo();
		     }
			 if(count==PrimerProducts.size()) {
				 LastProductNO = "~"+primerProduct.getProductNo();
		     }
			 //计算每条生产数据的碱基数
			String tbnStr = orderCaculate.getAnJiShu(primerProduct.getGeneOrder());
			//汇总碱基数
		    tbnTotal= tbnTotal.add(new BigDecimal(tbnStr));
		    
		}
		OrderInfo.setProductNoMinToMax(firstProductNO+LastProductNO);
		OrderInfo.setTbnTotal(tbnTotal.toString());
		
		return OrderInfo;
	}
	
	
	/**
     * 组织订单对象
     * @param customer
     * @param fileName
     * @return
     * @throws ParseException 
     */
    public Order convertOrder(Customer customer,String fileName,Order order) throws ParseException{
    	order.setOrderNo(atomicLongUtil.getOrderSerialNo());
    	
    	order.setCustomerCode(customer.getCode());
    	order.setCustomerName(customer.getName());
    	//后续补充，获取登录操作人员的归属机构。
    	order.setComCode("11000000");
    	order.setType("00");
    	order.setFileName(fileName);
    	order.setCreateTime(new Date());
    	order.setValidate(true);
    	
    	return order;
    }
    

    public PrimerProductOperation createPrimerProductOperation(PrimerProduct primerProduct) {
    	PrimerProductOperation primerProductOperation = new PrimerProductOperation();
    	primerProductOperation.setPrimerProduct(primerProduct);
    	primerProductOperation.setType(PrimerType.PrimerOperationType.orderInit);
    	primerProductOperation.setTypeDesc(PrimerType.PrimerOperationType.orderInit.desc());
    	primerProductOperation.setBackTimes(0);
    	//默认值，后续调整
    	primerProductOperation.setUserCode("19820833");
    	primerProductOperation.setUserName("检查工2号");
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
    
    public Order ReadExcel(String path, int sheetIndex, String rows,Order order) {
    	return orderExcelPase.ReadExcel(path, 1,"2-",order);
    }
    
    public ArrayList<String> getExcelPaseErrors(String path,int ignoreRows, int sheetIndex) throws FileNotFoundException, IOException{
    	return orderExcelPase.getExcelPaseErrors(path,1,2);
    }
    
    /**
     * 订单审核
     * @param orderNo
     */
    public void examine(String orderNo,String failReason){
    	Order order = orderRepository.findByOrderNo(orderNo);
    	for (PrimerProduct primerProduct : order.getPrimerProducts()) {
    		primerProduct.setOperationType(PrimerType.PrimerStatusType.orderCheck);
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
    	orderRepository.save(order);
    }
  
}
