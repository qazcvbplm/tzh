package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.PageQueryDTO;
import ops.school.api.entity.Shop;
import ops.school.api.entity.ShopOpenTime;
import ops.school.api.util.ResponseObject;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ShopService extends IService<Shop> {

    void add(@Valid Shop shop);

    List<Shop> find(Shop shop);

    int update(Shop shop);

    Shop login(String loginName, String enCode);

    int openorclose(Integer id);

    int shoptx(Map<String,Object> map);

    /**
     * @date:   2019/7/22 15:18
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   shop
     * @param   pageQueryDTO
     * @Desc:   desc 分页查询店铺，查询满减，根据开店时间倒叙排（关闭的店铺在最后面）
     */
    ResponseObject findShopWithFullCutOBTime(Shop shop, PageQueryDTO pageQueryDTO);

    /**
     * @date:   2019/8/23 18:44
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Boolean
     * @param   shopOpenTimeList
     * @Desc:   desc
     */
    Boolean ShopNowIsOpen(List<ShopOpenTime> shopOpenTimeList,Integer shopId) throws ParseException;

    /**
     * @date:   2019/8/28 22:32
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   shopId
     * @param   discount
     * @Desc:   desc
     */
    ResponseObject discountAllProductBySId(Integer shopId, BigDecimal discount);
}
