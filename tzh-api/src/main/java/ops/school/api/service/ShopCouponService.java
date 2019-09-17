package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.ShopCoupon;

import java.util.List;

public interface ShopCouponService extends IService<ShopCoupon> {

    /**
     * @date:   2019/7/26 17:57
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.ShopCoupon>
     * @param   couponIdS
     * @Desc:   desc 根据couponIds查询
     */
    List<ShopCoupon> batchFindSCByCouponIdS(List<Long> couponIdS);
}
