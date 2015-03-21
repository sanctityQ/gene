package org.one.gene.excel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import org.springframework.stereotype.Component;

import com.google.common.base.CharMatcher;

/**
 * 为避免double精度问题，计算中使用BigDecimal和String
 * @author ThinkPad User
 *
 */
@Component
public class OrderCaculate {

	public final static Map<String,String> modiMidMap = new HashMap<String,String>();  
	static {  
		modiMidMap.put("Cy5", "635.88");  
		modiMidMap.put("TAMRA", "567.66");  
		modiMidMap.put("ROX", "672.79");
		modiMidMap.put("Dabcyl", "407.49");
		modiMidMap.put("Biotin", "382.5");
		modiMidMap.put("Digoxin", "854");
		modiMidMap.put("FAM", "513.5");
	}
	public final static Map<String,String> modiSpeMap = new HashMap<String,String>();  
	static {
		//* 表示硫代
		modiSpeMap.put("dI", "314.20");  
		modiSpeMap.put("dU", "290.17");  
		modiSpeMap.put("dT-NH2", "155.2");
		modiSpeMap.put("Spacer(C12)", "263.3");
		modiSpeMap.put("*", "16");
		modiSpeMap.put("5-Methyl dC", "303.18");
	} 

	//获取字符串长度
	public String getAnJiShu(String str){
		return String.valueOf(str.length());
	}
	
	/**
	 * 获取引物序列 
	 * TCGUINBDHKMRSWYV 需要自动去除除以上字符外的所有字符，字符可以自动转换为大写
	 */
	public String getYWSeqValue(String str){
        return CharMatcher.anyOf("ATCGUINBDHKMRSWYV").retainFrom(str.toUpperCase());
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

	
	public String getGeneOrder(String str){
		//获取不包含在括号内字符串
		String result = "";
		Stack<Character> stack = new Stack<Character>();	 
		StringBuffer resultbuf = new StringBuffer();	 
		for(int i=0;i<str.length();i++){		 
			char c = str.charAt(i);		 
			if('(' == c){			 
				stack.push(c);		 
			}else if(')' == c){			 
				stack.pop(); 		 
			}else if(stack.isEmpty()){			
				resultbuf.append(c);		 
			}	 
		}
		result = resultbuf.toString();
		return result;
	}
	
	public String valiGeneOrder(String str){
		//获取不包含在括号内字符串
		String result = "";
		Stack<Character> stack = new Stack<Character>();	 
		for(int i=0;i<str.length();i++){		 
			char c = str.charAt(i);		 
			if('(' == c){			 
				stack.push(c);		 
			}else if(')' == c){			 
				stack.pop(); 		 
			}	 
		}
		if (!stack.isEmpty()) {
			result = "修饰分子格式不完全正确,请您确认()是否成对出现！";
		}
		return result;
	}
	
	//获取序列中的所有修饰
	public String getModiStr(String str){
		//获取括号内数据
		StringBuffer resultbuf = new StringBuffer();
        int m=0,n=0; 
        int count=0; 
        for(int i=0;i<str.length();i++){ 
            if(str.charAt(i)=='('){ 
                if(count==0){ 
                    m=i; 
                } 
                count++; 
            } 
            if(str.charAt(i)==')'){ 
                count--; 
                if(count==0){ 
                    n=i; 
                    resultbuf.append(str.substring(m+1,n)).append(",");
                } 
            } 
        } 
        return resultbuf.toString();
	}
	
	//将序列中修饰分类
	public String getModiType(String geneOrder,Map<String,String> modiMap){
		String type = "";
		String moditype = getModiStr(geneOrder);
		String[] moditypes = moditype.split(",");
		for(int i=0;i<moditypes.length;i++){
			if(modiMap.containsKey(moditypes[i])){
				type = type+moditypes[i]+",";
			}
		}
		
		return getCountStr(type);
	}
	//不统计修饰个数
	public String getNoCountModiType(String geneOrder,Map<String,String> modiMap){
		String type = "";
		String moditype = getModiStr(geneOrder);
		String[] moditypes = moditype.split(",");
		for(int i=0;i<moditypes.length;i++){
			if(modiMap.containsKey(moditypes[i])){
				type = type+moditypes[i]+",";
			}
		}
		
		return type;
	}
	
	public String  getCountStr(String str) {
		//将字符串拆分放到ArrayList集合中处理 begin
		String[] strArray = str.split(",");
		ArrayList<String> Mylist = new ArrayList<String>();
		for(int i=0;i<strArray.length;i++){
			String subStr = strArray[i];
			Mylist.add(subStr);
		}
		//将字符串拆分放到ArrayList集合中处理 end
		//统计每个元素的个数，并将统计结果放到hashtable中key:元素值 value:(统计个数+元素值）
		Hashtable<String,String> hashTable = new Hashtable<String,String> ();
		for(int i=0;i<Mylist.size();i++){
			  //System.out.println(Mylist.get(i)+" 在List中存在" + Collections.frequency(Mylist,Mylist.get(i)) + "个");
			  int counts = Collections.frequency(Mylist,Mylist.get(i));
			  String value = "";
			  //如果统计个数为1个，则不拼接统计个数到新字符串中
			  if(counts!=1){
				  value = Collections.frequency(Mylist,Mylist.get(i))+Mylist.get(i);
			  }else{
				  value = Mylist.get(i);
			  }
			  hashTable.put(Mylist.get(i),value);
		} 
		//循环统计结果hashtable，重新整合字符串
		String result = "";
		for(String val : hashTable.values()){
			result = result+val+","; 
		}
		return result;
	}
	
	public static void main(String[] args) {
		OrderCaculate ee = new OrderCaculate();
		String val = ee.getModiType("TCGAGAAGCTTGG(dI)C(dI)A(dI)GC(Cy5)GGGCCCC(dU)TACCCCGCGGG",modiSpeMap);
		System.out.println(val);
	}
}
