package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.OrdersCompleteMapper;
import ops.school.api.entity.OrdersComplete;
import ops.school.api.service.OrderCompleteService;
import org.springframework.stereotype.Service;

@Service
public class OrderCompleteServiceImpl extends ServiceImpl<OrdersCompleteMapper, OrdersComplete> implements OrderCompleteService {
}
