package org.one.gene.web.print;

import java.util.List;

import org.one.gene.domain.entity.Board;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.repository.PrimerProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import com.sinosoft.one.mvc.web.Invocation;
import com.sinosoft.one.mvc.web.annotation.Param;
import com.sinosoft.one.mvc.web.annotation.Path;
import com.sinosoft.one.mvc.web.annotation.rest.Get;

@Path
public class PrintController {
    
	@Autowired
    private PrimerProductRepository primerProductRepository;
	
    /**
     * 进入打印标签查询页面
     * 
     * */
    @Get("prePrintLabelQuery")
    public String prePrintLabelQuery(){
    	
    	return "printLabelQuery";
    }
    
    /**
     * 打印标签查询
     * */
    public String printLabelQuery(@Param("comcode") String comcode1, Invocation inv){
    	
    	System.out.println("打印标签选择的公司======="+comcode1);
    	
    	if(!"".equals(comcode1)){
    		comcode1 = comcode1+"%";
    	}
    	List<PrimerProduct> primerProducts = primerProductRepository.selectPrimerProductForLabel(comcode1);
    	
    	inv.addModel("primerProducts", primerProducts);
    	return "printLabelList";
    }

}
