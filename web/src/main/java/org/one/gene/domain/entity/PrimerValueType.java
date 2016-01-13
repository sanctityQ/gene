package org.one.gene.domain.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.one.gene.domain.CalculatePrimerValue;
import org.one.gene.excel.OrderCaculate;

public enum PrimerValueType implements CalculatePrimerValue, PrimerType.TypeDesc {

    odTotal {

        @Override
        public String desc() {
            return "odTotal";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            if(primerProduct.isOrderUpType(Order.OrderType.nmol)) {
                //（'nmol/tube' * 'OD/μmol'）/ 1000
                return primerProduct.getNmolTotal().multiply(ODμmol.value(primerProduct))
                        .divide(new BigDecimal(1000)).setScale(2, RoundingMode.HALF_UP);
            }else{
                return primerProduct.getOdTotal();
            }
        }

    },

    odTB {
        @Override
        public String desc() {
            return "OD/tb";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            if(primerProduct.isOrderUpType(Order.OrderType.nmol)) {
                //（'nmol总量' * 'OD/μmol'）/ 1000

                return primerProduct.getNmolTB().multiply(ODμmol.value(primerProduct))
                        .divide(new BigDecimal(1000)).setScale(2, RoundingMode.HALF_UP);
            }else{
                return primerProduct.getOdTB();
            }
        }

    },

    nmolTotal {
        @Override
        public String desc() {
            return "nmolTotal";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct)
        {
            if(primerProduct.isOrderUpType(Order.OrderType.od)) {
                //通过od值计算  1000 * 'OD总量' / 'OD/μmol'
                return new BigDecimal(1000).multiply(primerProduct.getOdTotal()).divide(
                        ODμmol.value(primerProduct), 2, BigDecimal.ROUND_HALF_UP).setScale(2, RoundingMode.HALF_UP);
            }else{
                return primerProduct.getNmolTotal();
            }
        }
    },

    nmolTB {
        @Override
        public String desc() {
            return "nmol/tb";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            if (primerProduct.isOrderUpType(Order.OrderType.od)) {
                //1000 * 'OD/tube' / 'OD/μmol'
                return new BigDecimal(1000).multiply(primerProduct.getOdTB()).divide(
                        ODμmol.value(primerProduct), 2, BigDecimal.ROUND_HALF_UP).setScale(2, RoundingMode.HALF_UP);
            } else {
                return primerProduct.getNmolTB();
            }
        }
    },

    /**
     * 碱基数
     */
    baseCount {
        @Override
        public String desc() {
            return "baseCount";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(primerProduct.getGeneOrder().trim().length());
        }
    },

