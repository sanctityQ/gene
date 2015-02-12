package org.one.gene.web.labelConfigure;

import java.util.Map;

import org.one.gene.domain.service.LabelConfigureService;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Get;
import com.sinosoft.one.mvc.web.annotation.rest.Post;
import com.sinosoft.one.mvc.web.instruction.reply.Reply;
import com.sinosoft.one.mvc.web.instruction.reply.Replys;
import com.sinosoft.one.mvc.web.instruction.reply.transport.Text;


@Path
public class LabelConfigureController {
    
    @Autowired
    private LabelConfigureService labelConfigureService;
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
    		                @Param("configList") String configList,
    		                Invocation inv) {
    	String message ="";
    	
    	Map<String,Object>  jsonMap = JSON.parseObject(configList);
    	
    	message = labelConfigureService.configSave(customerCode, customerName,columnsNumber,jsonMap);
    	
    	return Replys.with(message).as(Text.class);  
    }
    
}
