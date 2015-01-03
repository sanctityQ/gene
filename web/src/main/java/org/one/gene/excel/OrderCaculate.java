package org.one.gene.excel;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.CharMatcher;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 为避免double精度问题，计算中使用BigDecimal和String
 * @author ThinkPad User
 *
 */
@Component
public class OrderCaculate {

	
	//获取字符串长度
	public String getAnJiShu(String str){
		return String.valueOf(str.length());
	}
	
	/**
	 * 获取引物序列 
	 * TCGUINBDHKMRSWYV 需要自动去除除以上字符外的所有字符，字符可以自动转换为大写
	 */
	public String getYWSeqValue(String str){
        CharMatcher charMatcher = CharMatcher.anyOf(str.toUpperCase());
        return charMatcher.retainFrom("ATCGUINBDHKMRSWYV");
//		str = str.toUpperCase();
//		char [] ch = "TCGUINBDHKMRSWYV".toCharArray();
//		Map<Character,Character> map = new TreeMap<Character,Character>();
//		for (int i = 0; i < ch.length; i++){
//			map.put(ch[i], ch[i]);
//		}
//		StringBuilder sb = new StringBuilder();
//
//		char [] chStr = str.toCharArray();
//		for (int j = 0; j < chStr.length; j++){
//			if(map.containsKey(chStr[j])){
//				sb.append(chStr[j]);
//			}
//		}
//		return sb.toString();
	}
	
	
	//获取指定字符个数
	public String getCount(String str,char chr){
        char [] ch = str.toCharArray();
		Map<Character,Integer> map = new TreeMap<Character,Integer>();
		for (int i = 0; i < ch.length; i++){
			//只统计里面英语字母的个数
			if(!(ch[i]>='a'&&ch[i]<='z'||ch[i]>='A'&&ch[i]<='Z'))
				continue;
			//获取字符数组顺序对应的值 
			Integer value = map.get(ch[i]); 
			int count=1;
			if(value!=null){
				count = value+1;
			}
			map.put(ch[i], count);
		}
		String chrCount = String.valueOf(map.get(chr));
		if("null".equals(chrCount) || "".equals(chrCount) || chrCount==null){
			chrCount = "0";
		}
		return chrCount;
	}

	//分装管数 = nmol总量/ ‘nmol/tube' 或 OD总量/ ‘OD/tube' 允许有余数，有余数的在订单修改页面中以红色标志

	
	
	//GC(%) 100 * (#C + #G) / 碱基数
	public String getGC(String str){
		String strAdd = new BigDecimal(StringUtils.countMatches(str,"C")).add(new BigDecimal(StringUtils.countMatches(str,"G"))).toString();
		String strMul = new BigDecimal("100").multiply(new BigDecimal(strAdd)).toString();
		String strDiv = new BigDecimal(strMul).divide(new BigDecimal(getAnJiShu(str)),4,BigDecimal.ROUND_HALF_UP).toString();
		return strDiv.toString();
	}
	
	//TM
	  //当碱基数 > 20 = 81.5+(0.41 * GC(%)) - (600/碱基数）- 16.6 ，保留1位小数
	  //当碱基数 < 20 = 4 * (#C + #G) + 2 * (#A + #T)
	public String getTM(String str){
		String dbgc = getGC(str);
		int countC = StringUtils.countMatches(str,"C");
		int countG = StringUtils.countMatches(str,"G");
		int countA = StringUtils.countMatches(str,"A");
		int countT = StringUtils.countMatches(str,"T");
		String tm = "";
		if(str.length()>20){
			tm = new BigDecimal("81.5").add(new BigDecimal("0.41").multiply(new BigDecimal(dbgc)))
					.subtract(new BigDecimal("600").divide(new BigDecimal(getAnJiShu(str)),2,BigDecimal.ROUND_HALF_UP))
					.subtract(new BigDecimal("16.6")).setScale(1, RoundingMode.HALF_UP).toString();
		}
		if (str.length()<20){
			tm = new BigDecimal("4").multiply(new BigDecimal(countC).add(new BigDecimal(countG)))
			.add(new BigDecimal("2").multiply(new BigDecimal(countA).add(new BigDecimal(countT))))
			.setScale(1, RoundingMode.HALF_UP).toString();
		}
		
		return tm;
	}
	
