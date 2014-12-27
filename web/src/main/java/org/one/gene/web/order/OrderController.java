package org.one.gene.web.order;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.service.OrderService;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Maps;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Get;
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
    @Autowired
    private PrimerProductRepository primerProductRepository;
    
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
    public String query(@Param("orderNo") String orderNo, @Param("customerCode") String customerCode,@Param("pageNo")Integer pageNo,
                        @Param("pageSize")Integer pageSize,Invocation inv) throws Exception {

        if(pageNo == null){
            pageNo = 0;
        }

        if(pageSize == null){
            pageSize = 5;
        }

        Pageable pageable = new PageRequest(pageNo,pageSize);
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchFilter.Operator.EQ+"_orderNo",orderNo);
        searchParams.put(SearchFilter.Operator.EQ+"_customerCode",customerCode);
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Order> spec = DynamicSpecifications.bySearchFilter(filters.values(), Order.class);
        
        Page<Order> orderPage = orderRepository.findAll(spec,pageable);
        //orderPage = orderService.convertOrderList(orderPage.getContent());
        
    	inv.addModel("page", orderPage);
    	inv.addModel("pageSize", pageSize);
        return "orderInfo";
    }
    
    @Get("modify/{orderNo}") 
    public String modify(@Param("orderNo") String orderNo,Invocation inv){
    	Order order = orderRepository.findByOrderNo(orderNo);
    	Customer customer = orderService.findCustomer(order.getCustomerCode());
    	inv.addModel("customer", customer);
    	inv.addModel("order", order);
    	inv.addModel("primerProducts", order.getPrimerProducts());
    	return "upLoadModify";
    }
    
    @Get("copy/{productNo}") 
    public String copy(@Param("productNo") String productNo,Invocation inv) throws Exception{
    	orderService.copy(productNo);
    	
    	return "";
    }
    
    @Get("downLoad") 
    @Post("downLoad") 
    public String downLoad(Invocation inv) throws Exception{
		String fileName = "views\\downLoad\\template\\订购表模版.xls";
		String filePath = inv.getRequest().getSession().getServletContext().getRealPath(fileName);
		
		System.out.println("文件下载路径filePath::::::"+filePath);
		
        File file=new File(filePath);
        
        InputStream is=new FileInputStream(file);
        OutputStream os=inv.getResponse().getOutputStream();
        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8");// 处理中文文件名的问题
        fileName = new String(fileName.getBytes("UTF-8"), "GBK");// 处理中文文件名的问题
        inv.getResponse().reset();
        inv.getResponse().setContentType("application/x-msdownload");// 不同类型的文件对应不同的MIME类型
        inv.getResponse().setHeader("Content-Disposition", "attachment; filename="+fileName);
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = bis.read(buffer)) != -1){
            bos.write(buffer, 0, bytesRead);// 将文件发送到客户端
        }
        bos.flush();
        bis.close();
        bos.close();
        is.close();
        os.close();
        return null;
    }

    public Reply test(){
        Order order = orderRepository.findOne(1l);
        return Replys.with(order.toString()).as(Text.class);
    }
    
    

}
