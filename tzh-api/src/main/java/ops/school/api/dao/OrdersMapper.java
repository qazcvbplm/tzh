package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.entity.Orders;
import ops.school.api.entity.Sender;
import ops.school.api.entity.Shop;

import java.util.List;
import java.util.Map;

public interface OrdersMapper extends BaseMapper<Orders> {

    Orders selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Orders record);

    List<Orders> find(Orders orders);

    int count(Orders orders);


    int paySuccess(Map<String, Object> map);

    List<Orders> findByShopByDjs(int shopId);

    int shopAcceptOrderById(Orders update);

    List<Orders> findByShop(Shop s);

    int waterNumber(Orders update);

    void remove();


    List<Orders> findBySenderTakeout(Sender sender);

    int SenderAccept(Orders orders);

    int getorder(String orderId);

    int end(Orders orders);

    List<Orders> senderStatistics(Map<String, Object> map);

    int pl(String id);

    int cancel(String id);


    Orders completeByShopId(Map<String, Object> map);

    List<Orders> completeBySchoolId(Map<String, Object> map);

    int countBySchoolId(int schoolId);

    List<Orders> shopsta(Map<String, Object> map);

    List<Orders> findByShopYJS(Shop s);

    List<Orders> findAllDjs();
}