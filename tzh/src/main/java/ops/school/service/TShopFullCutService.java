package ops.school.service;

import ops.school.api.entity.ShopFullCut;

import java.util.List;

public interface TShopFullCutService {

    List<ShopFullCut> findShopFullCut(Integer shopId);
}
