package org.one.gene.web.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.one.gene.domain.entity.User;
import org.one.gene.domain.service.account.AccountService;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
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
  private UserRepository userRepository;

  @Autowired
  private AccountService accountService;

  @Get("prepareManageQuery")
  public String prepareManageQuery() {
    return "userManageQuery";
  }

  @Get("manageQuery")
  public String manageQuery(@Param("userName") String userName,
                            @Param("comCode") String comCode, @Param("pageNo") Integer pageNo,
                            @Param("pageSize") Integer pageSize, Invocation inv) {

    if(pageNo == null){
      pageNo = 0;
    }

    if(pageSize == null){
      pageSize = 20;
    }

    Sort sort = new Sort(Sort.Direction.DESC, "createTime");
    Pageable pageable = new PageRequest(pageNo, pageSize, sort);
    Map<String, Object> searchParams = Maps.newHashMap();
    searchParams.put(SearchFilter.Operator.EQ + "_name", userName);
    searchParams.put(SearchFilter.Operator.EQ + "_comCode", comCode);
    Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
    Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);

    Page<User> userPage = userRepository.findAll(spec, pageable);

    inv.addModel("userName", userName);
    inv.addModel("comCode", comCode);

    Map<String, Object> view = Maps.newHashMap();
    view.put("pageNumber", userPage.getNumber() + 1);
    view.put("pageSize", userPage.getSize());


    List data = Lists.transform(userPage.getContent(), new Function<User, Map<String, Object>>() {
      @Nullable
      @Override
      public Map<String, Object> apply(User input) {

        Map<String,Object> back = Maps.newHashMap();
        back.put("code",input.getCode());
        back.put("name",input.getName());
        //TODO@(chengQ)
        back.put("comCode",input.getComCode());
        back.put("mobile",input.getMobile());
        back.put("email",input.getEmail());
        back.put("staffFlag",input.isStaffFlag());
        return back;
      }
    });
    view.put("data", data);
    String pageJsonString = JSONObject.toJSONString(view);
    logger.info(pageJsonString);
    inv.addModel("data", pageJsonString);
    return "userManageQuery";
  }

  @Post("addUser")
  public String addUser(@Param("user") User user, Invocation inv) {
    accountService.registerUser(user);
    return "f:/user/manageQuery";
  }

  @Post("update")
  public String updateUser(@Param("user") User user, Invocation inv) {
    accountService.updateUser(user);
    return "f:/user/manageQuery";
  }



  @Post("batchDelete")
  public String batchDelete(@Param("userInfoList") String userInfoList, Invocation inv)
      throws Exception {
    List<User> users = JSON.parseArray(userInfoList, User.class);

    return "";
  }

  /**
   * 模糊查询客户信息
   */
  @Post("vagueSeachCustomer")
  public Reply vagueSeachCustomer(@Param("customercode") String customercode, Invocation inv) {
    String customerSQL = "%" + customercode + "%";
    List<User> users = userRepository.vagueSeachCustomer(customerSQL);
    return Replys.with(users).as(Json.class);
  }
}
