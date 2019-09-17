package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.dto.RunOrdersTj;
import ops.school.api.entity.RunOrders;
import ops.school.api.entity.Sender;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface RunOrdersService extends IService<RunOrders> {

    Integer pl(String id);

    void add(@Valid RunOrders orders);

    List<RunOrders> find(RunOrders orders);

    Integer count(RunOrders orders);

    RunOrders findById(String orderId);

    Integer paySuccess(String orderId, String string);

    void remove();

    Integer cancel(String id);

    Integer countBySchoolId(int schoolId);

    List<RunOrders> findBySenderRun(Sender sender);

    Integer senderAccept(RunOrders orders);

    Integer end(String orderId);

    List<RunOrders> senderStatistics(Map<String, Object> map);

    List<RunOrdersTj> tj(Integer schoolId, String day);
}
