package ops.school.service;

import ops.school.api.dto.ShopTj;
import ops.school.api.dto.project.ProductOrderDTO;
import ops.school.api.entity.Orders;
import ops.school.api.util.ResponseObject;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface TOrdersService {
    @Transactional
    void addTakeout(Integer[] productIds, Integer[] attributeIndex, Integer[] counts, @Valid Orders orders);

    //int addOrder(List<ProductOrderDTO> productOrderDTOS, @Valid Orders orders);

    /**
     * @date:   2019/7/19 18:15
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   productOrderDTOS
     * @param   orders
     * @Desc:   desc 用户提交订单
     */
    ResponseObject addOrder2(List<ProductOrderDTO> productOrderDTOS, @Valid Orders orders);

    @Transactional
    int pay(Orders orders,String formid);

    @Transactional
    int paySuccess(String orderId, String payment);

    @Transactional
    int cancel(String id);

    @Transactional
    int shopAcceptOrderById(String orderId);

    ShopTj shopstatistics(Integer shopId, String beginTime, String endTime);

    Map countKindsOrderByBIdAndTime(Integer buildId,String beginTime,String endTime);


    @Transactional
    int orderSettlement(String orderId);

    /**
     * @date:   2019/8/6 15:39
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Boolean
     * @param   orders
     * @Desc:   desc 传orders结算少一个查库
     */
    Boolean orderSettlementByOrders(Orders orders);


    /**
     * @date:   2019/8/21 14:38
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Boolean
     * @param   orders
     * @Desc:   desc
     */
    Boolean orderSettlementByOrdersNoSender(Orders orders);

    /**
     * @date:   2019/8/15 15:54
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Boolean
     * @param   orderId
     * @Desc:   desc 接手订单
     */
    ResponseObject shopAcceptOrderById2(String orderId);

    /**
     * @date:   2019/8/15 14:01
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   orderId
     * @Desc:   desc
     */
    ResponseObject printAndAcceptOneOrderByOId(String orderId,Long shopId);
}
