package ops.school.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ops.school.api.dao.WxUserCouponMapper;
import ops.school.api.entity.WxUserCoupon;
import ops.school.api.enums.ResponseViewEnums;
import ops.school.api.exception.Assertions;
import ops.school.service.TWxUserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TWxUserCouponServiceImpl implements TWxUserCouponService {

    @Autowired
    private WxUserCouponMapper wxUserCouponMapper;

    @Override
    public List<WxUserCoupon> findInvalidUserCoupon() {
        return wxUserCouponMapper.findInvalidUserCoupon();
    }

    @Override
    public int updateIsInvalid(WxUserCoupon wxUserCoupon) {
        if (wxUserCoupon == null || wxUserCoupon.getId() == null || wxUserCoupon.getIsInvalid() == null){
            return -1;
        }
        int rs = wxUserCouponMapper.update(wxUserCoupon);
        return rs;
    }

    @Override
    public List<WxUserCoupon> findUserCoupon(Long wxUserId, Long shopId) {
        Map<String,Object> map = new HashMap<>();
        map.put("wxUserId",wxUserId);
        map.put("shopId",shopId);
        return wxUserCouponMapper.findUserCoupon(map);
    }

    @Override
    public List<Map<String,Object>> findAllUserCoupons(Long wxUserId) {
        return wxUserCouponMapper.findAllUserCoupons(wxUserId);
    }

    /**
     * @date:   2019/7/25 15:30
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.WxUserCoupon>
     * @param   userId
     * @param   wxUserCoupon
     * @Desc:   desc 分页查询用户所有的优惠券（isInvalid不传查所有，传0查有效）
     */
    @Override
    public IPage<WxUserCoupon> pageFindALLCouponsByUserId(Long userId, WxUserCoupon wxUserCoupon) {
        Assertions.notNull(userId, ResponseViewEnums.WX_USER_NEED_USER_ID);
        QueryWrapper<WxUserCoupon> wrapper = new QueryWrapper<>();
        wrapper.eq("wx_user_id",userId);
        if (wxUserCoupon.getIsInvalid() != null){
            wrapper.eq("is_invalid",wxUserCoupon.getIsInvalid());
        }
        Integer countNum = wxUserCouponMapper.selectCount(wrapper);
        List<WxUserCoupon> userCoupons =  wxUserCouponMapper.pageFindALLCouponsByUserId(userId,wxUserCoupon);
        IPage<WxUserCoupon> result = new Page<>();
        result.setRecords(userCoupons);
        result.setTotal(Long.valueOf(countNum));
        return result;
    }
}
