package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.OrdersComplete;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrdersCompleteMapper extends BaseMapper<OrdersComplete> {

    /**
     * @date:   2019/9/7 17:32
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   completeList
     * @Desc:   desc
     */
    Integer batchInsertOrdersComplete(@Param("list") List<OrdersComplete> completeList);
}