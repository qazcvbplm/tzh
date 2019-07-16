package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.ChargeLog;

import java.math.BigDecimal;
import java.util.List;

public interface ChargeLogMapper extends BaseMapper<ChargeLog> {

    ChargeLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChargeLog record);

    int updateByPrimaryKey(ChargeLog record);

    List<ChargeLog> findByOpenId(String openId);

    ChargeLog tj(int appId);

    BigDecimal surplus(int appId);
}