package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.ShopOpenTime;

import java.util.List;

public interface ShopOpenTimeService extends IService<ShopOpenTime> {

    List<ShopOpenTime> findByShopId(Integer shopId);
}
