package org.one.gene.web.order;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
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
import com.sinosoft.one.mvc.web.instruction.reply.transport.Json;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Text;

@Path
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PrimerProductRepository primerProductRepository;


    @Get("import")
    public String orderImport(){
        return "orderImport";
    }
    @Get("orderList")
    public String orderList(){
        return "orderList";
    }
    @Get("orderExamine")
    public String orderExamine(){
        return "orderExamine";
    }
    
    
    @Post("upload")
    public Reply upload(@Param("customerCode") String customerCode, @Param("file") MultipartFile file, Invocation inv) throws Exception {

    	ArrayList<String> errors = new ArrayList<String>();
    	
    	if("".equals(customerCode)){
    		throw new Exception("客户代码或名称未录入，请您录入！");
    	}
    	if (file.isEmpty()) {
    		throw new Exception("请您选择要上传的文件！");
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
        	realpathdir = inv.getServletContext().getRealPath("/")+"upExcel/"+dateString+"/";
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
	        }
    	}
        if(errors.size()>0){
        	return Replys.with(errors).as(Json.class);
        }else{
        	//获取客户信息
        	Customer customer = orderService.findCustomer(customerCode);
        	if(customer==null){
        		throw new Exception("无此客户信息，请您确认后重新上传！");
        	}
        	//组织订单对象
        	Order order = new Order();
        	order = orderService.ReadExcel(path, 1,"2-",order);
        	orderService.convertOrder(customer,filename,order);
        	//保存订单信息
        	orderService.save(order);
        	inv.getResponse().setContentType("text/html");
        	Order orderRe = new Order();
        	orderRe.setOrderNo(order.getOrderNo());
            return Replys.with(orderRe).as(Json.class);
        }
        
    }
    
    /**
     * 订单导入列表展示
     * @param orderNo
     * @param inv
     * @return
     * @throws Exception
     */
    @Post("productQuery")
    public Reply productQuery(@Param("orderNo") String orderNo,@Param("pageNo")Integer pageNo,
            @Param("pageSize")Integer pageSize,Invocation inv) throws Exception {
        
    	if(pageNo == null){
            pageNo = 0;
        }

        if(pageSize == null){
            pageSize = 10;
        }

        Pageable pageable = new PageRequest(pageNo,pageSize);
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchFilter.Operator.EQ+"_orderNo",orderNo);
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Order> spec = DynamicSpecifications.bySearchFilter(filters.values(), Order.class);
        
        Page<Order> orderPage = orderRepository.findAll(spec,pageable);
        
        BigDecimal orderTotalValue = new BigDecimal("0");
        String coustomerCode = "";
        for(Order order:orderPage.getContent()){
        	coustomerCode = order.getCustomerCode();
        	for(PrimerProduct product:order.getPrimerProducts()){
        		product.setTbn(new BigDecimal(product.getGeneOrder().length()));
        		orderTotalValue = orderTotalValue.add(product.getTotalVal());
        	}
        	order.setTotalValue(orderTotalValue);
		}
        Customer customer = orderService.findCustomer(coustomerCode);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setCustomer(customer);
        orderInfo.setOrderPage(orderPage);
        return Replys.with(orderInfo).as(Json.class);
    }
    
    @Post("save")
    public String save(@Param("primerProducts") List<Order> primerProducts,Invocation inv) throws IllegalStateException, IOException {
//    	System.out.println(primerProducts);
    	//List<Order> orders = JSON.parseArray(primerProducts, Order.class);
//    	for(Order order:orders){
//			orderRepository.save(order);
//		}

        //primerProductRepository.save(primerProducts);

        return "";
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
    public Reply query(@Param("orderNo") String orderNo, @Param("customerCode") String customerCode,@Param("pageNo")Integer pageNo,
                        @Param("pageSize")Integer pageSize,Invocation inv) throws Exception {

        if(pageNo == null){
            pageNo = 0;
        }

        if(pageSize == null){
            pageSize = 10;
        }
        Pageable pageable = new PageRequest(pageNo,pageSize);
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchFilter.Operator.EQ+"_orderNo",orderNo);
        searchParams.put(SearchFilter.Operator.EQ+"_customerCode",customerCode);
        searchParams.put(SearchFilter.Operator.EQ+"_status","0");
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Order> spec = DynamicSpecifications.bySearchFilter(filters.values(), Order.class);
        
        Page<Order> orderPage = orderRepository.findAll(spec,pageable);
        Page<OrderInfo> orderListPage = orderService.convertOrderList(orderPage,pageable);
        
        return Replys.with(orderListPage).as(Json.class);
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
     * @param pageNo
     * @param pageSize
     * @param inv
     * @return
     * @throws Exception
     */
    @Post("examine")
    public Object examine(@Param("orderNo") String orderNo,@Param("failReason") String failReason,Invocation inv) throws Exception {
    	orderService.examine(orderNo,failReason);
    	return Replys.with("sucess").as(Text.class);
    }
    
    @Get("downLoad") 
    @Post("downLoad") 
    public String downLoad(Invocation inv) throws Exception{
		String fileName = "订购表模版.xls";
		String filePath = inv.getRequest().getSession().getServletContext().getRealPath("/");
		filePath = filePath+"views\\downLoad\\template\\"+fileName;
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
