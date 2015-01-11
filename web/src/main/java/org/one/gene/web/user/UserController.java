package org.one.gene.web.user;

import java.util.List;
import java.util.Map;

import org.one.gene.domain.entity.User;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.annotation.rest.Post;

@Path
public class UserController {
    
	@Autowired
	private UserRepository userRepository;

    @Get("prepareManageQuery")
    public String prepareManageQuery(){
        return "userManageQuery";
    }
    
    @Post("manageQuery")
	public String manageQuery(@Param("userName") String userName,
			@Param("comCode") String comCode, @Param("pageNo") Integer pageNo,
			@Param("pageSize") Integer pageSize, Invocation inv) {
    	
    	
    	if(pageNo == null){
            pageNo = 0;
        }

        if(pageSize == null){
            pageSize = 5;
        }

        Pageable pageable = new PageRequest(pageNo,pageSize);
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchFilter.Operator.EQ+"_name",userName);
        searchParams.put(SearchFilter.Operator.EQ+"_comCode",comCode);
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<User> spec = DynamicSpecifications.bySearchFilter(filters.values(), User.class);
        
        Page<User> userPage = userRepository.findAll(spec,pageable);
        
    	inv.addModel("userName", userName);
    	inv.addModel("comCode", comCode);
    	inv.addModel("page", userPage);
    	
        return "userManageQuery";
    }
    
    @Post("addUser")
	public String addUser(@Param("user") User user, Invocation inv) {
    	userRepository.save(user);
		return "";
	}
    
    @Post("batchDelete")
    public String  batchDelete(@Param("userInfoList")String userInfoList,Invocation inv) throws Exception {
    	 List<User> users = JSON.parseArray(userInfoList, User.class);
    	

     	return "";
    }
    
    
}
