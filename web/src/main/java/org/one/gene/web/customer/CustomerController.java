package org.one.gene.web.customer;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.one.gene.domain.entity.Customer;
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
import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.annotation.rest.Post;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Json;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Text;

@Path
public class CustomerController {

	@Autowired
    private CustomerService customerService;
	@Autowired
    private CustomerRepository customerRepository;
	
	@Get("clientManage")
    public String clientManage(){
        return "clientManage";
    }
	@Get("customerView")
    public String customerView(@Param("customerCode") String customerCode,Invocation inv){
		Customer customer = customerRepository.findByCode(customerCode);
		inv.addModel("customer", customer);
        return "customerView";
    }
	@Get("modifyCustomer")
    public String modifyCustomer(@Param("customerCode") String customerCode,Invocation inv){
		Customer customer = customerRepository.findByCode(customerCode);
		inv.addModel("customer", customer);
        return "addClient";
    }
	@Get("addClient")
	public String addClient(Invocation inv){
		Customer customer = new Customer();
		customer.setCreateTime(new Date());
		inv.addModel("customer", customer);
		return "addClient";
	}
	@Post("save")
	public String save(@Param("customer") Customer customer,Invocation inv) throws IllegalStateException, IOException{
		customerService.save(customer);
		return "clientManage";
	}
	
	@Post("query")
	public Reply query(@Param("customerName") String customerName,
			@Param("unitName") String unitName,
			@Param("pageNo")Integer pageNo,
            @Param("pageSize")Integer pageSize,Invocation inv){
		
		if(pageNo == null || pageNo ==0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 10;
        }

        Pageable pageable = new PageRequest(pageNo-1,pageSize);
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchFilter.Operator.EQ+"_name",customerName);
        searchParams.put(SearchFilter.Operator.EQ+"_unit",unitName);
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Customer> spec = DynamicSpecifications.bySearchFilter(filters.values(), Customer.class);
        
        Page<Customer> customerPage = customerRepository.findAll(spec, pageable);
        
    	inv.addModel("page", customerPage);
    	inv.addModel("pageSize", pageSize);
		
    	return Replys.with(customerPage).as(Json.class);
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
    
    /**
     * 模糊查询客户单位
     * */
    @Post("vagueSeachUnit")
    public Reply vagueSeachUnit(@Param("unitName") String unitName, Invocation inv){
		String unitSQL = "%" + unitName + "%";
		List<Customer> customers = customerRepository.vagueSeachUnit(unitSQL);
    	return Replys.with(customers).as(Json.class);
    }
    
    /**
     * 删除
     * @param orderNo
     * @param inv
     * @return
     */
    @Post("delete") 
    public Object delete(@Param("customerCode") String customerCode,Invocation inv){
    	Customer customer = customerRepository.findByCode(customerCode);
    	customerRepository.delete(customer);
    	return Replys.with("sucess").as(Text.class);  
    }
    
}
