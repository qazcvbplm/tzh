package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ops.school.api.entity.Orders;
import ops.school.api.entity.Sender;
import ops.school.api.entity.Shop;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Repository
public interface OrdersMapper extends BaseMapper<Orders> {

    Orders selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Orders record);

    List<Orders> find(@Param("o") Orders orders, Page page);

    int count(Orders orders);


    int paySuccess(Map<String, Object> map);

    List<Orders> findByShopByDjs(int shopId);

    int shopAcceptOrderById(Orders update);

    List<Orders> findByShop(@Param("s") Shop s, @Param("page")int page,@Param("size") int size);

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

    List<Orders> findByShopYJS(Map<String,Object> map);

    List<Orders> findAllDjs();

    BigDecimal countPayPriceByFloor(Map<String,Object> map);

    /**
     * @date:   2019/9/2 15:47
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.Orders>
     * @param   shopId
     * @param   orderStatus
     * @param   endTime
     * @Desc:   desc
     */
    List<Orders> selectDayDataWithComplete(@Param("shopId") Integer shopId,@Param("orderStatus") String orderStatus,@Param("endTime") String endTime);
}