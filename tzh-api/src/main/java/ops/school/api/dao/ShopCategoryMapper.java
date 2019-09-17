package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryMapper extends BaseMapper<ShopCategory> {

    ShopCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ShopCategory record);

    int updateByPrimaryKey(ShopCategory record);

    List<ShopCategory> find(ShopCategory shopCategory);
}