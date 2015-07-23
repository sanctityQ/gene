package org.one.gene.web.customer;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.User;
import org.one.gene.domain.service.CustomerService;
import org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.UserRepository;
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
	@Autowired
    private UserRepository userRepository;
	
	@Get("clientManage")
    public String clientManage(){
        return "clientManage";
    }
	@Get("customerView")
    public String customerView(@Param("customerCode") String customerCode,Invocation inv){
		Customer customer = customerRepository.findByCode(customerCode);
		if ("0".equals(customer.getCustomerFlag())) {
			customer.setCustomerFlag("梓熙");
		}else if ("1".equals(customer.getCustomerFlag())) {
			customer.setCustomerFlag("代理公司");
		}else if ("2".equals(customer.getCustomerFlag())) {
			customer.setCustomerFlag("直接客户");
		}
		inv.addModel("customer", customer);
        return "customerView";
    }
	@Get("modifyCustomer")
    public String modifyCustomer(@Param("customerCode") String customerCode,Invocation inv){
		Customer customer = customerRepository.findByCode(customerCode);
		String haveZiXi = "";//是否有梓熙
		List<Customer> customers = customerRepository.seachHaveZiXi();
		if (customers != null && customers.size() > 0) {
			haveZiXi = "1";
		}
		
		inv.addModel("haveZiXi", haveZiXi);
		inv.addModel("customer", customer);
        return "addClient";
    }
	@Get("addClient")
	public String addClient(Invocation inv){
		ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		Customer customer = new Customer();
		customer.setCreateTime(new Date());
		String haveZiXi = "";//是否有梓熙
		List<Customer> customers = customerRepository.seachHaveZiXi();
		if (customers != null && customers.size() > 0) {
			haveZiXi = "1";
		}
		inv.addModel("haveZiXi", haveZiXi);
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
			@Param("customerFlag") String customerFlag,
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
        searchParams.put(SearchFilter.Operator.EQ+"_customerFlag",customerFlag);
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<Customer> spec = DynamicSpecifications.bySearchFilter(filters.values(), Customer.class);
        
        Page<Customer> customerPage = customerRepository.findAll(spec, pageable);
        
        for(Customer customer:customerPage.getContent()){
        	List<User> users = userRepository.getUserByCustomerId(customer.getId()+"");
			if (users != null && users.size()>0) {
				customer.setHaveUserFlag("0");//挂有用户，不能删除
			} else {
				customer.setHaveUserFlag("1");//未挂用户，可以删除
			}
        }
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
