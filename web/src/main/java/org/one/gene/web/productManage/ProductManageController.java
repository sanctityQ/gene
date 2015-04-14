package org.one.gene.web.productManage;

import java.util.Map;

import org.one.gene.domain.entity.Order;
import org.one.gene.domain.entity.ProductMolecular;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.ProductMolecularRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ProductManageController {
	
	@Autowired
	private ProductMolecularRepository productMolecularRepository;

    private static Logger logger = LoggerFactory.getLogger(ProductManageController.class);
    
    @Get("productMolecularList")
    @Post("productMolecularList")
    public String productMolecularList(){
        return "productMolecularManage";
    }
    
    @Get("modifiePricedList")
    @Post("modifiePricedList")
    public String modifiePricedList(){
        return "modifiePriceManage";
    }
    //新增产品
    @Get("addproductMolecular")
    @Post("addproductMolecular")
    public String addproductMolecular(){
        return "addproductMolecular";
    }
    
    @Post("query")
    public Reply query(@Param("productCode") String productCode,@Param("productCategories") String productCategories,@Param("pageNo")Integer pageNo,
                        @Param("pageSize")Integer pageSize,Invocation inv) throws Exception {

        if(pageNo == null || pageNo ==0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 20;
        }
        Pageable pageable = new PageRequest(pageNo-1,pageSize);
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchFilter.Operator.EQ+"_productCode",productCode);
        searchParams.put(SearchFilter.Operator.EQ+"_productCategories",productCategories);
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<ProductMolecular> spec = DynamicSpecifications.bySearchFilter(filters.values(), ProductMolecular.class);
        
        Page<ProductMolecular> productMoleculars = productMolecularRepository.findAll(spec,pageable);
        
        return Replys.with(productMoleculars).as(Json.class);
    }
    
    @Post("save")
    public String save(@Param("productMolecular") ProductMolecular productMolecular ,Invocation inv){
    	
    	productMolecularRepository.save(productMolecular);
    	
    	return "productMolecularManage";
    }
    
    
    @Post("modifyQuery")
    @Get("modifyQuery")
    public String modifyQuery(@Param("id") Integer id,Invocation inv) throws Exception {
    	ProductMolecular productMolecular = productMolecularRepository.findById(id);
     	inv.addModel("productMolecular", productMolecular);
    	return "addproductMolecular";
    }
    
    /**
     * 删除
     * @param orderNo
     * @param inv
     * @return
     */
    @Post("delete") 
    public Object delete(@Param("id") Integer id,Invocation inv){
    	ProductMolecular productMolecular = productMolecularRepository.findById(id);
    	productMolecularRepository.delete(productMolecular);
    	return Replys.with("sucess").as(Text.class);  
    }
}