	//MW
/*	MW =  (313.2 * #A + 289.2 * #C + 329.2 * #G+ 304.2 * #T + #U * 290.2 + #I * 314.2 + #N * 309 
	         + #B * 307.5 + #D * 315.5 + #H * 302.2 + #K * 316.7 + #M * 301.2 + #R * 321.2 
	         + #S * 309.2 + #V * 310.5 + #W * 308.7 + #Y * 296.7 - 61) + 每种修饰的分子量()保留1位小数 */
	public String getMW(String str){
		String mw = "";
		mw = new BigDecimal("313.2").multiply(new BigDecimal(StringUtils.countMatches(str,"A")))
			.add(new BigDecimal("289.2").multiply(new BigDecimal(StringUtils.countMatches(str,"C"))))
			.add(new BigDecimal("329.2").multiply(new BigDecimal(StringUtils.countMatches(str,"G"))))
			.add(new BigDecimal("304.2").multiply(new BigDecimal(StringUtils.countMatches(str,"T"))))
			.add(new BigDecimal("290.2").multiply(new BigDecimal(StringUtils.countMatches(str,"U"))))
			.add(new BigDecimal("314.2").multiply(new BigDecimal(StringUtils.countMatches(str,"I"))))
			.add(new BigDecimal("309").multiply(new BigDecimal(StringUtils.countMatches(str,"N"))))
			.add(new BigDecimal("307.5").multiply(new BigDecimal(StringUtils.countMatches(str,"B"))))
			.add(new BigDecimal("315.5").multiply(new BigDecimal(StringUtils.countMatches(str,"D"))))
			.add(new BigDecimal("302.2").multiply(new BigDecimal(StringUtils.countMatches(str,"H"))))
			.add(new BigDecimal("316.7").multiply(new BigDecimal(StringUtils.countMatches(str,"K"))))
			.add(new BigDecimal("301.2").multiply(new BigDecimal(StringUtils.countMatches(str,"M"))))
			.add(new BigDecimal("321.2").multiply(new BigDecimal(StringUtils.countMatches(str,"R"))))
			.add(new BigDecimal("309.2").multiply(new BigDecimal(StringUtils.countMatches(str,"S"))))
			.add(new BigDecimal("310.5").multiply(new BigDecimal(StringUtils.countMatches(str,"V"))))
			.add(new BigDecimal("308.7").multiply(new BigDecimal(StringUtils.countMatches(str,"W"))))
			.add(new BigDecimal("296.7").multiply(new BigDecimal(StringUtils.countMatches(str,"Y"))))
			.subtract(new BigDecimal(61))
//			.add(每种修饰的分子量());
			.setScale(1,RoundingMode.HALF_UP).toString();
		return mw;
	}
	
	//μg/OD = 'nmol/tube' * 'MW' /1000
	public String getg_OD(String str,String nmol_tube){
		if("".equals(nmol_tube)){
			return "0.0";
		}
		return new BigDecimal(nmol_tube).multiply(new BigDecimal(getMW(str)).divide(new BigDecimal("1000"))).toString();
	}
	