    av {
        @Override
        public String desc() {
            return "#A";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"A"));
        }

    },

    tv {
        @Override
        public String desc() {
            return "#T";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"T"));
        }
    },

    cv {
        @Override
        public String desc() {
            return "#C";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"C"));
        }
    },

    gv {
        @Override
        public String desc() {
            return "#G";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"G"));
        }
    },

    uv {
        @Override
        public String desc() {
            return "#U";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"U"));
        }
    },

    iv {
        @Override
        public String desc() {
            return "#I";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"I"));
        }
    },
    nv {
        @Override
        public String desc() {
            return "#N";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"N"));
        }
    },
    bv {
        @Override
        public String desc() {
            return "#B";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"B"));
        }
    },
    dv {
        @Override
        public String desc() {
            return "#D";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"D"));
        }
    },

    hv {
        @Override
        public String desc() {
            return "#H";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"H"));
        }
    },


    kv {
        @Override
        public String desc() {
            return "#K";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"K"));
        }
    }, mv {
        @Override
        public String desc() {
            return "#M";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"M"));
        }
    }, rv {
        @Override
        public String desc() {
            return "#R";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"R"));
        }
    }, sv {
        @Override
        public String desc() {
            return "#S";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"S"));
        }
    }, wv {
        @Override
        public String desc() {
            return "#W";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"W"));
        }
    }, yv {
        @Override
        public String desc() {
            return "#Y";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"Y"));
        }
    }, vv {
        @Override
        public String desc() {
            return "#V";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"V"));
        }
    },
    
    dI{
        @Override
        public String desc() {
            return "dI";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrderMidi(),"(dI)"));
        }

    },
    dU{
        @Override
        public String desc() {
            return "dU";
        }
        

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrderMidi(),"(dU)"));
        }

    },
    liudai{
        @Override
        public String desc() {
            return "*";
        }
        

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrderMidi(),"(*)"));
        }

    },
    dTNH2{
        @Override
        public String desc() {
            return "dT-NH2";
        }
        

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrderMidi(),"(dT-NH2)"));
        }

    },
    SpacerC12{
        @Override
        public String desc() {
            return "Spacer(C12)";
        }
        

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrderMidi(),"(Spacer(C12))"));
        }

    },
    MethyldC{
        @Override
        public String desc() {
            return "5-Methyl dC";
        }
        

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrderMidi(),"(5-Methyl dC)"));
        }

    },
    Cy5{
        @Override
        public String desc() {
            return "Cy5";
        }
        

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrderMidi(),"(Cy5)"));
        }

    },
    TAMRA{
        @Override
        public String desc() {
            return "TAMRA";
        }
        

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrderMidi(),"(TAMRA)"));
        }

    },
    ROX{
        @Override
        public String desc() {
            return "ROX";
        }
        

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrderMidi(),"(ROX)"));
        }

    },
    Dabcyl{
        @Override
        public String desc() {
            return "Dabcyl";
        }
        

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrderMidi(),"(Dabcyl)"));
        }

    },
    Biotin{
        @Override
        public String desc() {
            return "Biotin";
        }
        

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrderMidi(),"(Biotin)"));
        }

    },
    Digoxin{
        @Override
        public String desc() {
            return "Digoxin";
        }
        

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrderMidi(),"(Digoxin)"));
        }

    },
    FAM{
        @Override
        public String desc() {
            return "FAM";
        }
        

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrderMidi(),"(FAM)"));
        }

    },

    /**
     * 分装管数
     */
    tb {
        @Override
        public String desc() {
            return "分装管数";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
        	if (primerProduct.isOrderUpType(Order.OrderType.od)) {
        		return odTotal.value(primerProduct).divide(odTB.value(primerProduct), 0, BigDecimal.ROUND_UP);
        	} else {
        		return nmolTotal.value(primerProduct).divide(nmolTB.value(primerProduct), 0, BigDecimal.ROUND_UP);
        	}
        }
    },

    TM {
        @Override
        public String desc() {
            return "TM";
        }
		/**
		  碱基数大于等于20：
		= 81.5+(0.41*GC）- (600/碱基数)	- 16.6
		
		碱基数小于20：
		= 4 * (#C + #G) +(2 * (#A + #T) )
		 */
        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            BigDecimal bc = baseCount.value(primerProduct);
            // > 20
            if(bc.compareTo(new BigDecimal(20)) == 1){
                return new BigDecimal(81.5).add(new BigDecimal(0.41).multiply(GC.value(primerProduct)))
                        .subtract(new BigDecimal(600).divide(baseCount.value(primerProduct),2,BigDecimal.ROUND_HALF_UP))
                        .subtract(new BigDecimal(16.6)).setScale(1, RoundingMode.HALF_UP);
            }else{
                return new BigDecimal(4).multiply(cv.value(primerProduct).add(gv.value(primerProduct)))
                        .add(new BigDecimal(2).multiply(av.value(primerProduct).add(tv.value(primerProduct))))
                        .setScale(1, RoundingMode.HALF_UP);
            }
        }
    },

    MW {
        @Override
        public String desc() {
            return "MW";
        }

        /*(313.2 * #A + 289.2 * #C + 329.2 * #G+ 304.2 * #T + 
        #N * 309 + #B * 307.5 + #D * 315.5+ 
        #H * 302.2 + #K * 316.7 + #M * 301.2 + #R * 321.2 + 
        #S * 309.2 + #V * 310.5 + #W * 308.7 + #Y * 296.7 - 61) + 每种修饰的分子量*/

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
        	
        	Map<String,String> modiFiveMap = new HashMap<String,String>();
        	Map<String,String> modiThreeMap = new HashMap<String,String>();
        	Map<String,String> modiMidMap = new HashMap<String,String>();
        	Map<String,String> modiSpeMap = new HashMap<String,String>();

			if (primerProduct.getProductMolecularMap() != null) {
				  for (Map.Entry<String, BigDecimal> entry : primerProduct.getProductMolecularMap().entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue()+"";
					if (key.startsWith("modiMidType")) {
						
						modiMidMap.put(key.replace("modiMidType_", ""), value);
					} else if (key.startsWith("modiThreeType")) {
						modiThreeMap.put(key.replace("modiThreeType_", ""), value);
					} else if (key.startsWith("modiSpeType")) {
						modiSpeMap.put(key.replace("modiSpeType_", ""), value);
					} else if (key.startsWith("modiFiveType")) {
						modiFiveMap.put(key.replace("modiFiveType_", ""), value);
					}
				  }
			}
			
        	
			BigDecimal modiFiveVal = new BigDecimal("0");
			if(modiFiveMap.get(primerProduct.getModiFiveType())!=null){
				modiFiveVal = new BigDecimal(modiFiveMap.get(primerProduct.getModiFiveType()));
			}
			
			BigDecimal modiThreeVal = new BigDecimal("0");
			if(modiThreeMap.get(primerProduct.getModiThreeType())!=null){
				modiThreeVal = new BigDecimal(modiThreeMap.get(primerProduct.getModiThreeType()));
			}
			
        	BigDecimal modiMidVal = new BigDecimal("0");
			OrderCaculate orderCaculate = new OrderCaculate();
			String modiMidType = orderCaculate.getModiStr(primerProduct.getGeneOrderMidi());
			String[] moditypes = modiMidType.split(",");
			for(int i=0;i<moditypes.length;i++){
				if(modiMidMap.get(moditypes[i])!=null){
					modiMidVal =  modiMidVal.add(new BigDecimal(modiMidMap.get(moditypes[i])));
				}
			}
			
			BigDecimal modiSpeVal = new BigDecimal("0");
			String modiSpeType = orderCaculate.getModiStr(primerProduct.getGeneOrderMidi());
			String[] modiSpeTypes = modiSpeType.split(",");
			for(int i=0;i<modiSpeTypes.length;i++){
				if(modiSpeMap.get(modiSpeTypes[i])!=null){
					modiSpeVal = modiSpeVal.add(new BigDecimal(modiSpeMap.get(modiSpeTypes[i])));
				}
			}
        	
        	return new BigDecimal("313.2").multiply(av.value(primerProduct))
            		.add(new BigDecimal("289.2").multiply(cv.value(primerProduct)))
            		.add(new BigDecimal("329.2").multiply(gv.value(primerProduct)))
            		.add(new BigDecimal("304.2").multiply(tv.value(primerProduct)))
					.add(new BigDecimal("309").multiply(nv.value(primerProduct)))
					.add(new BigDecimal("307.5").multiply(bv.value(primerProduct)))
					.add(new BigDecimal("315.5").multiply(dv.value(primerProduct)))
					.add(new BigDecimal("302.2").multiply(hv.value(primerProduct)))
					.add(new BigDecimal("316.7").multiply(kv.value(primerProduct)))
					.add(new BigDecimal("301.2").multiply(mv.value(primerProduct)))
					.add(new BigDecimal("321.2").multiply(rv.value(primerProduct)))
					.add(new BigDecimal("309.2").multiply(sv.value(primerProduct)))
					.add(new BigDecimal("310.5").multiply(vv.value(primerProduct)))
					.add(new BigDecimal("308.7").multiply(wv.value(primerProduct)))
					.add(new BigDecimal("296.7").multiply(yv.value(primerProduct)))
					.subtract(new BigDecimal(61)).add(modiFiveVal).add(modiThreeVal).add(modiMidVal).add(modiSpeVal)
					.setScale(1, RoundingMode.HALF_UP);
        }
    },

    GC {
        @Override
        public String desc() {
            return "GC";
        }

        /**
         * //GC(%) = 100 * (#C + #G) / 碱基数
         * @param primerProduct
         * @return
         */
        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            BigDecimal strAdd = cv.value(primerProduct).add(gv.value(primerProduct));
            BigDecimal strMul = new BigDecimal(100).multiply(strAdd);
            return strMul.divide(baseCount.value(primerProduct), 4, BigDecimal.ROUND_HALF_UP);
        }
    },

    μgOD {
        @Override
        public String desc() {
            return "μgOD";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
        	return MW.value(primerProduct).divide(ODμmol.value(primerProduct),2, BigDecimal.ROUND_HALF_UP);
        }
    },
    
    ODμmol {
    	@Override
        public String desc() {
            return "ODμmol";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
        	return new BigDecimal("15.4").multiply(av.value(primerProduct))
        	    .add(new BigDecimal("8.8").multiply(tv.value(primerProduct)))
        	    .add(new BigDecimal("7.3").multiply(cv.value(primerProduct)))
        	    .add(new BigDecimal("11.7").multiply(gv.value(primerProduct)))
        	    .add(new BigDecimal("10.1").multiply(uv.value(primerProduct)))
        	    .add(new BigDecimal("12.25").multiply(iv.value(primerProduct)))
        	    .add(new BigDecimal("10.95").multiply(nv.value(primerProduct)))
        	    .add(new BigDecimal("9.5").multiply(bv.value(primerProduct)))
        	    .add(new BigDecimal("12.1").multiply(dv.value(primerProduct)))
        	    .add(new BigDecimal("10.7").multiply(hv.value(primerProduct)))
        	    .add(new BigDecimal("10.6").multiply(kv.value(primerProduct)))
        	    .add(new BigDecimal("11.4").multiply(mv.value(primerProduct)))
        	    .add(new BigDecimal("13.6").multiply(rv.value(primerProduct)))
        	    .add(new BigDecimal("9.6").multiply(sv.value(primerProduct)))
        	    .add(new BigDecimal("11.5").multiply(vv.value(primerProduct)))
        	    .add(new BigDecimal("12.3").multiply(wv.value(primerProduct)))
        	    .add(new BigDecimal("8.35").multiply(yv.value(primerProduct)));
        }
    };

    abstract BigDecimal value(PrimerProduct primerProduct);

    public PrimerProductValue create(PrimerProduct primerProduct) {
        PrimerProductValue primerProductValue = new PrimerProductValue();
        primerProductValue.setPrimerProduct(primerProduct);
        primerProductValue.setCreateTime(DateTime.now().toDate());
        primerProductValue.setType(this);
        primerProductValue.setTypeDesc(this.desc());
        primerProductValue.setValue(this.value(primerProduct).setScale(1, BigDecimal.ROUND_HALF_UP));
        return primerProductValue;
    }

}
