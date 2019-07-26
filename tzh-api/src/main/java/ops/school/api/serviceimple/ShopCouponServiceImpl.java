package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.ShopCouponMapper;
import ops.school.api.entity.ShopCoupon;
import ops.school.api.exception.Assertions;
import ops.school.api.service.ShopCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopCouponServiceImpl extends ServiceImpl<ShopCouponMapper, ShopCoupon> implements ShopCouponService {

    @Autowired
    private ShopCouponMapper shopCouponMapper;
    /**
     * @date:   2019/7/26 17:58
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.ShopCoupon>
     * @param   couponIdS
     * @Desc:   desc 根据couponIds查询
     */
    @Override
    public List<ShopCoupon> batchFindSCByCouponIdS(List<Long> couponIdS) {
        Assertions.notEmpty(couponIdS);
        List<ShopCoupon> shopCouponList = shopCouponMapper.batchFindSCByCouponIdS(couponIdS);
        return shopCouponList;
    }
}
