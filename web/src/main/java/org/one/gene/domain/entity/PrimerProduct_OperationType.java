package org.one.gene.domain.entity;

import java.math.BigDecimal;

import org.joda.time.DateTime;


public enum PrimerProduct_OperationType {

	 listImport {
	        @Override
	        String desc() {
	            return "订单导入";
	        }
	    },
	 makeTable {
	        @Override
	        String desc() {
	            return "待制表";
	        }
	    },
	 synthesis {
	        @Override
	        String desc() {
	            return "待合成";
	        }
	    },
	 modi {
	        @Override
	        String desc() {
	            return "待修饰";
	        }
	    },
	 ammonia {
	        @Override
	        String desc() {
	            return "待氨解";
	        }
	    },
	 purify {
	        @Override
	        String desc() {
	            return "待纯化";
	        }
	    },
	 measure {
	        @Override
	        String desc() {
	            return "待测值";
	        }
	    },
	 pack {
	        @Override
	        String desc() {
	            return "待分装";
	        }
	    },
	 bake {
	        @Override
	        String desc() {
	            return "待烘干";
	        }
	    },
	 detect {
	        @Override
	        String desc() {
	            return "待检测";
	        }
	    },
	 delivery {
	        @Override
	        String desc() {
	            return "待发货";
	        }
	    },
	 finish {
	        @Override
	        String desc() {
	            return "结束";
	        }
	    };

	 abstract String desc();
	 
	    
}
