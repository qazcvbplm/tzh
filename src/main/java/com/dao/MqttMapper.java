package com.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entity.Mqtt;

import java.util.Map;

public interface MqttMapper extends BaseMapper<Mqtt>{

	void incr(Integer id);

	int tx(Map<String,Object> id);
}