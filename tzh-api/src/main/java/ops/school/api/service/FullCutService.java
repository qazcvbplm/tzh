package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.FullCut;

import java.util.List;

public interface FullCutService extends IService<FullCut> {

    List<FullCut> findByShopId(Integer shopId);

    /**
     * @date:   2019/8/23 11:42
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.FullCut>
     * @param   shopIds
     * @Desc:   desc
     */
    List<FullCut> batchFindByShopIds(List<Long> shopIds);
}
