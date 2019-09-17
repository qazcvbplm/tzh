package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.Evaluate;

import java.util.List;
import java.util.Map;

public interface EvaluateMapper extends BaseMapper<Evaluate> {

    Evaluate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Evaluate record);

    int updateByPrimaryKey(Evaluate record);

    List<Evaluate> find(Evaluate evaluate);

    int count(Evaluate evaluate);

    List<Evaluate> findByShopId(Map<String, Object> map);
}