package ops.school.service;

import ops.school.api.entity.RunOrders;
import org.springframework.transaction.annotation.Transactional;

public interface TRunOrdersService {
    @Transactional
    int cancel(String id);

    int pay(RunOrders orders);
}
