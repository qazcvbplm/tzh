package ops.school.service;

import ops.school.api.dto.ShopTj;
import ops.school.api.entity.Orders;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

public interface TOrdersService {
    @Transactional
    void addTakeout(Integer[] productIds, Integer[] attributeIndex, Integer[] counts, @Valid Orders orders);

    @Transactional
    int pay(Orders orders);

    @Transactional
    int paySuccess(String orderId, String payment);

    @Transactional
    int cancel(String id);

    @Transactional
    int shopAcceptOrderById(String orderId);

    ShopTj shopstatistics(Integer shopId, String beginTime, String endTime);

}
