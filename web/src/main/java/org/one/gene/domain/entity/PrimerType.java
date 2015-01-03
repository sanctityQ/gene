package org.one.gene.domain.entity;

/**
 * 生产数据类型
 */
public class PrimerType {

    interface TypeDesc{
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
        };

    }
}
