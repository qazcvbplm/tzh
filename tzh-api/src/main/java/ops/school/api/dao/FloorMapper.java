package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.Floor;

import java.util.List;

public interface FloorMapper extends BaseMapper<Floor> {

    Floor selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Floor record);

    int updateByPrimaryKey(Floor record);

    List<Floor> find(Floor floor);
}