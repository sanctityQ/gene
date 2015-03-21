package org.one.gene.domain.service;

import java.math.BigDecimal;

import org.one.gene.domain.entity.PrimerProduct;
import org.one.gene.excel.OrderCaculate;
import org.springframework.stereotype.Component;

/**
 * 该工具类暂时写死
 * @author ThinkPad User
 *
 */
@Component
public class PriceTool {

	//int tbn,BigDecimal odTotal,String purifyType
	public void getPrice(PrimerProduct primerProduct){
		
		int tbn = primerProduct.getGeneOrder().trim().length();
		int odTotal = primerProduct.getOdTotal().intValue();
		
		if("OPC".equals(primerProduct.getPurifyType())){
			if(tbn<60){
				if(odTotal<=5){
					primerProduct.setBaseVal(new BigDecimal("0.45"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}else if(odTotal>=6&&odTotal<=10){
					primerProduct.setBaseVal(new BigDecimal("0.8"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}else if(odTotal>=11&&odTotal<=20){
					primerProduct.setBaseVal(new BigDecimal("1.6"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}else{
					primerProduct.setBaseVal(new BigDecimal("0"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}
			}else if(tbn>=60){
				if(odTotal<=5){
					primerProduct.setBaseVal(new BigDecimal("0.8"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}else if(odTotal>=6&&odTotal<=10){
					primerProduct.setBaseVal(new BigDecimal("1.6"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}else if(odTotal>=11&&odTotal<=20){
					primerProduct.setBaseVal(new BigDecimal("3.2"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}else{
					primerProduct.setBaseVal(new BigDecimal("0"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}
			}
		}else if("PAGE".equals(primerProduct.getPurifyType())){
			if(tbn<60){
				if(odTotal<=5){
					primerProduct.setBaseVal(new BigDecimal("0.6"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}else if(odTotal>=6&&odTotal<=10){
					primerProduct.setBaseVal(new BigDecimal("1.2"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}else if(odTotal>=11&&odTotal<=20){
					primerProduct.setBaseVal(new BigDecimal("2.4"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}else{
					primerProduct.setBaseVal(new BigDecimal("0"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}
			}else if(tbn>=60){
				if(odTotal<=5){
					primerProduct.setBaseVal(new BigDecimal("1.2"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}else if(odTotal>=6&&odTotal<=10){
					primerProduct.setBaseVal(new BigDecimal("2.4"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}else if(odTotal>=11&&odTotal<=20){
					primerProduct.setBaseVal(new BigDecimal("4.8"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}else{
					primerProduct.setBaseVal(new BigDecimal("0"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}
			}
		}else if("HPLC".equals(primerProduct.getPurifyType())){
			if(tbn<60){
				if(odTotal<=5){
					primerProduct.setBaseVal(new BigDecimal("0.45"));
					primerProduct.setPurifyVal(new BigDecimal("30"));
				}else if(odTotal>=6&&odTotal<=10){
					primerProduct.setBaseVal(new BigDecimal("0.8"));
					primerProduct.setPurifyVal(new BigDecimal("60"));
				}else if(odTotal>=11&&odTotal<=20){
					primerProduct.setBaseVal(new BigDecimal("1.6"));
					primerProduct.setPurifyVal(new BigDecimal("120"));
				}else{
					primerProduct.setBaseVal(new BigDecimal("0"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}
			}else if(tbn>=60){
				if(odTotal<=5){
					primerProduct.setBaseVal(new BigDecimal("0.8"));
					primerProduct.setPurifyVal(new BigDecimal("30"));
				}else if(odTotal>=6&&odTotal<=10){
					primerProduct.setBaseVal(new BigDecimal("1.6"));
					primerProduct.setPurifyVal(new BigDecimal("60"));
				}else if(odTotal>=11&&odTotal<=20){
					primerProduct.setBaseVal(new BigDecimal("3.2"));
					primerProduct.setPurifyVal(new BigDecimal("120"));
				}else{
					primerProduct.setBaseVal(new BigDecimal("0"));
					primerProduct.setPurifyVal(new BigDecimal("0"));
				}
			}
		}else{
			//primerProduct.setModiPrice(new BigDecimal("0"));//修饰价格
			primerProduct.setBaseVal(new BigDecimal("0"));//碱基单价
			primerProduct.setPurifyVal(new BigDecimal("0"));//纯化价格
		}
		
	}
	
	public void getModiTypePrice(PrimerProduct primerProduct){
		OrderCaculate orderCaculate = new OrderCaculate();
		String modiFiveType = primerProduct.getModiFiveType();
		String modiThreeType = primerProduct.getModiThreeType();
		String modiSpeType = orderCaculate.getModiType(primerProduct.getGeneOrderMidi(),OrderCaculate.modiSpeMap);
	}
	
}
