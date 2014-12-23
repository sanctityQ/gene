package org.one.gene.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.excel.OrderExcelPase;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.one.gene.web.order.AtomicLongUtil;
import org.one.gene.web.order.OrderInfoList;
import org.one.gene.web.order.PrimerProductList;
import org.springframework.beans.factory.annotation.Autowired;
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
    private OrderExcelPase orderExcelPase;
	
	/**
	 * 订单入库
	 * @param primerProductList
	 * @param customer
	 * @param order
	 * @param inv
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	@Transactional(readOnly=false)
    public String save(PrimerProductList primerProductList,Customer customer,Order order) throws IllegalStateException, IOException {

    	//存储客户信息
    	customerRepository.save(customer);
    	//存储订单数据
    	//外部订单号何处收集 
    	order.setOutOrderNo(order.getOrderNo());
    	order.setModifyTime(new Date());
    	orderRepository.save(order);
    	//存储生产数据
        for (PrimerProduct primerProduct : primerProductList.getPrimerProducts()) {
        	//后续补充，获取登录操作人员的归属机构。
        	primerProduct.setComCode("11000000");
        	primerProduct.setOperationType("1");
        	primerProduct.setOrder(order);
        	primerProductRepository.save(primerProduct);
        }

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
	
	public ArrayList<OrderInfoList> query(String orderNo,String customerCode) throws IllegalStateException, IOException {
		//订单号、客户姓名、生产编号（头尾）、碱基总数、状态、导⼊时间，修改时间
		Order order = new Order();
		Customer customer = new Customer();
		if(!"".equals(orderNo)){
			order = orderRepository.findByOrderNo(orderNo);
			customer = customerRepository.findByCode(order.getComCode());
		}
		if(!"".equals(customerCode)){
			customer = customerRepository.findByCode(customerCode);
			order = orderRepository.findByCustomerCode(customer.getCode());
		}
        return null;
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
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-DD HH:mm:dd");
    	order.setCreateTime(sf.parse(sf.format(new Date())));
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
