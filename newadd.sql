CREATE TABLE `shop_print` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `shop_id` bigint(20) NOT NULL COMMENT '店铺id',
  `yes_print_gpr` int(4) NOT NULL COMMENT '店铺是否开启飞鹅GPRS打印 0-不开启，1-开启',
  `print_brand` int(4) NOT NULL COMMENT '打印机品牌 1-飞鹅，2-其他',
  `fei_e_sn` VARCHAR(64) NOT NULL COMMENT '店铺打印机的sn码',
  `fei_e_key` VARCHAR(64) NOT NULL COMMENT '店铺打印机的key码',

  `create_id` bigint(20) NOT NULL COMMENT '创建人id',
  `update_id` bigint(20) DEFAULT '0' COMMENT '修改人id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `normal_index_shop_id` (`shop_id`) USING BTREE COMMENT '店铺id索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='店铺打印机表';


CREATE TABLE `index_shop_product` (
	`id` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`shop_id` BIGINT (20) DEFAULT 0 COMMENT '店铺id',
	`shop_weight` INT (4) DEFAULT 100 COMMENT '首页店铺排序权重，1排第一位',
	`product_id` BIGINT (20) DEFAULT 0 COMMENT '商品id',
	`product_weight` INT (4) DEFAULT 100 COMMENT '首页店铺排序权重，1排第一位',
	`school_id` BIGINT (20) NOT NULL COMMENT '学校id',
	`create_id` BIGINT (20) NOT NULL COMMENT '创建人id',
	`update_id` BIGINT (20) DEFAULT '0' COMMENT '修改人id',
	`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT = '首页展示推荐店铺商品表';


alter table day_log_takeout add `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间';

alter table day_log_takeout add `school_get_total` decimal(10,2) DEFAULT '0.00' COMMENT '当日统计学校所得';
alter table day_log_takeout add `school_day_tx` decimal(10,2) DEFAULT '0.00' COMMENT '学校当日提现';
alter table day_log_takeout add `school_all_money` decimal(10,2) DEFAULT '0.00' COMMENT '学校截至到统计时间可提现';
alter table day_log_takeout add `shop_day_tx` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '每家店铺当日提现/学校店铺当日总提现';

alter table school add `message_phone` varchar(20) DEFAULT '0' COMMENT '学校负责人短信接收手机号';

ALTER TABLE `day_log_takeout`
ADD COLUMN `down_send_count`  int(4) NOT NULL DEFAULT 0 COMMENT '楼下完成配送员总单数' AFTER `school_all_money`,
ADD COLUMN `down_send_money`  decimal(10,2) NOT NULL DEFAULT 0 COMMENT '配送员楼下完成订单配送费' AFTER `down_send_count`;


CREATE TABLE `club_card_send` (
	`id` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`school_id` BIGINT(20) NOT NULL COMMENT '学校id',
	`name` varchar(32) NOT NULL COMMENT '卡名',
    `description` varchar(64) DEFAULT "" COMMENT '卡描述',
    `day_time` int(5) NOT NULL DEFAULT '0' COMMENT '卡购买后每天使用次数',
    `day_money` decimal(10,2) NOT NULL DEFAULT '0' COMMENT '卡购买后每天最大的使用金额',
    `price_sale` decimal(10,2) NOT NULL DEFAULT '0' COMMENT '卡购买价格，销售价',
    `price_original` decimal(10,2) NOT NULL DEFAULT '0' COMMENT '卡购买原价，展示用',
    `send_begin_time` datetime NOT NULL COMMENT '卡发放开始时间',
    `send_end_time` datetime NOT NULL COMMENT '卡发放结束时间',
    `effective_days` int(5) NOT NULL DEFAULT '0' COMMENT '卡购买后的有效天数',
    `type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '卡类型，1-配送费卡',
    `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '卡的状态，0-冻结不可用，1-可用',

    `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除 0：未删除 1：已删除',
	`create_id` BIGINT(20) NOT NULL COMMENT '创建人id',
	`update_id` BIGINT(20) DEFAULT '0' COMMENT '修改人id',
	`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT = '配送会员卡';

CREATE TABLE `card_user` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`school_id` BIGINT(20) NOT NULL COMMENT '学校id',
	`user_id` BIGINT(20) NOT NULL COMMENT '用户id',
	`card_id` BIGINT(20) NOT NULL  COMMENT '卡id',
	`card_day_time` int(5) NOT NULL DEFAULT '0' COMMENT '卡购买后每天使用次数',
    `card_day_money` decimal(10,2) NOT NULL DEFAULT '0' COMMENT '卡购买后每天最大的使用金额',
    `card_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '卡类型，1-配送费卡',
    `card_failure_time` datetime NOT NULL COMMENT '失效时间',
    `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否删除 0：未删除 1：已删除',
	`create_id` BIGINT(20) NOT NULL COMMENT '创建人id',
	`update_id` BIGINT(20) DEFAULT '0' COMMENT '修改人id',
	`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT = '用户拥有配送卡表';


CREATE TABLE `card_pay_log` (
	`id` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`school_id` BIGINT(20) NOT NULL COMMENT '学校id',
	`card_user_id` BIGINT(20) NOT NULL COMMENT '用户配送卡主id',
	`user_id` BIGINT(20) NOT NULL COMMENT '用户id',
	`card_id` BIGINT(20) NOT NULL COMMENT '卡id',
	`card_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '卡类型，1-配送费卡',
	`order_id` varchar(50) NOT NULL COMMENT '使用的外卖订单id',
    `use_money` decimal(10,2) NOT NULL DEFAULT '0' COMMENT '订单中使用的金额',
    `create_id` BIGINT(20) NOT NULL COMMENT '创建人id',
	`update_id` BIGINT(20) DEFAULT '0' COMMENT '修改人id',
	`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT = '配送卡消费日志表';

CREATE TABLE `card_buy_log` (
	`id` BIGINT (20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
	`school_id` BIGINT(20) NOT NULL COMMENT '学校id',
	`user_id` BIGINT(20) NOT NULL COMMENT '用户id',
	`card_id` BIGINT(20) NOT NULL COMMENT '卡id',
	`money` decimal(10,2) NOT NULL DEFAULT '0' COMMENT '购买金额',
	`wx_trade_no` varchar(50) NOT NULL DEFAULT '0' COMMENT '微信充值流水号',

    `create_id` BIGINT(20) NOT NULL COMMENT '创建人id',
	`update_id` BIGINT(20) DEFAULT '0' COMMENT '修改人id',
	`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT = '配送卡购买日志表';


ALTER TABLE `orders`
ADD COLUMN `card_send_user_id`  bigint(20) NULL DEFAULT 0 COMMENT '用户购买的配送会员卡id' AFTER `after_discount_price`,
ADD COLUMN `card_send_money`  decimal(10,2) NULL DEFAULT 0 COMMENT '用户此单减免配送费' AFTER `card_send_user_id`;

ALTER TABLE `orders_complete`
ADD COLUMN `send_card_money` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '每笔订单配送卡' ;


