package org.one.gene.web.customer;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.User;
import org.one.gene.domain.service.CustomerService;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Maps;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Post;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Json;

@Path
public class CustomerController {

	@Autowired
    private CustomerService customerService;
	@Autowired
    private CustomerRepository customerRepository;
	
	@Post("save")
	public String save(@Param("customer") Customer customer,Invocation inv) throws IllegalStateException, IOException{

		/*String realpathdir = "";
    	String path="";//暂时按照只上传一个文件定义
    	String filename = "";
    	if (!file.isEmpty()) { 
        	filename = file.getOriginalFilename();
        	realpathdir = inv.getServletContext().getRealPath("/")+"customer/companyLogo/"+customer.getCode()+"/";
        	path = realpathdir+filename;
        	
    	    // 创建文件目录
    	    File savedir = new File(realpathdir);
    	    // 如果目录不存在就创建
    	    if (!savedir.exists()) {
    	      savedir.mkdirs();
    	    }
    	    
        	System.out.println(path);
        	file.transferTo(new File(path));
    	}*/
		customerService.save(customer);
		return "";
	}
	
	@Post("query")
	public String query(@Param("customerName") String customerName,@Param("pageNo")Integer pageNo,
            @Param("pageSize")Integer pageSize,Invocation inv){
		
		if(pageNo == null){
            pageNo = 0;
        }

        if(pageSize == null){
            pageSize = 5;
        }

        Pageable pageable = new PageRequest(pageNo,pageSize);
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchFilter.Operator.EQ+"_name",customerName);
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Customer> spec = DynamicSpecifications.bySearchFilter(filters.values(), Customer.class);
        
        Page<Customer> customerPage = customerRepository.findAll(spec, pageable);
        
    	inv.addModel("page", customerPage);
    	inv.addModel("pageSize", pageSize);
		
		return "customerQuery";
	}
	
    /**
     * 模糊查询客户信息
     * */
    @Post("vagueSeachCustomer")
    public Reply vagueSeachCustomer(@Param("customercode") String customercode, Invocation inv){
		String customerSQL = "%" + customercode + "%";
		List<Customer> customers = customerRepository.vagueSeachCustomer(customerSQL);
    	return Replys.with(customers).as(Json.class);
    }
    
    
}
