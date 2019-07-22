package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.PageQueryDTO;
import ops.school.api.entity.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ShopMapper extends BaseMapper<Shop> {

    Shop selectByPrimaryKey(Integer id);

    Shop checkByLoginName(String shopLoginName);

    List<Shop> find(Shop shop);

    int count(Shop shop);

    int shoptx(Map<String,Object> map);

    /**
     * @date:   2019/7/22 15:23
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.Shop>
     * @param   shop
     * @param   pageQueryDTO
     * @Desc:   desc 分页查询店铺，查询满减，根据开店时间倒叙排（关闭的店铺在最后面）
     */
    List<Shop> findShopWithFullCutOBTime(@Param("shop") Shop shop, @Param("pageQueryDTO") PageQueryDTO pageQueryDTO);
}