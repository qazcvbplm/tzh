package ops.school.service;

import ops.school.api.entity.SourceOrder;
import org.springframework.transaction.annotation.Transactional;


public interface TSourceOrderService {
    @Transactional
    String add(Integer id, SourceOrder sourceOrder);
}
