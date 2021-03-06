CREATE DATABASE IF NOT EXISTS gene DEFAULT CHARSET utf8 COLLATE utf8_general_ci;


GRANT ALL PRIVILEGES ON gene.* TO 'geneuser'@'%' IDENTIFIED BY 'XTY00pVZKmYTSb' WITH GRANT OPTION;
FLUSH PRIVILEGES;

CREATE TABLE `company` (
  `id`             INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `com_code`       VARCHAR(15)      NOT NULL COMMENT '机构代码',
  `com_name`       VARCHAR(127)     NOT NULL COMMENT '机构名称',
  `upper_com_code` VARCHAR(15)      NOT NULL COMMENT '上机机构代码',
  `com_type`       VARCHAR(15)      NOT NULL COMMENT '机构类型',
  `com_level`      TINYINT(4)       NOT NULL COMMENT '机构级别',
  `phone_number`   VARCHAR(35) COMMENT '电话号码',
  `fax_number`     VARCHAR(15) COMMENT '传真号码',
  `post_code`      CHAR(6) COMMENT '邮编',
  `address`        VARCHAR(255) COMMENT '地址',
  `desc`          VARCHAR(511) COMMENT '描述',
  `validate`       TINYINT(1)  NOT NULL COMMENT '是否有效,0-无效，1-有效',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `modify_time` DATETIME COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_com_code` (`com_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='机构表';

CREATE TABLE `customer` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `code`    VARCHAR(15) NOT NULL COMMENT '客户代码',
  `name` VARCHAR(127) NOT NULL COMMENT '客户名称',
  `customer_flag`    TINYINT(1) NOT NULL DEFAULT 1 COMMENT '客户标识，0-梓熙，1-代理公司，2-直接客户',
  `com_code`    VARCHAR(15) NOT NULL DEFAULT '' COMMENT '机构代码',
  `leader_name` VARCHAR(127) COMMENT '负责人姓名',
  `invoice_title` VARCHAR(127) COMMENT '发票抬头',
  `pay_ways` VARCHAR(15) COMMENT '结账方式',
  `address` VARCHAR(255) COMMENT '客户地址',
  `phone_no` VARCHAR(15) COMMENT '联系电话',
  `fax` VARCHAR(15) COMMENT '传真',
  `email` VARCHAR(63) COMMENT '邮箱',
  `web_site` varchar(127)  COMMENT '网址',
  `office` varchar(255)  COMMENT '办事处',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `modify_time` DATETIME NOT NULL COMMENT '最后修改时间',
  `prefix` varchar(2)  COMMENT '生产编号开头标识',
  `handler_code` varchar(30) NOT NULL COMMENT  '业务员代码',
  `handler_name` varchar(255) NOT NULL COMMENT '业务员姓名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_customer_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户信息表';

CREATE TABLE `user` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `code`    VARCHAR(15) NOT NULL COMMENT '用户代码',
  `name`    VARCHAR(31) NOT NULL DEFAULT '' COMMENT '用户名称',
  `password`    VARCHAR(63) NOT NULL COMMENT '用户密码',
  `com_code`    VARCHAR(15) NOT NULL DEFAULT '' COMMENT '机构代码',
  `mobile`      CHAR(11) NOT NULL DEFAULT '' COMMENT '手机号',
  `email`       VARCHAR(63) NOT NULL DEFAULT '' COMMENT '邮箱',
  `user_flag` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '用户标志,0-系统管理员，1-管理层，2-使用者',
  `validate` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否有效,0-不是，1-是',
  `customer_id`      INT(11) COMMENT '客户信息数据ID',
  `salt`        VARCHAR(63) NOT NULL DEFAULT '' COMMENT '加密salt',
  `menu_id`     VARCHAR(1000) COMMENT '菜单id',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `modify_time` DATETIME NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';


CREATE TABLE `order` (
  `id`                INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `order_no`          CHAR(12)     NOT NULL COMMENT '订单号',
  `out_order_no`      VARCHAR(63) NOT NULL COMMENT '外部订单号',
  `customer_code`     VARCHAR(31)  NOT NULL COMMENT '客户代码',
  `customer_name`     VARCHAR(127) COMMENT '客户姓名',
  `contacts_name`     VARCHAR(127) COMMENT '客户联系人姓名',
  `com_code`          VARCHAR(15) COMMENT '归属机构代码',
  `status`            TINYINT(2)  COMMENT '订单状态',
  `type`              CHAR(2) NOT NULL DEFAULT 0 COMMENT '订单类型: 00-合成',
  `file_name`         VARCHAR(127) COMMENT '上传文件名称',
  `create_time`       DATETIME NOT NULL COMMENT '创建时间',
  `modify_time`       DATETIME NOT NULL COMMENT '修改时间',
  `order_up_type`      varchar(10)  NULL COMMENT '订单导入类型',
  `validate` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否有效,0-不是，1-是',
  `product_no_min_to_max` varchar(50) DEFAULT NULL COMMENT '生产编号最小最大号',
  `tbn_total`         decimal(10,0) DEFAULT NULL COMMENT '碱基总数',
  `handler_code`      varchar(30) NOT NULL COMMENT  '业务员代码',
  `handler_name`      varchar(255) NOT NULL COMMENT  '业务员名称',
  `delivery_remark`   varchar(2000) COMMENT '发货备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';


CREATE TABLE `primer_product` (
  `id`                  BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `product_no`          CHAR(16) NOT NULL COMMENT '生产编号',
  `order_no`            CHAR(12)  NOT NULL COMMENT '订单号',
  `out_product_no`      VARCHAR(63) COMMENT '外部生产编号',
  `from_product_no`   CHAR(16) COMMENT '来源编号ID',
  `prime_name`          VARCHAR(255) NOT NULL COMMENT '引物名称',
  `gene_order`          VARCHAR(255) NOT NULL COMMENT '引物序列',
  `purify_type`      VARCHAR(7) NOT NULL COMMENT '纯化方式',
  `modi_five_type`      VARCHAR(63) COMMENT '5修饰',
  `modi_three_type`     VARCHAR(63) COMMENT '3修饰',
  `modi_mid_type`    VARCHAR(63) COMMENT '中间修饰',
  `modi_spe_type`    VARCHAR(63) COMMENT '特殊单体',
  `modi_price`   DECIMAL(10,2) COMMENT '修饰价格',
  `base_val`     DECIMAL(10,2) COMMENT '碱基单价',
  `purify_val`     DECIMAL(10,2) COMMENT '纯化价格',
  `total_val`    DECIMAL(10,2) COMMENT '总价格:修饰单价+碱基单价*碱基数+纯化价格',
  `remark`              VARCHAR(255) COMMENT '描述',
  `operation_type`     VARCHAR(31) NOT NULL COMMENT '状态',
  `board_no`            VARCHAR(127) COMMENT '板号',
  `com_code`            VARCHAR(20) NOT NULL COMMENT '归属机构代码',
  `back_times`          TINYINT(2) DEFAULT '0' COMMENT '循环重回次数',
  `review_file_name`    VARCHAR(127) COMMENT '检测',
  `modify_time` DATETIME NOT NULL COMMENT '最后修改时间',
  `measure_volume`       INT(11) COMMENT '测值体积',
  `gene_order_midi` varchar(255) DEFAULT NULL COMMENT '带修饰的序列',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_no` (`product_no`),
  KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='引物生产数据表';

CREATE TABLE `board` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `board_no`            VARCHAR(127) COMMENT '板号',
  `board_type`        TINYINT(1) NOT NULL DEFAULT 1 COMMENT '板类型:0-横排 1-竖排',
  `type`              VARCHAR(31) COMMENT '类型',
  `create_time`       DATETIME NOT NULL COMMENT '创建时间',
  `create_user`       INT(11) NOT NULL COMMENT '创建user',
  `operation_type`     VARCHAR(31) NOT NULL COMMENT '状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_board_no` (`board_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合成板表';

CREATE TABLE `board_hole` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `board_no`          VARCHAR(127) COMMENT '板号',
  `hole_no`           VARCHAR(7) COMMENT '孔号',
  `product_id`        BIGINT(20) COMMENT '生产数据ID',
  `create_time`       DATETIME NOT NULL COMMENT '创建时间',
  `create_user`       INT(11) NOT NULL COMMENT '创建user',
  `modify_time`       DATETIME  COMMENT '修改时间',
  `modify_user`       INT(11)  COMMENT '修改user',
  `ppo_id` INT(11) COMMENT '引物操作ID',
  `sorting` TINYINT(2) DEFAULT '0' COMMENT '排序',
  `status`            TINYINT(2)  COMMENT '状态:0正常，1删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_board_no_hole_no` (`board_no`,`hole_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='合成板表';


CREATE TABLE `primer_product_operation` (
  `id`          BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `primer_product_id` INT(11) NOT NULL COMMENT '引物生产数据ID',
  `type`   VARCHAR(31) NOT NULL DEFAULT '' COMMENT '类型',
  `type_desc`   VARCHAR(63) NOT NULL DEFAULT '' COMMENT '类型描述',
  `back_times`  TINYINT(2) DEFAULT '0' COMMENT '工艺循环次数',
  `user_code`   VARCHAR(15) NOT NULL COMMENT '用户代码',
  `user_name`   VARCHAR(31) NOT NULL COMMENT '用户名称',
  `create_time`   DATETIME NOT NULL COMMENT '操作时间',
  `fail_reason` VARCHAR(511) DEFAULT '' comment '失败原因',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ppid_tc_bt` (`primer_product_id`,`type`,`back_times`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='引物生产数据操作记录表';


CREATE TABLE `primer_product_value` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `primer_product_id` INT(11) NOT NULL COMMENT '引物生产数据ID',
  `type`  VARCHAR(31) NOT NULL COMMENT '值类型',
  `type_desc` VARCHAR(63) NOT NULL COMMENT '类型描述',
  `value`  DECIMAL(20,2) NOT NULL COMMENT '数值',
  `create_time`  DATETIME NOT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_ppid_type` (`primer_product_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='引物数据数值表';

CREATE TABLE `primer_label_config` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `customer_code` VARCHAR(31)      NOT NULL COMMENT '客户代码',
  `customer_name` VARCHAR(127)     NOT NULL COMMENT '客户名称',
  `columns` TINYINT(2) DEFAULT 1 COMMENT '标签排列列数',
  `user_code` VARCHAR(15) NOT NULL COMMENT '用户代码',
  `user_name` VARCHAR(31) NOT NULL COMMENT '用户名称',
  `create_time` DATETIME NOT NULL COMMENT '创建时间',
  `modify_time` DATETIME NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_plc_customer_code` (`customer_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='引物标签打印配置主表';

CREATE TABLE `primer_label_config_sub` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `primer_label_config_id` INT(11) NOT NULL COMMENT '引物标签打印配置主表ID',
  `type` VARCHAR(31) NOT NULL COMMENT '值类型',
  `type_desc` VARCHAR(63) NOT NULL COMMENT '类型描述',
  `sorting` TINYINT(2) DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_plc_id_type` (`primer_label_config_id`,`type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='引物标签打印配置子表';


CREATE TABLE `customer_price` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `customer_id` INT(11) NOT NULL COMMENT '客户公司id',
  `min_tbn`     DECIMAL(10,2) COMMENT '最小碱基数',
  `max_tbn`     DECIMAL(10,2) COMMENT '最大碱基数',
  `min_od`      DECIMAL(10,2) COMMENT '最小OD总数 ',
  `max_od`      DECIMAL(10,2) COMMENT '最大OD总数 ',
  `purify_type` VARCHAR(7) NOT NULL COMMENT '纯化方式',
  `base_val`     DECIMAL(10,2) COMMENT '碱基单价',
  `purify_val`     DECIMAL(10,2) COMMENT '纯化价格',
  `modi_price`   DECIMAL(10,2) COMMENT '修饰价格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户引物单价配置表';

CREATE TABLE `customer_contacts` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `customer_id` INT(11) NOT NULL COMMENT '客户公司id',
  `name` VARCHAR(30) NOT NULL COMMENT '联系人姓名',
  `phone_no` VARCHAR(15) COMMENT '联系电话',
  `email` VARCHAR(60) COMMENT '邮箱',
  `fax` VARCHAR(15) COMMENT '传真',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_customer_contacts` (`customer_code`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户联系人信息表';

CREATE TABLE `product_molecular` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `product_categories` VARCHAR(31)      NOT NULL COMMENT '产品大类代码',
  `product_code`   VARCHAR(31) COMMENT '产品小类代码',
  `modified_molecular`     DECIMAL(10,2) COMMENT '修饰分子量',
  `validate`     VARCHAR(7) COMMENT '有效标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='修饰分子量配置表';

CREATE TABLE `modified_price` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `product_categories` VARCHAR(31)      NOT NULL COMMENT '修饰类别',
  `modi_type` VARCHAR(31)      NOT NULL COMMENT '修饰组合类型',
  `modi_price`   DECIMAL(10,2) COMMENT '修饰价格',
  `validate`     VARCHAR(7) COMMENT '有效标志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='修饰价格表';

CREATE TABLE `menu` (
  `id` INT(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `first_id`   VARCHAR(31) NOT NULL COMMENT '一级菜单id',
  `first_name` VARCHAR(31) NOT NULL COMMENT '一级菜单名称',
  `second_id`  VARCHAR(31) NOT NULL COMMENT '二级菜单id',
  `second_name` VARCHAR(31) NOT NULL COMMENT '二级菜单名称',
  `url`        VARCHAR(100) COMMENT '二级菜单url',
  `order_no`   INT(11) NOT NULL COMMENT '位置序号',
  `remark`     VARCHAR(100) COMMENT '备注',
  `valid`   VARCHAR(7) NOT NULL COMMENT '有效标志',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_menu_levelid` (`first_id`,`second_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单基础表';

CREATE TABLE `product_sequence` (
  `id`                  BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '唯一标识id',
  `modify_time` DATETIME NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='引物生产编号序列表';
