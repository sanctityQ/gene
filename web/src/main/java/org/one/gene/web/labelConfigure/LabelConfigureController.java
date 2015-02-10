package org.one.gene.web.labelConfigure;

import org.one.gene.domain.service.OrderService;
import org.one.gene.domain.service.PrintService;
import org.one.gene.repository.OrderRepository;
import org.one.gene.repository.PrimerProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Get;


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
    
    
    
}
