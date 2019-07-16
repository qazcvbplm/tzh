package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.Mqtt;

import java.util.Map;

public interface MqttMapper extends BaseMapper<Mqtt> {

    void incr(Integer id);

    int tx(Map<String, Object> id);
}