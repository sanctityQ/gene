package org.one.gene.web.labelConfigure;

import java.util.List;

import org.one.gene.domain.entity.Customer;
import org.one.gene.domain.entity.PrimerLabelConfig;
import org.one.gene.domain.entity.PrimerLabelConfigSub;
import org.one.gene.domain.service.LabelConfigureService;
import org.one.gene.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;

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
public class LabelConfigureController {
    
    @Autowired
    private LabelConfigureService labelConfigureService;
    @Autowired
    private CustomerRepository customerRepository;
    
    /**
     * 进入标签配置页面
     * 
     * */
    @Get("config")
    public String configLabel(){
    	
    	return "labelConfigure";
    }
    
    
    @Post("configSave")
    public Reply configSave(@Param("customerCode") String customerCode,
    		                @Param("customerName") String customerName,
    		                @Param("columnsNumber") String columnsNumber,
    		                @Param("primerLabelConfigSubs") List<PrimerLabelConfigSub> primerLabelConfigSubs,
    		                Invocation inv) {
    	
    	String message = labelConfigureService.configSave(customerCode, customerName,columnsNumber,primerLabelConfigSubs);
    	
    	return Replys.with(message).as(Text.class);  
    }
    
    @Post("searchConfigure")
    public Reply searchConfigure(@Param("customerCode") String customerCode,Invocation inv) {
    	
		Customer customer = customerRepository.findByCode(customerCode);
		//直接客户直接使用梓熙生物的配置
		if (customer != null && "2".equals(customer.getCustomerFlag())) {
			return Replys.with("{\"success\":true,\"mess\":\"该客户标识为'直接客户'，标签直接使用梓熙生物的配置！\"}").as(Json.class);
		}
    	
    	PrimerLabelConfig primerLabelConfig = labelConfigureService.searchConfigure(customerCode);
    	
    	return Replys.with(primerLabelConfig).as(Json.class);
    }
    
}
