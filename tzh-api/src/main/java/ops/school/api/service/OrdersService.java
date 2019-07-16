package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.Orders;
import ops.school.api.entity.Sender;

import java.util.List;
import java.util.Map;

public interface OrdersService extends IService<Orders> {

    Integer pl(String id);

    List<Orders> find(Orders orders);

    Integer count(Orders orders);

    Orders findById(String orderId);


    List<Orders> findByShop(int shopId, int page, int size);

    void remove();

    Integer countBySchoolId(int schoolId);

    public List<Orders> findByShopByDjs(int shopId);

    List<Orders> findByShopYJS(int shopId, int page, int size);

    List<Orders> findAllDjs();

    Integer paySuccess(Map<String, Object> map);

    Integer cancel(String id);

    Integer waterNumber(Orders update);

    Integer shopAcceptOrderById(Orders update);

    List<Orders> findBySenderTakeout(Sender sender);

    Integer senderAccept(Orders orders);

    Integer getorder(String orderId);

    Integer end(Orders orders);

    List<Orders> senderStatistics(Map<String, Object> map);

    List<Orders> shopsta(Map<String, Object> map);

    Orders completeByShopId(Map<String, Object> map);

    List<Orders> completeBySchoolId(Map<String, Object> map);
}
