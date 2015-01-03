package org.one.gene.domain.entity;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.one.gene.domain.CalculatePrimerValue;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum PrimerValueType implements CalculatePrimerValue {

    odTotal {

        @Override
        String desc() {
            return "odTotal";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return primerProduct.getOdTotal();
        }

    },

    odTB {
        @Override
        String desc() {
            return "OD/tb";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return primerProduct.getOdTB();
        }

    },

    nmolTotal {
        @Override
        String desc() {
            return "nmolTotal";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return primerProduct.getNmolTotal();
        }
    },

    nmolTB {
        @Override
        String desc() {
            return "nmol/tb";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return primerProduct.getNmolTB();
        }
    },

    /**
     * 碱基数
     */
    baseCount {
        @Override
        String desc() {
            return null;
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(primerProduct.getGeneOrder().trim().length());
        }
    },

    av {
        @Override
        String desc() {
            return "#A";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"A"));
        }

    },

    tv {
        @Override
        String desc() {
            return "#T";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"T"));
        }
    },

    cv {
        @Override
        String desc() {
            return "#C";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"C"));
        }
    },

    gv {
        @Override
        String desc() {
            return "#G";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"G"));
        }
    },

    uv {
        @Override
        String desc() {
            return "#U";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"U"));
        }
    },

    iv {
        @Override
        String desc() {
            return "#I";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"I"));
        }
    },
    nv {
        @Override
        String desc() {
            return "#N";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"N"));
        }
    },
    bv {
        @Override
        String desc() {
            return "#B";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"B"));
        }
    },
    dv {
        @Override
        String desc() {
            return "#D";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"D"));
        }
    },

    hv {
        @Override
        String desc() {
            return "#H";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"H"));
        }
    },


    kv {
        @Override
        String desc() {
            return "#K";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"K"));
        }
    }, mv {
        @Override
        String desc() {
            return "#M";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"M"));
        }
    }, rv {
        @Override
        String desc() {
            return "#R";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"R"));
        }
    }, sv {
        @Override
        String desc() {
            return "#S";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"S"));
        }
    }, wv {
        @Override
        String desc() {
            return "#W";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"W"));
        }
    }, yv {
        @Override
        String desc() {
            return "#Y";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal(StringUtils.countMatches(primerProduct.getGeneOrder(),"Y"));
        }
    }, vv {
        @Override
        String desc() {
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
        String desc() {
            return "分装管数";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return primerProduct.getOdTotal().divide(primerProduct.getOdTB());
        }
    },

    TM {
        @Override
        String desc() {
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
        String desc() {
            return "MW";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return new BigDecimal("313.2").multiply(av.value(primerProduct))
            		.add(new BigDecimal("289.2").multiply(cv.value(primerProduct)))
            		.add(new BigDecimal("329.2").multiply(gv.value(primerProduct)))
            		.add(new BigDecimal("304.2").multiply(tv.value(primerProduct)))
            		.add(new BigDecimal("290.2").multiply(uv.value(primerProduct)))
            		.add(new BigDecimal("314.2").multiply(iv.value(primerProduct)))
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
//					.add(每种修饰的分子量());
					.setScale(1,RoundingMode.HALF_UP);
        }
    },

    GC {
        @Override
        String desc() {
            return "GC";
        }

        /**
         * //GC(%) = 100 * (#C + #G) / 碱基数
         * @param primerProduct
         * @return
         */
        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            BigDecimal strAdd = cv.value(primerProduct).add(cv.value(primerProduct));
            BigDecimal strMul = new BigDecimal(100).multiply(strAdd);
            return strMul.divide(baseCount.value(primerProduct), 4, BigDecimal.ROUND_HALF_UP);
        }
    },

    μgOD {
        @Override
        String desc() {
            return "μgOD";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return nmolTB.value(primerProduct).multiply(MW.value(primerProduct).divide(new BigDecimal(1000)));
        }
    },
    
    ODμmol {
    	@Override
        String desc() {
            return "ODμmol";
        }

        @Override
        BigDecimal value(PrimerProduct primerProduct) {
            return av.value(primerProduct).multiply(new BigDecimal("15.4"))
    				.add(tv.value(primerProduct)).multiply(new BigDecimal("8.8"))
    				.add(cv.value(primerProduct)).multiply(new BigDecimal("7.3"))
    				.add(gv.value(primerProduct)).multiply(new BigDecimal("11.7"))
    				.add(uv.value(primerProduct)).multiply(new BigDecimal("10.1"))
    				.add(iv.value(primerProduct)).multiply(new BigDecimal("12.25"))
    				.add(nv.value(primerProduct)).multiply(new BigDecimal("10.95"))
    				.add(bv.value(primerProduct)).multiply(new BigDecimal("9.5"))
    				.add(dv.value(primerProduct)).multiply(new BigDecimal("12.1"))
    				.add(hv.value(primerProduct)).multiply(new BigDecimal("10.7"))
    				.add(kv.value(primerProduct)).multiply(new BigDecimal("10.6"))
    				.add(mv.value(primerProduct)).multiply(new BigDecimal("11.4"))
    				.add(rv.value(primerProduct)).multiply(new BigDecimal("13.6"))
    				.add(sv.value(primerProduct)).multiply(new BigDecimal("9.6"))
    				.add(vv.value(primerProduct)).multiply(new BigDecimal("11.5"))
    				.add(wv.value(primerProduct)).multiply(new BigDecimal("12.3"))
    				.add(yv.value(primerProduct)).multiply(new BigDecimal("8.35"));
        }
    };

    abstract String desc();

    abstract BigDecimal value(PrimerProduct primerProduct);

    public PrimerProductValue create(PrimerProduct primerProduct) {
        PrimerProductValue primerProductValue = new PrimerProductValue();
        primerProductValue.setPrimerProduct(primerProduct);
        primerProductValue.setCreateTime(DateTime.now().toDate());
        primerProductValue.setType(this);
        primerProductValue.setTypeDesc(this.desc());
        primerProductValue.setValue(this.value(primerProduct));
        return primerProductValue;
    }

}
