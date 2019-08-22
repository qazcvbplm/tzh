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
	`product_id` BIGINT (20) DEFAULT 0 COMMENT '商品id',
	`school_id` BIGINT (20) NOT NULL COMMENT '学校id',
	`create_id` BIGINT (20) NOT NULL COMMENT '创建人id',
	`update_id` BIGINT (20) DEFAULT '0' COMMENT '修改人id',
	`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8 COMMENT = '首页展示推荐店铺商品表';




insert into shop (
        shop.school_id,
        shop.shop_name,
        shop.shop_phone,
        shop.shop_category_id,
        shop.open_flag,
        shop.send_model_flag,
        shop.get_model_flag,
        shop.score,
        shop.start_price,
        shop.box_price,
        shop.send_price,
        shop.send_time,
        shop.top_title,
        shop.shop_login_name,
        shop.shop_login_pass_word,
        shop.shop_address,
        shop.rate,
        shop.send_price_add,
        shop.ts_model_flag,
        shop.full_minus_rate,
        shop.coupon_rate,
        shop.discount_rate,
        shop.shop_tx_flag
)
values
(
		24,
		'小木村石锅拌饭',
		'18357317997',
		'53',
		'1',
		'1',
		'1',
		'5',
		'10.00',
		'1.00',
		'0.00',
		'35',
		'',
		'13136207971',
		'123456',
		'浙江省嘉兴市南湖区东栅街道景宜路格林小镇',
		'0.06',
		'0',
		'0.00',
		'1',
		'0.00',
		'0.00',
		'0.00',
		'1'
		);

