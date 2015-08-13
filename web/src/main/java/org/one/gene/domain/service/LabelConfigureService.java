package org.one.gene.domain.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.one.gene.domain.entity.PrimerLabelConfig;
import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.domain.entity.PrimerLabelConfig.ColumnType;
import org.one.gene.domain.entity.PrimerLabelConfigSub;
import org.one.gene.domain.entity.User;
import org.one.gene.domain.service.account.ShiroDbRealm.ShiroUser;
import org.one.gene.repository.PrimerLabelConfigRepository;
import org.one.gene.repository.PrimerLabelConfigSubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Component
public class LabelConfigureService {

	 @Autowired
	 private PrimerLabelConfigRepository primerLabelConfigRepository;
	 @Autowired
	 private PrimerLabelConfigSubRepository primerLabelConfigSubRepository;
	
	 @Transactional(readOnly=false)
	public String configSave(String customerCode, String customerName,
			String columnsNumber,
			List<PrimerLabelConfigSub> primerLabelConfigSubs) {
		 
		String message = "";
		List<PrimerLabelConfigSub> plcsNews = Lists.newArrayList();
		Map<String, PrimerLabelConfigSub> PlcsMap = Maps.newHashMap();
		Map<Long, PrimerLabelConfigSub> PlcsMapnew = Maps.newHashMap();
		PrimerLabelConfig primerLabelConfig = primerLabelConfigRepository.findByCustomerCode(customerCode);
		if (primerLabelConfig == null) {
			primerLabelConfig = new PrimerLabelConfig();
		}else{
			for(PrimerLabelConfigSub plcs:primerLabelConfig.getPrimerLabelConfigSubs()){
				primerLabelConfigSubRepository.deleteById(plcs.getId());
			}
		}
		
		
		primerLabelConfig = this.getPrimerLabelConfig(primerLabelConfig,customerCode,customerName,columnsNumber);
		
		for (PrimerLabelConfigSub plcSub : primerLabelConfigSubs) {
			plcSub.setPrimerLabelConfig(primerLabelConfig);
        }
		
		primerLabelConfig.setPrimerLabelConfigSubs(primerLabelConfigSubs);
		
		primerLabelConfigRepository.save(primerLabelConfig);
		message = "sucess";
		
		return message;
	}
	
	public PrimerLabelConfig getPrimerLabelConfig(
			PrimerLabelConfig primerLabelConfig, String customerCode,
			String customerName, String columnsNumber) {
		
		ShiroUser shiroUser = (ShiroUser)SecurityUtils.getSubject().getPrincipal();
		User user = shiroUser.getUser();
		
		primerLabelConfig.setCustomerCode(customerCode);
		primerLabelConfig.setCustomerName(customerName);
		primerLabelConfig.setCreateTime(new Date());
		primerLabelConfig.setModifyTime(new Date());
		primerLabelConfig.setColumnType(ColumnType.covertValue(new Integer(columnsNumber)));
		primerLabelConfig.setUserCode(user.getCode());
		primerLabelConfig.setUserName(user.getName());
		return primerLabelConfig;
	}
	
	public PrimerLabelConfig searchConfigure(String customerCode){
		PrimerLabelConfig primerLabelConfig = primerLabelConfigRepository.findByCustomerCode(customerCode);
		return primerLabelConfig;
	}
	
}
