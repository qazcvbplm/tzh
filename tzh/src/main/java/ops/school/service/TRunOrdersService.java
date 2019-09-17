package ops.school.service;

import ops.school.api.entity.RunOrders;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public interface TRunOrdersService {
    @Transactional
    int cancel(String id);

    int pay(RunOrders orders,String formid);

    BigDecimal countTotalPriceByFloor(Integer floorId, String beginTime, String endTime);
}
