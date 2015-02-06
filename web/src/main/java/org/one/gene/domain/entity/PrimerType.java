package org.one.gene.domain.entity;

/**
 * 生产数据类型
 */
public class PrimerType {

    public interface TypeDesc{
        String desc();
    }

    public static enum PrimerStatusType implements TypeDesc{
        /**
         * 订单初始
         */
        orderInit {
            @Override
            public String desc() {
                return "订单初始导入";
            }
        },
        /**
         * 订单检查
         */
        orderCheck {
            @Override
            public String desc() {
                return "订单检查";
            }
        },
        /**
         * 制作合成板
         */
        makeBoard {
            @Override
            public String desc() {
                return "制板";
            }
        },
        /**
         * 合成
         */
        synthesis {
            @Override
            public String desc() {
                return "合成";
            }
        },
        /**
         * 修饰
         */
        modification {
            @Override
            public String desc() {
                return "修饰";
            }
        },
        /**
         * 氨解
         */
        ammonia {
            @Override
            public String desc() {
                return "氨解";
            }
        },

        purify {
            @Override
            public String desc() {
                return "纯化";
            }
        },
        measure {
            @Override
            public String desc() {
                return "测值";
            }
        },
        pack {
            @Override
            public String desc() {
                return "分装";
            }
        },
        bake {
            @Override
            public String desc() {
                return "烘干";
            }
        },
        detect {
            @Override
            public String desc() {
                return "检测";
            }
        },
        delivery {
            @Override
            public String desc() {
                return "发货";
            }
        },
        finish {
            @Override
            public String desc() {
                return "完成";
            }
        };

    }

    public static enum PrimerOperationType implements TypeDesc{


        orderInit {
            @Override
            public String desc() {
                return "订单初始化";
            }
        },
        orderCheckSuccess {
            @Override
            public String desc() {
                return "订单检查成功";
            }
        },
        orderCheckFailure {
            @Override
            public String desc() {
                return "订单检查失败";
            }
        },
        makeBoard {
            @Override
            public String desc() {
                return "制板";
            }
        },
        synthesisSuccess {
            @Override
            public String desc() {
                return "合成成功";
            }
        },
        synthesisFailure {
            @Override
            public String desc() {
                return "合成失败";
            }
        },
        modiSuccess {
            @Override
            public String desc() {
                return "修饰成功";
            }
        },
        modiFailure {
            @Override
            public String desc() {
                return "修饰失败";
            }
        },
        ammoniaSuccess {
            @Override
            public String desc() {
                return "氨解成功";
            }
        },
        ammoniaFailure {
            @Override
            public String desc() {
                return "氨解失败";
            }
        },
        purifySuccess {
            @Override
            public String desc() {
                return "纯化成功";
            }
        },
        purifyFailure {
            @Override
            public String desc() {
                return "纯化失败";
            }
        },
        measureSuccess {
            @Override
            public String desc() {
                return "测值成功";
            }
        },
        measureFailure {
            @Override
            public String desc() {
                return "测值失败";
            }
        },
        packSuccess {
            @Override
            public String desc() {
                return PrimerStatusType.pack.desc()+"成功";
            }
        },
        packFailure {
            @Override
            public String desc() {
                return PrimerStatusType.pack.desc()+"失败";
            }
        },
        bakeSuccess {
            @Override
            public String desc() {
                return PrimerStatusType.bake.desc()+"成功";
            }
        },
        bakeFailure {
            @Override
            public String desc() {
                return PrimerStatusType.bake.desc()+"失败";
            }
        },
        detectSuccess {
            @Override
            public String desc() {
                return PrimerStatusType.detect.desc()+"成功";
            }
        },
        detectFailure {
            @Override
            public String desc() {
                return PrimerStatusType.detect.desc()+"失败";
            }
        },

        deliverySuccess {
            @Override
            public String desc() {
                return PrimerStatusType.delivery.desc()+"成功";
            }
        },
        deliveryFailure {
            @Override
            public String desc() {
                return PrimerStatusType.delivery.desc()+"失败";
            }
        },
        backSuccess {
            @Override
            public String desc() {
                return "发货召回成功";
            }
        },
        backFailure {
            @Override
            public String desc() {
            	return "发货召回失败";
            }
        }

    }
}
