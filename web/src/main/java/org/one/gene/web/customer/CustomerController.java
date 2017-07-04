package org.one.gene.web.customer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.one.gene.domain.entity.Company;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.CustomerContacts;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.User;
import org.one.gene.domain.service.CustomerService;
import org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.CompanyRepository;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Lists;
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
	@Autowired
    private CompanyRepository companyRepository;
	
	@Get("clientManage")
    public String clientManage(Invocation inv){
		List<Company> companys = companyRepository.seachCompanyList();
		inv.addModel("companys", companys);
        return "clientManage";
    }
	@Get("customerView")
    public String customerView(@Param("customerCode") String customerCode,Invocation inv) throws IOException{
		if (customerCode != null && !customerCode.equals("")) {
			customerCode = new String(customerCode.getBytes("iso8859-1"), "UTF-8");
		}
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
			Customer customerTemp = (Customer)customers.get(0);
			haveZiXi = customerTemp.getId()+"";
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
		customer.setCustomerFlag("2");//新增客户时，默认为 直接客户
		customer.setPrefix("AX");//新增客户时，默认为 AX
		
		String haveZiXi = "";//是否有梓熙
		List<Customer> customers = customerRepository.seachHaveZiXi();
		if (customers != null && customers.size() > 0) {
			Customer customerTemp = (Customer)customers.get(0);
			haveZiXi = customerTemp.getId()+"";
		}
		inv.addModel("haveZiXi", haveZiXi);
		inv.addModel("customer", customer);
		return "addClient";
	}
	@Post("save")
	public String save(@Param("customer") Customer customer,Invocation inv) throws IllegalStateException, IOException {
		Company company = null;
		if(customer.getHandlerCode()==null || "".equals(customer.getHandlerCode())){//客户是梓熙，不需要选择业务员
			company = companyRepository.findByComCode("00000000");
		}else{
			User user = userRepository.findByCode(customer.getHandlerCode());
			company = user.getCompany();
		}
		
		customer.setCompany(company);
		customerService.save(customer);
		
		List<Company> companys = companyRepository.seachCompanyList();
		inv.addModel("companys", companys);
		
		return "clientManage";
	}
	
	@Post("query")
	public Reply query(@Param("customerName") String customerName,
			@Param("customerFlag") String customerFlag,
			@Param("companyList") String companyList,
			@Param("handlerName") String handlerName,
			@Param("contactName") String contactName,
			@Param("pageNo")Integer pageNo,
            @Param("pageSize")Integer pageSize,
            Invocation inv){
		
		if(pageNo == null || pageNo ==0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 10;
        }
        
        if(customerName == null){
        	customerName = "";
        }
        if(customerFlag == null){
        	customerFlag = "";
        }
        if(handlerName == null){
        	handlerName = "";
        }
        if(contactName == null){
        	contactName = "";
        }
        
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String comCode = user.getUser().getCompany().getComCode();
        
        Pageable pageable = new PageRequest(pageNo-1,pageSize);

        if(!"1".equals(user.getUser().getCompany().getComLevel())){//分公司
        }
        
		if (companyList != null) {//总公司
			comCode = companyList;
		}
        
		Page<Customer> customerPage = customerService.queryCustomers(
				customerName, customerFlag, handlerName, contactName, comCode,
				pageable);
        
        
        for(Customer customer:customerPage.getContent()){
        	List<User> users = userRepository.getUserByCustomerId(customer.getId()+"");
			if (users != null && users.size()>0) {
				customer.setHaveUserFlag("0");//挂有用户，不能删除
			} else {
				customer.setHaveUserFlag("1");//未挂用户，可以删除
			}
        }

    	return Replys.with(customerPage).as(Json.class);
	}
	
    /**
     * 模糊查询客户信息
     * */
    @Post("vagueSeachCustomer")
    public Reply vagueSeachCustomer(@Param("customercode") String customercode, Invocation inv){
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String comCodeSQL = user.getUser().getCompany().getComCode();
        if("1".equals(user.getUser().getCompany().getComLevel())){
        	comCodeSQL = "";
        }
		String customerSQL = "%" + customercode + "%";
		
		List<Customer> customers = Lists.newArrayList();
		
		if (customercode.startsWith("梓熙")) {
			customers = customerRepository.vagueSeachCustomerZhiJie(comCodeSQL);
		} else {
			customers = customerRepository.vagueSeachCustomer(customerSQL,comCodeSQL);
		}
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
    
    
    /**
     * 导出客户清单
     * @throws IOException 
     * */
    @Post("exportCustomer")
	public void exportCustomer(@Param("customerName") String customerName,
			@Param("customerFlag") String customerFlag,
			@Param("companyList") String companyList,
			@Param("handlerName") String handlerName, Invocation inv)
			throws IOException {
    	
		if (customerName != null && !customerName.equals("")) {
			customerName = new String(customerName.getBytes("iso8859-1"), "UTF-8");
		}
		if (handlerName != null && !handlerName.equals("")) {
			handlerName = new String(handlerName.getBytes("iso8859-1"), "UTF-8");
		}
        
        ShiroUser user = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
        String comCode = user.getUser().getCompany().getComCode();
        
        Map<String,Object> searchParams = Maps.newHashMap();
        
        searchParams.put(SearchFilter.Operator.EQ+"_name",customerName);
        searchParams.put(SearchFilter.Operator.EQ+"_customerFlag",customerFlag);
        searchParams.put(SearchFilter.Operator.EQ+"_handlerName",handlerName);
        if(!"1".equals(user.getUser().getCompany().getComLevel())){//分公司
        	searchParams.put(SearchFilter.Operator.EQ+"_company.comCode",comCode);
        }
		if (companyList != null && !companyList.equals("undefined")) {//总公司
			searchParams.put(SearchFilter.Operator.EQ+"_company.comCode",companyList);
		}
		
	    Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
	    Specification<Customer> spec = DynamicSpecifications.bySearchFilter(filters.values(), Customer.class);
	        
	    List<Customer> customers = customerRepository.findAll(spec);
    	customerService.exportCustomer( customers ,inv);

    }
	
}
