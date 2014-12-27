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
import org.one.gene.domain.entity.PrimerProductValue;
import org.one.gene.domain.entity.PrimerValueType;
import org.one.gene.excel.OrderCaculate;
import org.one.gene.excel.OrderExcelPase;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.repository.PrimerProductValueRepository;
import org.one.gene.web.order.AtomicLongUtil;
import org.one.gene.web.order.OrderInfoList;
import org.one.gene.web.order.PrimerProductList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
	 * @param primerProductList
	 * @param customer
	 * @param order
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@Transactional(readOnly=false)
    public String save(PrimerProductList primerProductList,Customer customer,Order order) throws IllegalStateException, IOException {

		//保存方法重写
		List<String> types = new ArrayList<String>();
		types.add("TM");
		types.add("GC");
		types.add("MW");
		types.add("μgOD");
		types.add("ODUMOL");
		types.add("PMOL");
    	//存储客户信息
    	customerRepository.save(customer);
    	//存储订单数据
    	//外部订单号何处收集 
    	order.setOutOrderNo(order.getOrderNo());
    	order.setModifyTime(new Date());
    	
    	//存储生产数据
        for (PrimerProduct primerProduct : primerProductList.getPrimerProducts()) {
        	//后续补充，获取登录操作人员的归属机构。
        	primerProduct.setComCode("11000000");
        	primerProduct.setOperationType("1");
        	primerProduct.setOrder(order);
        	for(int i=0;i<types.size();i++){
        		PrimerProductValue primerProductValue = new PrimerProductValue();
        		primerProductValue.setPrimerProduct(primerProduct);
        		primerProductValue.setCreateTime(new Date());
        		if(types.get(i).equals("TM")){
        			primerProductValue.setType(PrimerValueType.TM);
        			primerProductValue.setTypeDesc("TM值");
        			primerProductValue.setValue(new BigDecimal(orderCaculate.getTM(primerProduct.getGeneOrder())));
        			primerProduct.getPrimerProductValues().add(primerProductValue);
        		}
        		if(types.get(i).equals("GC")){
        			primerProductValue.setType(PrimerValueType.GC);
        			primerProductValue.setTypeDesc("GC值");
        			primerProductValue.setValue(new BigDecimal(orderCaculate.getGC(primerProduct.getGeneOrder())));
        			primerProduct.getPrimerProductValues().add(primerProductValue);
        		}
        		if(types.get(i).equals("MW")){
        			primerProductValue.setType(PrimerValueType.MW);
        			primerProductValue.setTypeDesc("MW值");
        			primerProductValue.setValue(new BigDecimal(orderCaculate.getMW(primerProduct.getGeneOrder())));
        			primerProduct.getPrimerProductValues().add(primerProductValue);
        		}
        		if(types.get(i).equals("μgOD")){
        			primerProductValue.setType(PrimerValueType.μgOD);
        			primerProductValue.setTypeDesc("μgOD值");
        			primerProductValue.setValue(new BigDecimal(orderCaculate.getg_OD(primerProduct.getGeneOrder(),"100")));
        			primerProduct.getPrimerProductValues().add(primerProductValue);
        		}
        	}
        	order.getPrimerProducts().add(primerProduct);
        }
        
        orderRepository.save(order);

        return "success";
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
	
	public Page<Order>  convertOrderList(List<Order> orders) throws IllegalStateException, IOException {
		//生产编号（头尾）、碱基总数
		/* Page<Order> orderPage = 
		OrderInfoList OrderInfo = new OrderInfoList();
		
		for(Order order:orders){
			OrderInfo = getOrderInfos(order);
			orderInfoList.add(OrderInfo);
		}*/
		
        return null;
    }
	
	public OrderInfoList getOrderInfos(Order order){
		OrderInfoList OrderInfo = new OrderInfoList();
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
				 LastProductNO = primerProduct.getProductNo();
		     }
			 //计算每条生产数据的碱基数
			String tbnStr = orderCaculate.getAnJiShu(primerProduct.getGeneOrder());
			//汇总碱基数
		    tbnTotal= tbnTotal.add(new BigDecimal(tbnStr));
		    
		}
		OrderInfo.setProductNoMinToMax(firstProductNO+"~"+LastProductNO);
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
    public Order convertOrder(Customer customer,String fileName) throws ParseException{
    	Order order = new Order();
    	order.setOrderNo(atomicLongUtil.getOrderSerialNo());
    	
    	order.setCustomerCode(customer.getCode());
    	order.setCustomerName(customer.getName());
    	//后续补充，获取登录操作人员的归属机构。
    	order.setComCode("11000000");
    	order.setStatus(Byte.parseByte("1"));
    	order.setType("00");
    	order.setFileName(fileName);
    	order.setCreateTime(new Date());
    	order.setValidate(true);
    	
    	return order;
    }
    
    public ArrayList<PrimerProduct> ReadExcel(String path, int sheetIndex, String rows) {
    	return orderExcelPase.ReadExcel(path, 1,"2-");
    }
    
    public ArrayList<String> getExcelPaseErrors(String path,int ignoreRows, int sheetIndex) throws FileNotFoundException, IOException{
    	return orderExcelPase.getExcelPaseErrors(path,1,2);
    }
}
