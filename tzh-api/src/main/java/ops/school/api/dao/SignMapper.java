package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.Sign;

public interface SignMapper extends BaseMapper<Sign> {

    Sign findLast(String openId);
}