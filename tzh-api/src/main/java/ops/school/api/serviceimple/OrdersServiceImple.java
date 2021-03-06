package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.OrdersMapper;
import ops.school.api.entity.Orders;
import ops.school.api.entity.Sender;
import ops.school.api.entity.Shop;
import ops.school.api.exception.Assertions;
import ops.school.api.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrdersServiceImple extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;


    @Override
    public Integer pl(String id) {
        return ordersMapper.pl(id);
    }

    @Override
    public IPage<Orders> find(Orders orders) {
        if (orders.getId() != null) {
            orders.setPage(1);
            orders.setSize(1);
        }
        Page<Orders> page = new Page<Orders>(orders.getPage(), orders.getSize());
        page.setRecords(ordersMapper.find(orders, page));
        return page;
    }

    @Override
    public Integer count(Orders orders) {
        return ordersMapper.count(orders);
    }

    @Override
    public Orders findById(String orderId) {
        return ordersMapper.selectById(orderId);
    }



    @Override
    public List<Orders> findByShopByDjs(int shopId) {
        return ordersMapper.findByShopByDjs(shopId);
    }



    @Override
    public List<Orders> findByShop(int shopId, int page, int size) {
        Shop s = new Shop();
        s.setId(shopId);
        return ordersMapper.findByShop(s,page,size);
    }

    @Override
    public void remove() {
        ordersMapper.remove();
    }




    @Override
    public Integer countBySchoolId(int schoolId) {
        return ordersMapper.countBySchoolId(schoolId);
    }

    @Override
    public List<Orders> findByShopYJS(int shopId, int page, int size) {
        Map<String,Object> map = new HashMap<>();
        map.put("id",shopId);
        map.put("page",(page-1) * size);
        map.put("size",size);
        return ordersMapper.findByShopYJS(map);
    }

    @Override
    public List<Orders> findAllDjs() {
        return ordersMapper.findAllDjs();
    }

    @Override
    public Integer paySuccess(Map<String, Object> map) {
        return ordersMapper.paySuccess(map);
    }

    @Override
    public Integer cancel(String id) {
        return ordersMapper.cancel(id);
    }

    @Override
    public Integer waterNumber(Orders update) {
        return ordersMapper.waterNumber(update);
    }

    @Override
    public Integer shopAcceptOrderById(Orders update) {
        return ordersMapper.shopAcceptOrderById(update);
    }

    @Override
    public List<Orders> findBySenderTakeout(Sender sender) {
        return ordersMapper.findBySenderTakeout(sender);
    }

    @Override
    public Integer senderAccept(Orders orders) {
        return ordersMapper.SenderAccept(orders);
    }

    @Override
    public Integer getorder(String orderId) {
        return ordersMapper.getorder(orderId);
    }

    @Override
    public Integer end(Orders orders) {
        return ordersMapper.end(orders);
    }

    @Override
    public List<Orders> senderStatistics(Map<String, Object> map) {
        return ordersMapper.senderStatistics(map);
    }

    @Override
    public List<Orders> shopsta(Map<String, Object> map) {
        return ordersMapper.shopsta(map);
    }

    @Override
    public Orders completeByShopId(Map<String, Object> map) {
        return ordersMapper.completeByShopId(map);
    }

    @Override
    public List<Orders> completeBySchoolId(Map<String, Object> map) {
        return ordersMapper.completeBySchoolId(map);
    }

    @Override
    public List<Orders> selectDayDataWithComplete(Integer shopId, String orderStatus, String endTime) {
        List<Orders> ordersList = ordersMapper.selectDayDataWithComplete(shopId,orderStatus,endTime+"%");
        return ordersList;
    }

    /**
     * @date:   2019/9/10 12:21
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.entity.Orders
     * @param   senderId
     * @param   beginTime
     * @param   endTime
     * @Desc:   desc
     */
    @Override
    public Orders countSenderDownOrders(int senderId, String beginTime, String endTime) {
        return ordersMapper.countSenderDownOrders(senderId,beginTime,endTime);
    }

    /**
     * @date:   2019/9/10 20:53
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   orderId
     * @Desc:   desc
     */
    @Override
    public Integer makeOrdersToWaitAccept(String orderId) {
        return ordersMapper.makeOrdersToWaitAccept(orderId);
    }
}
