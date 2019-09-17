package ops.school.service;

import ops.school.api.dto.SenderTj;
import ops.school.api.entity.Orders;
import ops.school.api.entity.RunOrders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TSenderService {

    List<Orders> findorderbydjs(Integer senderId, Integer page, Integer size, String status);

    Integer acceptOrder(Integer senderId, String orderId);

    Integer sendergetorder(String orderId);

    void end(String orderId, boolean end);


    void tsTakeOutOrdersEnd(String orderId, boolean end);

    List<RunOrders> findorderbyrundjs(int senderId, int page, int size, String status);

    int acceptOrderRun(int senderId, String orderId);

    void endRun(String orderId);

    SenderTj statistics(int senderId, String beginTime, String endTime);

}
