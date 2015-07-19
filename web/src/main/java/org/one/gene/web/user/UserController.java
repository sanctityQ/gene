package org.one.gene.web.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.one.gene.domain.entity.Company;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.User;
import org.one.gene.domain.service.account.AccountService;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.CompanyRepository;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.alibaba.fastjson.JSON;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.alibaba.fastjson.JSONObject;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.annotation.rest.Post;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Json;

import javax.annotation.Nullable;

@Path
public class UserController {


  private static final Logger logger = LoggerFactory.getLogger(UserController.class);

  @Autowired
  private CompanyRepository companyRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AccountService accountService;

  @Autowired
  private CustomerRepository customerRepository;
  
  @Get("")
  public String index() {
    return "f:/user/manageQuery";
  }

  @Get("preAdd")
  public String prepareAddUser(Invocation inv) {
	    //查询总公司信息，如果不是梓熙的用户，机构默认为总公司
	    Company topCompany = companyRepository.findByComCode("00000000");
	    inv.addModel("topCompany", topCompany);
    return "user";
  }

  @Get("view/{id}")
  public String view(@Param("id") Long id, @Param("op")String operation,Invocation inv) {
    User user = userRepository.findOne(id);
    //查询总公司信息，如果不是梓熙的用户，机构默认为总公司
    Company topCompany = companyRepository.findByComCode("00000000");
    
    inv.addModel("user", user);
    inv.addModel("topCompany", topCompany);
    if(StringUtils.isNotBlank(operation)){
      inv.addModel("op", operation);
    }
    return "user";
  }

  @Get("list")
  public  Reply list(@Param("userName") String userName,
                     @Param("comCode") String comCode,
                     @Param("pageNo") Integer pageNo,
                     @Param("pageSize") Integer pageSize, Invocation inv){
    if(pageNo == null){
      pageNo = 0;
    }

    if(pageSize == null){
      pageSize = 10;
    }

    Sort sort = new Sort(Sort.Direction.DESC, "createTime");
    Pageable pageable = new PageRequest(pageNo, pageSize, sort);
    Map<String, Object> searchParams = Maps.newHashMap();
    searchParams.put(SearchFilter.Operator.EQ + "_name", userName);
    searchParams.put(SearchFilter.Operator.EQ + "_company.comCode", comCode);
    Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
    Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);

    Page<User> userPage = userRepository.findAll(spec, pageable);

    Map<String, Object> view = Maps.newHashMap();

    view.put("total", userPage.getTotalElements());

    List data = Lists.transform(userPage.getContent(), new Function<User, Map<String, Object>>() {
      @Nullable
      @Override
      public Map<String, Object> apply(User input) {
        Map<String,Object> back = Maps.newHashMap();
        back.put("id",input.getId());
        back.put("code",input.getCode());
        back.put("name",input.getName());
        back.put("comCode",input.getCompany().getComName());
        back.put("mobile",input.getMobile());
        back.put("email",input.getEmail());
        back.put("userFlag",input.getUserFlag());
        back.put("customerName",input.getCustomer().getName());
        back.put("customerFlag",input.getCustomer().getCustomerFlag());
        back.put("validate",input.isValidate());
        return back;
      }
    });

    view.put("rows", data);
    return Replys.with(view).as(Json.class);
  }

  @Get("manageQuery")
  public String manageQuery(@Param("userName") String userName,
                            @Param("comCode") String comCode,
                            @Param("pageNo") Integer pageNo,
                            @Param("pageSize") Integer pageSize, Invocation inv) {

    if(pageNo == null){
      pageNo = 0;
    }

    if(pageSize == null){
      pageSize = 10;
    }

    Sort sort = new Sort(Sort.Direction.DESC, "createTime");
    Pageable pageable = new PageRequest(pageNo, pageSize, sort);
    Map<String, Object> searchParams = Maps.newHashMap();
    searchParams.put(SearchFilter.Operator.EQ + "_name", userName);
    searchParams.put(SearchFilter.Operator.EQ + "_company.comCode", comCode);
    Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
    Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);

    Page<User> userPage = userRepository.findAll(spec, pageable);

    inv.addModel("userName", userName);
    inv.addModel("comCode", comCode);

    Map<String, Object> view = Maps.newHashMap();
    view.put("pageNumber", userPage.getNumber() + 1);
    view.put("pageSize", userPage.getSize());
    view.put("total", userPage.getTotalElements());


    List data = Lists.transform(userPage.getContent(), new Function<User, Map<String, Object>>() {
      @Nullable
      @Override
      public Map<String, Object> apply(User input) {

        Map<String,Object> back = Maps.newHashMap();
        back.put("id",input.getId());
        back.put("code",input.getCode());
        back.put("name",input.getName());
        back.put("comCode",input.getCompany().getComName());
        back.put("mobile",input.getMobile());
        back.put("email",input.getEmail());
        back.put("userFlag",input.getUserFlag());
        back.put("customerName",input.getCustomer().getName());
        back.put("customerFlag",input.getCustomer().getCustomerFlag());
        back.put("validate",input.isValidate());
        return back;
      }
    });
    view.put("rows", data);
    inv.addModel("data", JSONObject.toJSONString(view));
    return "userManageQuery";
  }


  @Post("register")
  public String addUser(@Param("user") User user, Invocation inv) {
    Company company = companyRepository.findOne(user.getCompany().getId());
    user.setCompany(company);
	if (user.getCustomer()!=null) {
		Customer customer = customerRepository.findOne(user.getCustomer().getId());
			if (customer != null) {
				user.setCustomer(customer);
			}
	}
	
    accountService.registerUser(user);
    return "r:/user/manageQuery";
  }

  @Post("update")
  public String updateUser(@Param("user") User user, Invocation inv) {
    Company company = companyRepository.findByComCode(user.getCompany().getComCode());
    user.setCompany(company);
    accountService.updateUser(user);
    return "r:/user/manageQuery";
  }


  @Post("delete")
  public Reply delete(@Param("id[]")List<Long> userId){
    accountService.deleteUsers(userId);
    return Replys.with(true).as(Json.class);
  }

  /**
   * 模糊查询用户信息
   */
  @Post("vagueSeachUser")
  public Reply vagueSeachUser(@Param("userName") String userName, Invocation inv) {
    String userSQL = "%" + userName + "%";
    List<User> users = userRepository.vagueSeachUser(userSQL);
    return Replys.with(users).as(Json.class);
  }
}