	/*OD/μmol = #A * 15.4 + #T * 8.8 + #C * 7.3 + #G * 11.7 + #U * 10.1 
				+ #I * 12.25 + #N * 10.95 + #B * 9.5 + #D * 12.1 + #H * 10.7 
				+ #K * 10.6 + #M * 11.4 + #R * 13.6 + #S * 9.6 + #V * 11.5 + #W * 12.3 + #Y * 8.35*/
	public String getOD_Vmol(String str){
		String od_vmole = "";
		
		od_vmole = new BigDecimal(StringUtils.countMatches(str,"A")).multiply(new BigDecimal("15.4"))
				.add(new BigDecimal(StringUtils.countMatches(str,"T")).multiply(new BigDecimal("8.8")))
				.add(new BigDecimal(StringUtils.countMatches(str,"C")).multiply(new BigDecimal("7.3")))
				.add(new BigDecimal(StringUtils.countMatches(str,"G")).multiply(new BigDecimal("11.7")))
				.add(new BigDecimal(StringUtils.countMatches(str,"U")).multiply(new BigDecimal("10.1")))
				.add(new BigDecimal(StringUtils.countMatches(str,"I")).multiply(new BigDecimal("12.25")))
				.add(new BigDecimal(StringUtils.countMatches(str,"N")).multiply(new BigDecimal("10.95")))
				.add(new BigDecimal(StringUtils.countMatches(str,"B")).multiply(new BigDecimal("9.5")))
				.add(new BigDecimal(StringUtils.countMatches(str,"D")).multiply(new BigDecimal("12.1")))
				.add(new BigDecimal(StringUtils.countMatches(str,"H")).multiply(new BigDecimal("10.7")))
				.add(new BigDecimal(StringUtils.countMatches(str,"K")).multiply(new BigDecimal("10.6")))
				.add(new BigDecimal(StringUtils.countMatches(str,"M")).multiply(new BigDecimal("11.4")))
				.add(new BigDecimal(StringUtils.countMatches(str,"R")).multiply(new BigDecimal("13.6")))
				.add(new BigDecimal(StringUtils.countMatches(str,"S")).multiply(new BigDecimal("9.6")))
				.add(new BigDecimal(StringUtils.countMatches(str,"V")).multiply(new BigDecimal("11.5")))
				.add(new BigDecimal(StringUtils.countMatches(str,"W")).multiply(new BigDecimal("12.3")))
				.add(new BigDecimal(StringUtils.countMatches(str,"Y")).multiply(new BigDecimal("8.35"))).toString();
		
		return od_vmole;
	}
	
	//OD/tube = （'nmol/tube' * 'OD/μmol'）/ 1000
	public String getOD_tube(String str,String nmol_tube){
		if("".equals(nmol_tube)){
			return "0.0";
		}
		return new BigDecimal(nmol_tube).multiply(new BigDecimal(getOD_Vmol(str))).divide(new BigDecimal("1000")).setScale(1,RoundingMode.HALF_UP).toString();
	}
	//OD总量 = （'nmol总量' * 'OD/μmol'）/ 1000
	public String getODTotal(String str,String nmolTotal){
		if("".equals(nmolTotal)){
			return "0.0";
		}
		return new BigDecimal(nmolTotal).multiply(new BigDecimal(getOD_Vmol(str))).divide(new BigDecimal("1000")).setScale(1,RoundingMode.HALF_UP).toString();		
	}
	//nmole/tube = 1000 * 'OD/tube' / 'OD/μmol'
	public String getnmole_tube(String str,String od_tube){
		if("".equals(od_tube)){
			return "0.0";
		}
		return new BigDecimal("1000").multiply(new BigDecimal(od_tube).divide(new BigDecimal(getOD_Vmol(str)),2,BigDecimal.ROUND_HALF_UP)).setScale(1,RoundingMode.HALF_UP).toString();
	}
	//noml总量 = 1000 * 'OD总量' / 'OD/μmol'
	public String getnmoleTotal(String str,String odTotal){
		if("".equals(odTotal)){
			return "0.0";
		}
		return new BigDecimal("1000").multiply(new BigDecimal(odTotal).divide(new BigDecimal(getOD_Vmol(str)),2,BigDecimal.ROUND_HALF_UP)).setScale(1,RoundingMode.HALF_UP).toString();
	}
	//100pmole/μl = 'nmol/tube' * 10
	public String getpmol_vl(String nmol_tube){
		if("".equals(nmol_tube)){
			return "0.0";
		}
		return new BigDecimal(nmol_tube).multiply(new BigDecimal("10")).setScale(1,RoundingMode.HALF_UP).toString();
	}

}
