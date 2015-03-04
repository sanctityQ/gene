package org.one.gene.domain.service;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Maps;

public class PropotiesService {

	 private static Map<String, String> pptMap = Maps.newHashMap();
   
    private PropotiesService(){};
   

    public static String getValue(String key){
     if(pptMap.get(key)==null){
       String valueStr = createValue(key);
       pptMap.put(key,valueStr);
     }
     return pptMap.get(key);
    }
    
    private static String createValue(String key){
    	Properties p = new Properties();
    	String valueStr = "";
    	try {
			p.load(PropotiesService.class.getClassLoader().getResourceAsStream("product.properties"));
			valueStr = p.getProperty(key);
		} catch (IOException e1) {
			 e1.printStackTrace();
		}
    	if(valueStr==null || ("null").equals(valueStr)){valueStr="0";}
    	return valueStr;
    }
    
    public static void main(String[] args) {
    	System.out.println(PropotiesService.getValue("硫代-sp"));
	}
	
}
