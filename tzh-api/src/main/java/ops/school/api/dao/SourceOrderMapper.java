package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.SourceOrder;

import java.util.List;

public interface SourceOrderMapper extends BaseMapper<SourceOrder> {

    SourceOrder selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SourceOrder record);

    int updateByPrimaryKey(SourceOrder record);

    List<SourceOrder> find(SourceOrder sourceOrder);

    int count(SourceOrder sourceOrder);
}