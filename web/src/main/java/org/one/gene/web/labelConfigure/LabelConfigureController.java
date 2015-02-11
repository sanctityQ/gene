package org.one.gene.web.labelConfigure;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.service.OrderService;
import org.one.gene.domain.service.PrintService;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
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
    private PrimerProductRepository primerProductRepository;
	@Autowired
    private OrderRepository orderRepository;
	@Autowired
	private PrintService printService;
    @Autowired
    private OrderService orderService;
    
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
    	Map<String,Object>  jsonMap = JSON.parseObject(configList);
    	for (Object o : jsonMap.entrySet()) { 
            Map.Entry<String, String> entry = (Map.Entry<String,String>)o; 
            System.out.println(entry.getKey() + "--->" + entry.getValue()); 
        }
    	
    	return Replys.with("sucess").as(Text.class);  
    }
    
}
