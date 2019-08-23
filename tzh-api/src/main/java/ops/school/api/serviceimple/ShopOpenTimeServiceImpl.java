package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.constants.NumConstants;
import ops.school.api.dao.ShopOpenTimeMapper;
import ops.school.api.entity.ShopOpenTime;
import ops.school.api.service.ShopOpenTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopOpenTimeServiceImpl extends ServiceImpl<ShopOpenTimeMapper, ShopOpenTime> implements ShopOpenTimeService {

    @Autowired
    private ShopOpenTimeMapper shopOpenTimeMapper;


    @Override
    public List<ShopOpenTime> findByShopId(Integer shopId) {
        QueryWrapper<ShopOpenTime> query = new QueryWrapper<ShopOpenTime>();
        query.lambda().eq(ShopOpenTime::getShopId, shopId);
        return shopOpenTimeMapper.selectList(query);
    }

    @Override
    public List<ShopOpenTime> batchFindByShopIds(List<Long> shopIds) {
        if (shopIds.size() < NumConstants.INT_NUM_1){
            return new ArrayList<>();
        }
        List<ShopOpenTime> shopOpenTimes = shopOpenTimeMapper.batchFindByShopIds(shopIds);
        return shopOpenTimes;
    }
}
