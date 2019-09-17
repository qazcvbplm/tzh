package ops.school.service.impl;

import ops.school.api.entity.SourceOrder;
import ops.school.api.entity.SourceProduct;
import ops.school.api.entity.WxUser;
import ops.school.api.exception.YWException;
import ops.school.api.service.SourceOrderService;
import ops.school.api.service.SourceProductService;
import ops.school.api.service.WxUserBellService;
import ops.school.api.service.WxUserService;
import ops.school.api.util.Util;
import ops.school.service.TSourceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class TSourceOrdersServiceImpl implements TSourceOrderService {

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private SourceOrderService sourceOrderService;
    @Autowired
    private SourceProductService sourceProductService;
    @Autowired
    private WxUserBellService wxUserBellService;

    @Transactional
    @Override
    public String add(Integer id, SourceOrder sourceOrder) {
        WxUser wxUser = wxUserService.findById(sourceOrder.getOpenId());
        SourceProduct sp = sourceProductService.getById(id);
        sourceOrder.setId(Util.GenerateOrderId());
        sourceOrder.setPayPrice(sp.getPrice());
        sourceOrder.setProductImage(sp.getProductImage());
        sourceOrder.setProductName(sp.getProductName());
        Map<String, Object> map = new java.util.HashMap<>();
        map.put("phone", wxUser.getOpenId() + "-" + wxUser.getPhone());
        map.put("source", sp.getPrice());
        if (wxUserBellService.paySource(map) == 1) {
            sourceOrderService.save(sourceOrder);
        } else {
            throw new YWException("积分不足");
        }
        return sourceOrder.getId();
    }
}
