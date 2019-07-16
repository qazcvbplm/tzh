package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.SourceProduct;

import java.util.List;

public interface SourceProductMapper extends BaseMapper<SourceProduct> {

    SourceProduct selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SourceProduct record);

    int updateByPrimaryKey(SourceProduct record);

    List<SourceProduct> find(SourceProduct sourceProduct);
}