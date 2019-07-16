package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.Shop;

import java.util.List;

public interface ShopMapper extends BaseMapper<Shop> {

    Shop selectByPrimaryKey(Integer id);

    Shop checkByLoginName(String shopLoginName);

    List<Shop> find(Shop shop);

    int count(Shop shop);
}