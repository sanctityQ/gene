package org.one.gene.web.order;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.excel.OrderExcelPase;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.OrderRepository;
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
    private OrderRepository orderRepository;
    
    @Autowired
    private OrderExcelPase orderExcelPase;
    
    @Autowired
    private CustomerRepository customerRepository;

    @Post("upload")
    public String upload(@Param("customerCode") String customerCode, @Param("file") MultipartFile[] files, Invocation inv) throws Exception {

    	ArrayList<String> errors = new ArrayList<String>();
    	
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
        	//直接组织order订单对象。在controller中写这样代码勿喷。懒
        	Order order = new Order();
        	order.setOrderNo("");
        	order.setOutOrderNo("");
        	ArrayList<PrimerProduct>  primerProducts = orderExcelPase.ReadExcel(path, 1,"2-");
        	inv.addModel("customer", customer);
        	inv.addModel("primerProducts", primerProducts);
        	return "upLoadModify";
        }
        
    }
    
    @Post("save")
    public String save(@Param("primerProductList") PrimerProductList primerProductList, @Param("customer") Customer customer, Invocation inv) throws IllegalStateException, IOException {

        System.out.println(customer.getCode());
        for (PrimerProduct primerProduct : primerProductList.getPrimerProducts()) {
        	System.out.println(primerProduct.getGeneOrder());
        }

        return "";
    }

    public Reply test(){
        Order order = orderRepository.findOne(1l);
        return Replys.with(order.toString()).as(Text.class);
    }

}
