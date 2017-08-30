package org.one.gene.utils;

import java.math.BigDecimal;

import java.util.*;
/**
 * 系统缓存类
 */
public class CacheUtils {

    private static final String DILIMITER = "^";

    /** 用于缓存 摩尔消光系数 */

    private static Map molarMap = Collections.synchronizedMap(new HashMap());

    
	public static BigDecimal calcMolar(String geneOrder) {
		BigDecimal molar = new BigDecimal(0);
		
		//处理特殊碱基:dI|I;dU|U;dT-NH2|#;
		geneOrder  = geneOrder.replaceAll("\\(dI\\)", "I").replaceAll("\\(dU\\)", "U").replaceAll("\\(dT-NH2\\)", "#");
		
		//去掉其他有括号的值
		geneOrder = getGeneOrder(geneOrder);
		
        //循环序列，逐个计算 摩尔消光系数
		for (int i = 0; i < geneOrder.length(); i++) {
			if (i == 0) {
				try {
					molar = molar.add(CacheUtils.getMolar(geneOrder.substring(0, 1)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					molar = molar.add(CacheUtils.getMolar(geneOrder.substring(i - 1, i + 1)));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return molar;
	}
 


    public static BigDecimal getMolar(String key) throws Exception 
    	{
    	BigDecimal molar = new BigDecimal(0);

    	if(molarMap.size()==0){
            //初始化数值
        	getAllMolar();
    	}

        if (molarMap.containsKey(key)) {
        	molar = (BigDecimal) molarMap.get(key);
        	System.out.println("从map中得到="+key+",value="+molar);

        }

        return molar;
    }

	public static void getAllMolar() {
		 
    	molarMap.put("A", new BigDecimal(15400));
    	molarMap.put("T", new BigDecimal(8700));
    	molarMap.put("C", new BigDecimal(7400));
    	molarMap.put("G", new BigDecimal(11500));
    	molarMap.put("R", new BigDecimal(13450));
    	molarMap.put("Y", new BigDecimal(8050));
    	molarMap.put("M", new BigDecimal(11400));
    	molarMap.put("K", new BigDecimal(10100));
    	molarMap.put("S", new BigDecimal(9450));
    	molarMap.put("W", new BigDecimal(12050));
    	molarMap.put("N", new BigDecimal(10750));
    	molarMap.put("H", new BigDecimal(10498.95));
    	molarMap.put("B", new BigDecimal(9199.08));
    	molarMap.put("V", new BigDecimal(11432.19));
    	molarMap.put("D", new BigDecimal(11865.5));
    	molarMap.put("I", new BigDecimal(12350));
    	molarMap.put("U", new BigDecimal(10000));
    	molarMap.put("#", new BigDecimal(8700));
    	
    	molarMap.put("AA", new BigDecimal(12000));
    	molarMap.put("TA", new BigDecimal(14700));
    	molarMap.put("CA", new BigDecimal(13800));
    	molarMap.put("GA", new BigDecimal(13700));
    	molarMap.put("RA", new BigDecimal(12850));
    	molarMap.put("YA", new BigDecimal(14250));
    	molarMap.put("MA", new BigDecimal(12900));
    	molarMap.put("KA", new BigDecimal(14200));
    	molarMap.put("SA", new BigDecimal(13750));
    	molarMap.put("WA", new BigDecimal(13350));
    	molarMap.put("NA", new BigDecimal(13550));
    	molarMap.put("HA", new BigDecimal(13498.65));
    	molarMap.put("BA", new BigDecimal(14065.26));
    	molarMap.put("VA", new BigDecimal(13165.35));
    	molarMap.put("DA", new BigDecimal(13465.3));
    	molarMap.put("IA", new BigDecimal(13050));
    	molarMap.put("UA", new BigDecimal(13400));
    	molarMap.put("#A", new BigDecimal(14700));
    	
    	molarMap.put("AT", new BigDecimal(7400));
    	molarMap.put("TT", new BigDecimal(8100));
    	molarMap.put("CT", new BigDecimal(7800));
    	molarMap.put("GT", new BigDecimal(8500));
    	molarMap.put("RT", new BigDecimal(7950));
    	molarMap.put("YT", new BigDecimal(7950));
    	molarMap.put("MT", new BigDecimal(7600));
    	molarMap.put("KT", new BigDecimal(8300));
    	molarMap.put("ST", new BigDecimal(8150));
    	molarMap.put("WT", new BigDecimal(7750));
    	molarMap.put("NT", new BigDecimal(7950));
    	molarMap.put("HT", new BigDecimal(7765.89));
    	molarMap.put("BT", new BigDecimal(8132.52));
    	molarMap.put("VT", new BigDecimal(7899.21));
    	molarMap.put("DT", new BigDecimal(7999.18));
    	molarMap.put("IT", new BigDecimal(8250));
    	molarMap.put("UT", new BigDecimal(6800));
    	molarMap.put("#T", new BigDecimal(8100));
    	
    	molarMap.put("AC", new BigDecimal(5800));
    	molarMap.put("TC", new BigDecimal(7500));
    	molarMap.put("CC", new BigDecimal(7200));
    	molarMap.put("GC", new BigDecimal(6100));
    	molarMap.put("RC", new BigDecimal(5950));
    	molarMap.put("YC", new BigDecimal(7350));
    	molarMap.put("MC", new BigDecimal(6500));
    	molarMap.put("KC", new BigDecimal(6800));
    	molarMap.put("SC", new BigDecimal(6650));
    	molarMap.put("WC", new BigDecimal(6650));
    	molarMap.put("NC", new BigDecimal(6650));
    	molarMap.put("HC", new BigDecimal(6832.65));
    	molarMap.put("BC", new BigDecimal(6932.64));
    	molarMap.put("VC", new BigDecimal(6366.03));
    	molarMap.put("DC", new BigDecimal(6466));
    	molarMap.put("IC", new BigDecimal(6250));
    	molarMap.put("UC", new BigDecimal(6200));
    	molarMap.put("#C", new BigDecimal(7500));

    	molarMap.put("AG", new BigDecimal(9600));
    	molarMap.put("TG", new BigDecimal(10300));
    	molarMap.put("CG", new BigDecimal(10600));
    	molarMap.put("GG", new BigDecimal(10100));
    	molarMap.put("RG", new BigDecimal(9850));
    	molarMap.put("YG", new BigDecimal(10450));
    	molarMap.put("MG", new BigDecimal(10100));
    	molarMap.put("KG", new BigDecimal(10200));
    	molarMap.put("SG", new BigDecimal(10350));
    	molarMap.put("WG", new BigDecimal(9950));
    	molarMap.put("NG", new BigDecimal(6650));
    	molarMap.put("HG", new BigDecimal(10165.65));
    	molarMap.put("BG", new BigDecimal(10332.3));
    	molarMap.put("VG", new BigDecimal(10098.99));
    	molarMap.put("DG", new BigDecimal(9998.98));
    	molarMap.put("IG", new BigDecimal(10050));
    	molarMap.put("UG", new BigDecimal(9000));
    	molarMap.put("#G", new BigDecimal(10300));

    	molarMap.put("AR", new BigDecimal(10800));
    	molarMap.put("TR", new BigDecimal(12500));
    	molarMap.put("CR", new BigDecimal(12200));
    	molarMap.put("GR", new BigDecimal(11900));
    	molarMap.put("RR", new BigDecimal(11350));
    	molarMap.put("YR", new BigDecimal(12350));
    	molarMap.put("MR", new BigDecimal(11500));
    	molarMap.put("KR", new BigDecimal(12200));
    	molarMap.put("SR", new BigDecimal(10350));
    	molarMap.put("WR", new BigDecimal(11650));
    	molarMap.put("NR", new BigDecimal(11850));
    	molarMap.put("HR", new BigDecimal(8832.45));
    	molarMap.put("BR", new BigDecimal(12198.78));
    	molarMap.put("VR", new BigDecimal(11632.17));
    	molarMap.put("DR", new BigDecimal(11732.14));
    	molarMap.put("IR", new BigDecimal(11550));
    	molarMap.put("UR", new BigDecimal(11200));
    	molarMap.put("#R", new BigDecimal(12500));

    	molarMap.put("AY", new BigDecimal(6600));
    	molarMap.put("TY", new BigDecimal(7800));
    	molarMap.put("CY", new BigDecimal(7500));
    	molarMap.put("GY", new BigDecimal(7300));
    	molarMap.put("RY", new BigDecimal(6950));
    	molarMap.put("YY", new BigDecimal(7650));
    	molarMap.put("MY", new BigDecimal(7050));
    	molarMap.put("KY", new BigDecimal(7550));
    	molarMap.put("SY", new BigDecimal(7400));
    	molarMap.put("WY", new BigDecimal(7200));
    	molarMap.put("NY", new BigDecimal(7300));
    	molarMap.put("HY", new BigDecimal(7299.27));
    	molarMap.put("BY", new BigDecimal(7532.58));
    	molarMap.put("VY", new BigDecimal(7132.62));
    	molarMap.put("DY", new BigDecimal(7232.59));
    	molarMap.put("IY", new BigDecimal(7250));
    	molarMap.put("UY", new BigDecimal(6500));
    	molarMap.put("#Y", new BigDecimal(7800));

    	molarMap.put("AN", new BigDecimal(8700));    
    	molarMap.put("TN", new BigDecimal(10150));    
    	molarMap.put("CN", new BigDecimal(9850));    
    	molarMap.put("GN", new BigDecimal(9600));    
    	molarMap.put("RN", new BigDecimal(9150));    
    	molarMap.put("YN", new BigDecimal(10000));    
    	molarMap.put("MN", new BigDecimal(9275));    
    	molarMap.put("KN", new BigDecimal(9875));    
    	molarMap.put("SN", new BigDecimal(9725));    
    	molarMap.put("WN", new BigDecimal(9425));    
    	molarMap.put("NN", new BigDecimal(9575));    
    	molarMap.put("HN", new BigDecimal(9565.71));    
    	molarMap.put("BN", new BigDecimal(9865.68));    
    	molarMap.put("VN", new BigDecimal(9382.395));    
    	molarMap.put("DN", new BigDecimal(9482.36));
    	molarMap.put("IN", new BigDecimal(9400));
    	molarMap.put("UN", new BigDecimal(8850));
    	molarMap.put("#N", new BigDecimal(10150));

    	molarMap.put("AK", new BigDecimal(8500));
    	molarMap.put("TK", new BigDecimal(9200));
    	molarMap.put("CK", new BigDecimal(9200));
    	molarMap.put("GK", new BigDecimal(9300));
    	molarMap.put("RK", new BigDecimal(8900));
    	molarMap.put("YK", new BigDecimal(9200));
    	molarMap.put("MK", new BigDecimal(8850));
    	molarMap.put("KK", new BigDecimal(9250));
    	molarMap.put("SK", new BigDecimal(9250));
    	molarMap.put("WK", new BigDecimal(8850));
    	molarMap.put("NK", new BigDecimal(9050));
    	molarMap.put("HK", new BigDecimal(8965.77));
    	molarMap.put("BK", new BigDecimal(9232.41));
    	molarMap.put("VK", new BigDecimal(8999.1));
    	molarMap.put("DK", new BigDecimal(8999.08));
    	molarMap.put("IK", new BigDecimal(9150));
    	molarMap.put("UK", new BigDecimal(7900));
    	molarMap.put("#K", new BigDecimal(9200));

    	molarMap.put("AM", new BigDecimal(8900));
    	molarMap.put("TM", new BigDecimal(11100));
    	molarMap.put("CM", new BigDecimal(10500));
    	molarMap.put("GM", new BigDecimal(9900));
    	molarMap.put("RM", new BigDecimal(9400));
    	molarMap.put("YM", new BigDecimal(10800));
    	molarMap.put("MM", new BigDecimal(9700));
    	molarMap.put("KM", new BigDecimal(10500));
    	molarMap.put("SM", new BigDecimal(10200));
    	molarMap.put("WM", new BigDecimal(10000));
    	molarMap.put("NM", new BigDecimal(10100));
    	molarMap.put("HM", new BigDecimal(10165.65));
    	molarMap.put("BM", new BigDecimal(10498.95));
    	molarMap.put("VM", new BigDecimal(9765.69));
    	molarMap.put("DM", new BigDecimal(9965.65));
    	molarMap.put("IM", new BigDecimal(9650));
    	molarMap.put("UM", new BigDecimal(9800));
    	molarMap.put("#M", new BigDecimal(11100));

    	molarMap.put("AS", new BigDecimal(7700));
    	molarMap.put("TS", new BigDecimal(8900));
    	molarMap.put("CS", new BigDecimal(8900));
    	molarMap.put("GS", new BigDecimal(8100));
    	molarMap.put("RS", new BigDecimal(7900));
    	molarMap.put("YS", new BigDecimal(8900));
    	molarMap.put("MS", new BigDecimal(8300));
    	molarMap.put("KS", new BigDecimal(8500));
    	molarMap.put("SS", new BigDecimal(8500));
    	molarMap.put("WS", new BigDecimal(8300));
    	molarMap.put("NS", new BigDecimal(8400));
    	molarMap.put("HS", new BigDecimal(8499.15));
    	molarMap.put("BS", new BigDecimal(8632.47));
    	molarMap.put("VS", new BigDecimal(8232.51));
    	molarMap.put("DS", new BigDecimal(8232.49));
    	molarMap.put("IS", new BigDecimal(8150));
    	molarMap.put("US", new BigDecimal(7600));
    	molarMap.put("#S", new BigDecimal(8900));

    	molarMap.put("AW", new BigDecimal(9700));
    	molarMap.put("TW", new BigDecimal(11400));
    	molarMap.put("CW", new BigDecimal(10800));
    	molarMap.put("GW", new BigDecimal(11100));
    	molarMap.put("RW", new BigDecimal(10400));
    	molarMap.put("YW", new BigDecimal(11100));
    	molarMap.put("MW", new BigDecimal(10250));
    	molarMap.put("KW", new BigDecimal(11250));
    	molarMap.put("SW", new BigDecimal(10950));
    	molarMap.put("WW", new BigDecimal(10550));
    	molarMap.put("NW", new BigDecimal(10750));
    	molarMap.put("HW", new BigDecimal(10632.27));
    	molarMap.put("BW", new BigDecimal(11098.89));
    	molarMap.put("VW", new BigDecimal(10532.28));
    	molarMap.put("DW", new BigDecimal(10732.24));
    	molarMap.put("IW", new BigDecimal(10650));
    	molarMap.put("UW", new BigDecimal(10100));
    	molarMap.put("#W", new BigDecimal(11400));

    	molarMap.put("AH", new BigDecimal(8397.62));
    	molarMap.put("TH", new BigDecimal(10098.12));
    	molarMap.put("CH", new BigDecimal(9598.3));
    	molarMap.put("GH", new BigDecimal(9431.24));
    	molarMap.put("RH", new BigDecimal(8914.43));
    	molarMap.put("YH", new BigDecimal(9848.21));
    	molarMap.put("MH", new BigDecimal(8997.96));
    	molarMap.put("KH", new BigDecimal(9764.68));
    	molarMap.put("SH", new BigDecimal(9514.77));
    	molarMap.put("WH", new BigDecimal(9247.87));
    	molarMap.put("NH", new BigDecimal(9381.32));
    	molarMap.put("HH", new BigDecimal(9363.743532));
    	molarMap.put("BH", new BigDecimal(9708.25));
    	molarMap.put("VH", new BigDecimal(9147.47));
    	molarMap.put("DH", new BigDecimal(9308.04));
    	molarMap.put("IH", new BigDecimal(9181.18));
    	molarMap.put("UH", new BigDecimal(8798.12));
    	molarMap.put("#H", new BigDecimal(10098.12));

    	molarMap.put("AB", new BigDecimal(7597.7));
    	molarMap.put("TB", new BigDecimal(8631.6));
    	molarMap.put("CB", new BigDecimal(8531.74));
    	molarMap.put("GB", new BigDecimal(8231.36));
    	molarMap.put("RB", new BigDecimal(7914.53));
    	molarMap.put("YB", new BigDecimal(8581.67));
    	molarMap.put("MB", new BigDecimal(8064.72));
    	molarMap.put("KB", new BigDecimal(8431.48));
    	molarMap.put("SB", new BigDecimal(8381.55));
    	molarMap.put("WB", new BigDecimal(8114.65));
    	molarMap.put("NB", new BigDecimal(8248.1));
    	molarMap.put("HB", new BigDecimal(8252.854632));
    	molarMap.put("BB", new BigDecimal(8464.05));
    	molarMap.put("VB", new BigDecimal(8119.45));
    	molarMap.put("DB", new BigDecimal(8152.71));
    	molarMap.put("IB", new BigDecimal(8181.28));
    	molarMap.put("UB", new BigDecimal(7331.6));
    	molarMap.put("#B", new BigDecimal(8631.6));

    	molarMap.put("AV", new BigDecimal(9130.88));
    	molarMap.put("TV", new BigDecimal(10831.38));
    	molarMap.put("CV", new BigDecimal(10531.54));
    	molarMap.put("GV", new BigDecimal(9964.52));
    	molarMap.put("RV", new BigDecimal(9547.7));
    	molarMap.put("YV", new BigDecimal(10681.46));
    	molarMap.put("MV", new BigDecimal(9831.21));
    	molarMap.put("KV", new BigDecimal(10397.95));
    	molarMap.put("SV", new BigDecimal(10248.03));
    	molarMap.put("WV", new BigDecimal(9981.13));
    	molarMap.put("NV", new BigDecimal(10114.58));
    	molarMap.put("HV", new BigDecimal(10163.58));
    	molarMap.put("BV", new BigDecimal(10441.43));
    	molarMap.put("VV", new BigDecimal(9874.66));
    	molarMap.put("DV", new BigDecimal(9974.57));
    	molarMap.put("IV", new BigDecimal(9781.12));
    	molarMap.put("UV", new BigDecimal(9531.38));
    	molarMap.put("#V", new BigDecimal(10831.38));

    	molarMap.put("AD", new BigDecimal(9664.16));
    	molarMap.put("TD", new BigDecimal(11031.36));
    	molarMap.put("CD", new BigDecimal(10731.52));
    	molarMap.put("GD", new BigDecimal(10764.44));
    	molarMap.put("RD", new BigDecimal(10214.3));
    	molarMap.put("YD", new BigDecimal(10881.44));
    	molarMap.put("MD", new BigDecimal(10197.84));
    	molarMap.put("KD", new BigDecimal(10897.9));
    	molarMap.put("SD", new BigDecimal(10747.98));
    	molarMap.put("WD", new BigDecimal(10347.76));
    	molarMap.put("ND", new BigDecimal(10547.87));
    	molarMap.put("HD", new BigDecimal(10474.63));
    	molarMap.put("BD", new BigDecimal(10841.35));
    	molarMap.put("VD", new BigDecimal(10385.67));
    	molarMap.put("DD", new BigDecimal(10486));
    	molarMap.put("ID", new BigDecimal(10447.72));
    	molarMap.put("UD", new BigDecimal(9731.36));
    	molarMap.put("#D", new BigDecimal(11031.36));
    	
    	molarMap.put("AI", new BigDecimal(10000));
    	molarMap.put("TI", new BigDecimal(11900));
    	molarMap.put("CI", new BigDecimal(11200));
    	molarMap.put("GI", new BigDecimal(11100));
    	molarMap.put("RI", new BigDecimal(10550));
    	molarMap.put("YI", new BigDecimal(11550));
    	molarMap.put("MI", new BigDecimal(10600));
    	molarMap.put("KI", new BigDecimal(11500));
    	molarMap.put("SI", new BigDecimal(11150));
    	molarMap.put("WI", new BigDecimal(10950));
    	molarMap.put("NI", new BigDecimal(11050));
    	molarMap.put("HI", new BigDecimal(11032.23));
    	molarMap.put("BI", new BigDecimal(11398.86));
    	molarMap.put("VI", new BigDecimal(10765.59));
    	molarMap.put("DI", new BigDecimal(10998.88));
    	molarMap.put("II", new BigDecimal(10650));
    	molarMap.put("UI", new BigDecimal(10600));
    	molarMap.put("#I", new BigDecimal(11900));
    	
    	molarMap.put("AU", new BigDecimal(7400));
    	molarMap.put("TU", new BigDecimal(8100));
    	molarMap.put("CU", new BigDecimal(7800));
    	molarMap.put("GU", new BigDecimal(8500));
    	molarMap.put("RU", new BigDecimal(7950));
    	molarMap.put("YU", new BigDecimal(7950));
    	molarMap.put("MU", new BigDecimal(7600));
    	molarMap.put("KU", new BigDecimal(8300));
    	molarMap.put("SU", new BigDecimal(8150));
    	molarMap.put("WU", new BigDecimal(7750));
    	molarMap.put("NU", new BigDecimal(7950));
    	molarMap.put("HU", new BigDecimal(7765.89));
    	molarMap.put("BU", new BigDecimal(8132.52));
    	molarMap.put("VU", new BigDecimal(7899.21));
    	molarMap.put("DU", new BigDecimal(7999.18));
    	molarMap.put("IU", new BigDecimal(8250));
    	molarMap.put("UU", new BigDecimal(6800));
    	molarMap.put("#U", new BigDecimal(8100));
    	
    	molarMap.put("A#", new BigDecimal(7400));
    	molarMap.put("T#", new BigDecimal(8100));
    	molarMap.put("C#", new BigDecimal(7800));
    	molarMap.put("G#", new BigDecimal(8500));
    	molarMap.put("R#", new BigDecimal(7950));
    	molarMap.put("Y#", new BigDecimal(7950));
    	molarMap.put("M#", new BigDecimal(7600));
    	molarMap.put("K#", new BigDecimal(8300));
    	molarMap.put("S#", new BigDecimal(8150));
    	molarMap.put("W#", new BigDecimal(7750));
    	molarMap.put("N#", new BigDecimal(7950));
    	molarMap.put("H#", new BigDecimal(7765.89));
    	molarMap.put("B#", new BigDecimal(8132.52));
    	molarMap.put("V#", new BigDecimal(7899.21));
    	molarMap.put("D#", new BigDecimal(7999.18));
    	molarMap.put("I#", new BigDecimal(8250));
    	molarMap.put("U#", new BigDecimal(6800));
    	molarMap.put("##", new BigDecimal(8100));
    	
	}

	public static String getGeneOrder(String str){
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

    public static void main(String args[]) throws Exception {

    	String geneOrder = "A(dI)(dI)(dU)(dU)(dT-NH2)(@@@)A";

        System.out.println(calcMolar(geneOrder));
//        System.out.println(geneOrder.replaceAll("\\(dI\\)", "I").replaceAll("\\(dU\\)", "U").replaceAll("\\(dT-NH2\\)", "#"));

    } 



}