package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.OrderProduct;

public interface OrderProductMapper extends BaseMapper<OrderProduct> {

    OrderProduct findByOrderId(String id);

    int updateByPrimaryKeySelective(OrderProduct record);

    int updateByPrimaryKey(OrderProduct record);
}