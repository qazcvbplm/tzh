package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.RunOrdersMapper;
import ops.school.api.dto.RunOrdersTj;
import ops.school.api.entity.RunOrders;
import ops.school.api.entity.Sender;
import ops.school.api.service.RunOrdersService;
import ops.school.api.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RunOrdersServiceImple extends ServiceImpl<RunOrdersMapper, RunOrders> implements RunOrdersService {

    @Autowired
    private RunOrdersMapper runOrdersMapper;
    @Autowired
    private RedisUtil cache;

    @Override
    public Integer pl(String id) {
        return runOrdersMapper.pl(id);
    }

    @Override
    public void add(@Valid RunOrders orders) {
        runOrdersMapper.insert(orders);

    }

    @Override
    public List<RunOrders> find(RunOrders orders) {
        return runOrdersMapper.find(orders);
    }

    @Override
    public Integer count(RunOrders orders) {
        return runOrdersMapper.count(orders);
    }

    @Override
    public RunOrders findById(String orderId) {
        return runOrdersMapper.selectByPrimaryKey(orderId);
    }

    @Override
    public Integer paySuccess(String orderId, String payment) {
        Map<String, Object> map = new HashMap<>();
        map.put("orderId", orderId);
        map.put("payment", payment);
        map.put("payTimeLong", System.currentTimeMillis());
        RunOrders orders = findById(orderId);
        int rs = runOrdersMapper.paySuccess(map);
        if (rs == 1) {
            cache.runCountadd(orders.getSchoolId());
        }
        return rs;
    }


    @Override
    public void remove() {
        runOrdersMapper.remove();
    }

    @Override
    public Integer cancel(String id) {
        return runOrdersMapper.cancel(id);
    }


    @Override
    public Integer countBySchoolId(int schoolId) {
        // TODO Auto-generated method stub
        return runOrdersMapper.countBySchoolId(schoolId);
    }

    @Override
    public List<RunOrders> findBySenderRun(Sender sender) {
        return runOrdersMapper.findBySenderRun(sender);
    }

    @Override
    public Integer senderAccept(RunOrders orders) {
        return runOrdersMapper.SenderAccept(orders);
    }

    @Override
    public Integer end(String orderId) {
        return runOrdersMapper.end(orderId);
    }

    @Override
    public List<RunOrders> senderStatistics(Map<String, Object> map) {
        return runOrdersMapper.senderStatistics(map);
    }

    @Override
    public List<RunOrdersTj> tj(Integer schoolId, String day) {
        return runOrdersMapper.tj(schoolId, day);
    }

}
