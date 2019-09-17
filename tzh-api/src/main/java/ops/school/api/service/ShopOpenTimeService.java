package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.ShopOpenTime;

import java.util.List;

public interface ShopOpenTimeService extends IService<ShopOpenTime> {

    List<ShopOpenTime> findByShopId(Integer shopId);

    /**
     * @date:   2019/8/23 11:37
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.ShopOpenTime>
     * @param   shopIds
     * @Desc:   desc
     */
    List<ShopOpenTime> batchFindByShopIds(List<Long> shopIds);
}
