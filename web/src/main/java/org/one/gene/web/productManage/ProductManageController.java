package org.one.gene.web.productManage;

import java.util.List;
import java.util.Map;

import org.one.gene.domain.entity.ModifiedPrice;
import org.one.gene.domain.entity.ProductMolecular;
import org.one.gene.instrument.persistence.DynamicSpecifications;
import org.one.gene.instrument.persistence.SearchFilter;
import org.one.gene.repository.ModifiedPriceRepository;
import org.one.gene.repository.ProductMolecularRepository;
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
	@Autowired
	private ModifiedPriceRepository modifiedPriceRepository;

    
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
    
    //新增修饰价格
    @Get("addmodifiePrice")
    @Post("addmodifiePrice")
    public String addmodifiePrice(){
        return "addmodifiePrice";
    }
    
    @Post("query")
    public Reply query(@Param("productCode") String productCode,@Param("productCategories") String productCategories,@Param("validate") String validate,@Param("pageNo")Integer pageNo,
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
        searchParams.put(SearchFilter.Operator.EQ+"_validate",validate);
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
    
    
    @Post("queryModiPrice")
    public Reply queryModiPrice(@Param("modiType") String modiType,@Param("validate") String validate,@Param("pageNo")Integer pageNo,
                        @Param("pageSize")Integer pageSize,Invocation inv) throws Exception {

        if(pageNo == null || pageNo ==0){
            pageNo = 1;
        }

        if(pageSize == null){
            pageSize = 20;
        }
        Pageable pageable = new PageRequest(pageNo-1,pageSize);
        Map<String,Object> searchParams = Maps.newHashMap();
        searchParams.put(SearchFilter.Operator.EQ+"_modiType",modiType);
        searchParams.put(SearchFilter.Operator.EQ+"_validate",validate);
        Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
        Specification<ModifiedPrice> spec = DynamicSpecifications.bySearchFilter(filters.values(), ModifiedPrice.class);
        
        Page<ModifiedPrice> modifiedPrices = modifiedPriceRepository.findAll(spec,pageable);
        
        return Replys.with(modifiedPrices).as(Json.class);
    }
    
    
    @Post("modifyModiPrice")
    @Get("modifyModiPrice")
    public String modifyModiPrice(@Param("id") Integer id,Invocation inv) throws Exception {
    	ModifiedPrice modifiedPrice = modifiedPriceRepository.findById(id);
     	inv.addModel("modifiedPrice", modifiedPrice);
     	inv.addModel("modifyFlag", true);
    	return "addmodifiePrice";
    }
    
    /**
     * 删除
     * @param orderNo
     * @param inv
     * @return
     */
    @Post("deleteModiPrice") 
    public Object deleteModiPrice(@Param("id") Integer id,Invocation inv){
    	ModifiedPrice modifiedPrice = modifiedPriceRepository.findById(id);
    	modifiedPriceRepository.delete(modifiedPrice);
    	return Replys.with("sucess").as(Text.class);  
    }
    /**
     * 保存修饰价格信息
     * @param modifiedPrice 修饰价格对象
     * @param inv
     * @return
     */
    @Post("saveModiPrice")
    public String saveModiPrice(@Param("modifiedPrice") ModifiedPrice modifiedPrice ,Invocation inv){
    	
    	modifiedPriceRepository.save(modifiedPrice);
    	
    	return "modifiePriceManage";
    }
    
    
    @Post("selectQuery")
    public Reply selectQuery(@Param("productCategories") String productCategories,Invocation inv) throws Exception {

       
        List<ProductMolecular> productMoleculars = productMolecularRepository.findByProductCategories(productCategories);
        
        return Replys.with(productMoleculars).as(Json.class);
    }
}
