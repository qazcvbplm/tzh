package ops.school.service.impl;

import ops.school.api.dao.ShopFullCutMapper;
import ops.school.api.entity.ShopFullCut;
import ops.school.service.TShopFullCutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TShopFullCutServiceImpl implements TShopFullCutService {

    @Autowired
    private ShopFullCutMapper shopFullCutMapper;

    @Override
    public List<ShopFullCut> findShopFullCut(Integer shopId) {
        return shopFullCutMapper.findShopFullCut(shopId);
    }
}
