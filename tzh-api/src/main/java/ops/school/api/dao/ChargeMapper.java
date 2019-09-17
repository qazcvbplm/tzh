package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.Charge;

import java.util.List;

public interface ChargeMapper extends BaseMapper<Charge> {

    Charge selectByPrimaryKey(Integer id);

    List<Charge> find();

    int remove(int id);

}