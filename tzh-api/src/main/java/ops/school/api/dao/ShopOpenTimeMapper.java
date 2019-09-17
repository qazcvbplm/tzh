package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.ShopOpenTime;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShopOpenTimeMapper extends BaseMapper<ShopOpenTime> {

    /**
     * @date:   2019/8/23 11:38
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.ShopOpenTime>
     * @param
     * @Desc:   desc
     */
    List<ShopOpenTime> batchFindByShopIds(@Param("list") List<Long> shopIds);
}