package org.one.gene.web.order;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.excel.OrderExcelPase;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Post;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Text;

@Path
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderExcelPase orderExcelPase;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private AtomicLongUtil atomicLongUtil;
    
    @Autowired
    private PrimerProductRepository primerProductRepository;

    @Post("upload")
    public String upload(@Param("customerCode") String customerCode, @Param("file") MultipartFile[] files, Invocation inv) throws Exception {

    	ArrayList<String> errors = new ArrayList<String>();
    	
    	if("".equals(customerCode)){
    		throw new Exception("客户代码为空，请您录入客户代码！");
    	}
    	
    	//取得当前所在年份
		Calendar nowCalendar = Calendar.getInstance();
		int year = nowCalendar.get(Calendar.YEAR);	
		int month = nowCalendar.get(Calendar.MONTH)+1;
		int day = nowCalendar.get(Calendar.DAY_OF_MONTH);

		String realpathdir = "";
    	String path="";//暂时按照只上传一个文件定义
    	String filename = "";
        for (MultipartFile multipartFile : files) {
        	/*if(multipartFile.getSize()==0){
        		throw new Exception("请您选择要上传的文件！");
        	}*/
        	if(multipartFile.getOriginalFilename() == null){
				continue;
			}
        	filename = multipartFile.getOriginalFilename();
        	realpathdir = inv.getServletContext().getRealPath("/")+"upExcel/"+year+month+day+"/";
        	path = realpathdir+filename;
        	
    	    // 创建文件目录
    	    File savedir = new File(realpathdir);
    	    // 如果目录不存在就创建
    	    if (!savedir.exists()) {
    	      savedir.mkdirs();
    	    }
    	    
        	System.out.println(path);
			multipartFile.transferTo(new File(path));
        }
        
        if(!"".equals(path)){
	        errors = orderExcelPase.getExcelPaseErrors(path,1,2);
	        inv.addModel("errors", errors);
        }
        
        if(errors.size()>0){
        	return "upLoadFail";
        }else{
        	Customer customer = new Customer();
        	//解析数据展示列表,第一个sheet客户信息不解析获取
        	//orderExcelPase.ReadExcel(customerCode,path, 0, "4-",new String[] {"b","i"});
        	//excel验证通过，根据客户ID查询客户信息，并解析订单数据存储
        	if(orderExcelPase.isIncludedChinese(customerCode)) {
        		customer = customerRepository.findByNameLike(customerCode);
        	}else{
        		customer = customerRepository.findByCode(customerCode);
        	}
        	//此处直接组织order订单对象。在controller中写这样代码勿喷。懒
        	Order order = this.convertOrder(customer,filename);
        	ArrayList<PrimerProduct>  primerProducts = orderExcelPase.ReadExcel(path, 1,"2-");
        	inv.addModel("customer", customer);
        	inv.addModel("order", order);
        	inv.addModel("primerProducts", primerProducts);
        	return "upLoadModify";
        }
        
    }
    
    @Post("save")
    @Transactional(readOnly=false)
    public String save(@Param("primerProductList") PrimerProductList primerProductList, @Param("customer") Customer customer, @Param("order") Order order,Invocation inv) throws IllegalStateException, IOException {

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

        return "";
    }

    public Reply test(){
        Order order = orderRepository.findOne(1l);
        return Replys.with(order.toString()).as(Text.class);
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

}
