package org.one.gene.web.order;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.repository.OrderRepository;
import org.one.gene.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    
    @Post("upload")
    public String upload(@Param("customerCode") String customerCode, @Param("file") MultipartFile file, Invocation inv) throws Exception {

    	ArrayList<String> errors = new ArrayList<String>();
    	
    	if("".equals(customerCode)){
    		throw new Exception("客户代码为空，请您录入客户代码！");
    	}
    	if (file.isEmpty()) {
    		throw new Exception("请您选择要上传的文件！");
    	}
    	
    	//取得当前所在年份
		Calendar nowCalendar = Calendar.getInstance();
		int year = nowCalendar.get(Calendar.YEAR);	
		int month = nowCalendar.get(Calendar.MONTH)+1;
		int day = nowCalendar.get(Calendar.DAY_OF_MONTH);

		String realpathdir = "";
    	String path="";//暂时按照只上传一个文件定义
    	String filename = "";
    	if (!file.isEmpty()) { 
        	filename = file.getOriginalFilename();
        	realpathdir = inv.getServletContext().getRealPath("/")+"upExcel/"+year+month+day+"/";
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
		        inv.addModel("errors", errors);
	        }
    	}
        if(errors.size()>0){
        	return "upLoadFail";
        }else{
        	//获取客户信息
        	Customer customer = orderService.findCustomer(customerCode);
        	//组织订单对象
        	Order order = orderService.convertOrder(customer,filename);
        	//解析产品信息
        	ArrayList<PrimerProduct>  primerProducts = orderService.ReadExcel(path, 1,"2-");
        	//推送页面赋值
        	inv.addModel("customer", customer);
        	inv.addModel("order", order);
        	inv.addModel("primerProducts", primerProducts);
        	return "upLoadModify";
        }
        
    }
    
    @Post("save")
    public String save(@Param("primerProductList") PrimerProductList primerProductList, @Param("customer") Customer customer, @Param("order") Order order,Invocation inv) throws IllegalStateException, IOException {

    	orderService.save(primerProductList, customer, order);

        return "";
    }
    
    @Post("query")
    public String query(@Param("orderNo") String orderNo, @Param("customerCode") String customerCode,Invocation inv) throws IllegalStateException, IOException {

    	orderService.query(orderNo, customerCode);
        return "";
    }

    public Reply test(){
        Order order = orderRepository.findOne(1l);
        return Replys.with(order.toString()).as(Text.class);
    }
    
    

}
