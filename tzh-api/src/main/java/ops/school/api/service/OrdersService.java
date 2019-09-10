package ops.school.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.Orders;
import ops.school.api.entity.Sender;

import java.util.List;
import java.util.Map;

public interface OrdersService extends IService<Orders> {

    Integer pl(String id);

    IPage<Orders> find(Orders orders);

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

    /**
     * @date:   2019/9/2 15:45
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.Orders>
     * @param   shopId
     * @param   orderStatus
     * @param   endTime
     * @Desc:   desc
     */
    List<Orders> selectDayDataWithComplete(Integer shopId, String orderStatus, String endTime);

    /**
     * @date:   2019/9/10 12:20
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.entity.Orders
     * @param   senderId
     * @param   beginTime
     * @param   endTime
     * @Desc:   desc
     */
    Orders countSenderDownOrders(int senderId, String beginTime, String endTime);

    /**
     * @date:   2019/9/10 20:52
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   orderId
     * @Desc:   desc
     */
    Integer makeOrdersToWaitAccept(String orderId);
}
