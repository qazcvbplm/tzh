package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.SecondHand;

import java.util.List;

public interface SecondHandMapper extends BaseMapper<SecondHand> {

    SecondHand selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SecondHand record);

    int updateByPrimaryKeyWithBLOBs(SecondHand record);

    int updateByPrimaryKey(SecondHand record);

    List<SecondHand> find(SecondHand secondHand);

    int count(SecondHand secondHand);
}