package org.one.gene.domain.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.one.gene.domain.entity.ProductMolecular;
import org.one.gene.repository.ProductMolecularRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

@Component
public class PropotiesService {
	
	@Autowired
	private ProductMolecularRepository productMolecularRepository;

	 public static Map<String, Map> pcatMap = Maps.newHashMap();
	 public static Map<String, BigDecimal> modiThreeMap = Maps.newHashMap();
	 public static Map<String, BigDecimal> modiFiveMap = Maps.newHashMap();
	 public static Map<String, BigDecimal> modiSpeMap = Maps.newHashMap();
	 public static Map<String, BigDecimal> modiMidMap = Maps.newHashMap();
	 /*private static PropotiesService propotiesService=new PropotiesService();
   
    private PropotiesService(){};
    
    public static PropotiesService getInstance(){
     return propotiesService;
   }*/
   

    public String getValue(String productCategories,String productCode){
     if(pcatMap.get(productCategories)==null){
    	 createValue(productCategories,productCode);
     }
     
     if(pcatMap.get(productCategories).get(productCode)==null){
    	 return "0";
     }
     return pcatMap.get(productCategories).get(productCode).toString();
    }
    
    private void createValue(String productCategories,String productCode){
    	/*Properties p = new Properties();
    	try {
			p.load(PropotiesService.class.getClassLoader().getResourceAsStream("product.properties"));
			valueStr = p.getProperty(key);
		} catch (IOException e1) {
			 e1.printStackTrace();
		}*/
    	//狗屎，待优化 实现效果先
        List<ProductMolecular> productMoleculars = (List<ProductMolecular>) productMolecularRepository.findAll();
    	for(ProductMolecular productMolecular:productMoleculars){
    		if("modiThreeType".equals(productMolecular.getProductCategories())){
    			modiThreeMap.put(productMolecular.getProductCode(), productMolecular.getModifiedMolecular());
    			pcatMap.put(productMolecular.getProductCategories(), modiThreeMap);
    		}else if("modiFiveType".equals(productMolecular.getProductCategories())){
    			modiFiveMap.put(productMolecular.getProductCode(), productMolecular.getModifiedMolecular());
    			pcatMap.put(productMolecular.getProductCategories(), modiFiveMap);
    		}else if("modiSpeType".equals(productMolecular.getProductCategories())){
    			modiSpeMap.put(productMolecular.getProductCode(), productMolecular.getModifiedMolecular());
    			pcatMap.put(productMolecular.getProductCategories(), modiSpeMap);
    		}else if("modiMidType".equals(productMolecular.getProductCategories())){
    			modiMidMap.put(productMolecular.getProductCode(), productMolecular.getModifiedMolecular());
    			pcatMap.put(productMolecular.getProductCategories(), modiMidMap);
    		}
    	
    	}
    	
    }
    
}
