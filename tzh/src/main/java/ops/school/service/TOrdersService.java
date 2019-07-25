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
    int orderSettlement(String orderId, boolean end);
}
