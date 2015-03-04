package org.one.gene.domain.entity;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.one.gene.domain.CalculatePrimerValue;
import org.one.gene.domain.service.PropotiesService;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
                        .divide(new BigDecimal(1000)).setScale(1, RoundingMode.HALF_UP);
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
                        .divide(new BigDecimal(1000)).setScale(1, RoundingMode.HALF_UP);
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
                        ODμmol.value(primerProduct), 2, BigDecimal.ROUND_HALF_UP).setScale(1, RoundingMode.HALF_UP);
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
                        ODμmol.value(primerProduct), 2, BigDecimal.ROUND_HALF_UP).setScale(1, RoundingMode.HALF_UP);
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
            return odTotal.value(primerProduct).divide(odTB.value(primerProduct), 0, BigDecimal.ROUND_UP);
        }
    },

    TM {
        @Override
        public String desc() {
            return "TM";
        }

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
        #U * 290.2 + #I * 252 + #N * 309 + #B * 307.5 + #D * 315.5+ 
        #H * 302.2 + #K * 316.7 + #M * 301.2 + #R * 321.2 + 
        #S * 309.2 + #V * 310.5 + #W * 308.7 + #Y * 296.7 - 61) + 每种修饰的分子量*/

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
        	return new BigDecimal("313.2").multiply(av.value(primerProduct))
            		.add(new BigDecimal("289.2").multiply(cv.value(primerProduct)))
            		.add(new BigDecimal("329.2").multiply(gv.value(primerProduct)))
            		.add(new BigDecimal("304.2").multiply(tv.value(primerProduct)))
            		.add(new BigDecimal("290.2").multiply(uv.value(primerProduct)))
            		.add(new BigDecimal("252").multiply(iv.value(primerProduct)))
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
					.subtract(new BigDecimal(61))
					//临时处理方案，产品管理功能完成优化此处
					.add(new BigDecimal(PropotiesService.getValue(primerProduct.getModiFiveType().trim()+"-5")))
					.add(new BigDecimal(PropotiesService.getValue(primerProduct.getModiThreeType().trim()+"")))
					.add(new BigDecimal(PropotiesService.getValue(primerProduct.getModiMidType().trim()+"-m")))
					.add(new BigDecimal(PropotiesService.getValue(primerProduct.getModiSpeType().trim()+"-sp")))
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
            return nmolTB.value(primerProduct).multiply(MW.value(primerProduct).divide(new BigDecimal(1000)));
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
