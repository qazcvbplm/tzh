package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.RunOrdersTj;
import ops.school.api.entity.RunOrders;
import ops.school.api.entity.Sender;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface RunOrdersMapper extends BaseMapper<RunOrders> {


    RunOrders selectByPrimaryKey(String id);


    List<RunOrders> find(RunOrders orders);


    int count(RunOrders orders);


    int paySuccess(Map<String, Object> map);


    List<RunOrders> findBySenderRun(Sender sender);


    int SenderAccept(RunOrders orders);


    int end(String orderId);


    void remove();


    List<RunOrders> senderStatistics(Map<String, Object> map);


    List<RunOrders> temp(Map<String, Object> map);


    int pl(String orderid);


    int cancel(String id);


    int countBySchoolId(int schoolId);

    List<RunOrdersTj> tj(@Param("schoolId") Integer schoolId, @Param("day") String day);

    BigDecimal countTotalPriceByFloor(Map<String,Object> map);
}