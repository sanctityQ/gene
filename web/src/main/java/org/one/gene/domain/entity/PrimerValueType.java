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
                    .setScale(1, RoundingMode.HALF_UP);
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
