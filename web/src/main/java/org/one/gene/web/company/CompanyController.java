package org.one.gene.web.company;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.one.gene.domain.entity.Company;
import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.service.CompanyService;
import org.one.gene.domain.service.CustomerService;
import org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.CompanyRepository;
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
public class CompanyController {

	@Autowired
    private CompanyService companyService;
	@Autowired
	private CompanyRepository companyRepository;
	
    /**
     * 模糊查询机构信息
     * */
    @Post("vagueSeachCompany")
    public Reply vagueSeachCompany(@Param("comName") String comName, Invocation inv){
		String companySQL = "%" + comName + "%";
		List<Company> companys = companyRepository.vagueSeachCompany(companySQL);
    	return Replys.with(companys).as(Json.class);
    }
    /**
     * 查询机构信息列表
     * */
    @Post("seachCompanyList")
    public Reply seachCompanyList(Invocation inv){
		List<Company> companys = companyRepository.seachCompanyList();
    	return Replys.with(companys).as(Json.class);
    }
}
