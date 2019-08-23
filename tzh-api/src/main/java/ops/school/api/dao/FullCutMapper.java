package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.FullCut;

import java.util.List;

public interface FullCutMapper extends BaseMapper<FullCut> {

    int deleteOne(int id);

    List<FullCut> findByShop(int shopId);

    /**
     * @date:   2019/8/23 11:43
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.FullCut>
     * @param   shopIds
     * @Desc:   desc
     */
    List<FullCut> batchFindByShopIds(List<Long> shopIds);
}