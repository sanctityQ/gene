package org.one.gene.domain.service;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Maps;

public class ModiPropotiesService {

	 private static Map<String, String> modipptMap = Maps.newHashMap();
   
    private ModiPropotiesService(){};
   

    public static String getValue(String key){
     if(modipptMap.get(key)==null){
       String valueStr = createValue(key);
       modipptMap.put(key,valueStr);
     }
     return modipptMap.get(key);
    }
    
    private static String createValue(String key){
    	Properties p = new Properties();
    	String valueStr = "";
    	try {
			p.load(PropotiesService.class.getClassLoader().getResourceAsStream("modi.properties"));
			valueStr = p.getProperty(key);
		} catch (IOException e1) {
			 e1.printStackTrace();
		}
    	if(valueStr==null || ("null").equals(valueStr)){valueStr="0";}
    	return valueStr;
    }
    
    public static void main(String[] args) {
    	System.out.println(ModiPropotiesService.getValue("硫代-sp"));
	}
	
}
