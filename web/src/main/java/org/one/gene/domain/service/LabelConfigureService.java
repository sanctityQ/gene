package org.one.gene.domain.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.one.gene.domain.entity.PrimerLabelConfig;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerLabelConfig.ColumnType;
import org.one.gene.domain.entity.PrimerLabelConfigSub;
import org.one.gene.repository.CustomerRepository;
import org.one.gene.repository.PrimerLabelConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

@Component
public class LabelConfigureService {

	 @Autowired
	 private PrimerLabelConfigRepository primerLabelConfigRepository;
	
	@Transactional
	public String configSave(String customerCode,String customerName,String columnsNumber,Map<String,Object> jsonMap) {
		String message = "";
		PrimerLabelConfig primerLabelConfig = null;
		primerLabelConfig = primerLabelConfigRepository.findByCustomerCode(customerCode);
		if(primerLabelConfig==null){
			primerLabelConfig = new PrimerLabelConfig();
		}else{
			//先删除后插非最优，后续优化
			primerLabelConfigRepository.delete(primerLabelConfig);
		}
		try{
			primerLabelConfig = this.getPrimerLabelConfig(primerLabelConfig,customerCode,customerName,columnsNumber);
			
			PrimerLabelConfigSub primerLabelConfigSubNew = null;
	        
			List<PrimerLabelConfigSub> primerLabelConfigSubs = new ArrayList<PrimerLabelConfigSub>();
			for (Object o : jsonMap.entrySet()) { 
				primerLabelConfigSubNew = new PrimerLabelConfigSub();
				primerLabelConfigSubNew.setPrimerLabelConfig(primerLabelConfig);
	            Map.Entry<String, String> entry = (Map.Entry<String,String>)o; 
	            primerLabelConfigSubNew.setType(entry.getKey());
	            primerLabelConfigSubNew.setTypeDesc(entry.getValue());
/*	            for (PrimerLabelConfigSub primerLabelConfigSub : primerLabelConfig.getPrimerLabelConfigSubs()) {
					if(entry.getKey().equals(primerLabelConfigSub.getType())){
						primerLabelConfigSubNew.setId(primerLabelConfigSub.getId());
					}
				}*/
	            primerLabelConfigSubs.add(primerLabelConfigSubNew);
	        }
			primerLabelConfig.setPrimerLabelConfigSubs(primerLabelConfigSubs);
			
			primerLabelConfigRepository.save(primerLabelConfig);
			message = "sucess";
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return message;
	}
	
	public PrimerLabelConfig getPrimerLabelConfig(PrimerLabelConfig primerLabelConfig,String customerCode,String customerName,String columnsNumber){
		primerLabelConfig.setCustomerCode(customerCode);
		primerLabelConfig.setCustomerName(customerName);
		primerLabelConfig.setCreateTime(new Date());
		primerLabelConfig.setModifyTime(new Date());
		primerLabelConfig.setColumnType(ColumnType.covertValue(new Integer(columnsNumber)));
		//登陆从session中获取，暂时写死
		primerLabelConfig.setUserCode("8800331");
		primerLabelConfig.setUserName("赵五");
		return primerLabelConfig;
	}
	
}
