package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.OrderProductMapper;
import ops.school.api.entity.OrderProduct;
import ops.school.api.service.OrderProductService;
import org.springframework.stereotype.Service;

@Service
public class OrderProductServiceImpl extends ServiceImpl<OrderProductMapper, OrderProduct> implements OrderProductService {
}
