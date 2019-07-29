package ops.school.api.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ops.school.api.dto.project.ShopCouponDTO;
import ops.school.api.entity.ShopCoupon;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ShopCouponDAO继承基类
 */
@Repository
public interface ShopCouponMapper extends BaseMapper<ShopCoupon> {


    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   dto
     * @Desc:   desc 分页查询统计
     */
    Integer countLimitByDTO(ShopCouponDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<ShopCouponVO>
     * @param   dto
     * @Desc:   desc 分页查询
     */
    List<ShopCoupon> selectLimitByDTO(ShopCouponDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.LimitTableData<ShopCouponVO>
     * @param   ids
     * @Desc:   desc 批量查询
     */
    List<ShopCoupon> batchFindByIds(@Param("list") List<Long> ids);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   id
     * @Desc:   desc 通过id停用
     */
    Integer stopOneById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.lang.Integer
     * @param   id
     * @Desc:   desc 通过id启用
     */
    Integer startOneById(Long id);

    int insertOne(ShopCoupon shopCoupon);

    /**
     * @date:   2019/7/25 21:05
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   shopCouponList
     * @Desc:   desc
     */
    Integer batchInsert(@Param("list") List<ShopCoupon> shopCouponList);

    /**
     * @date:   2019/7/26 17:59
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.ShopCoupon>
     * @param   couponIdS
     * @Desc:   desc 根据couponIds查询
     */
    List<ShopCoupon> batchFindSCByCouponIdS(@Param("list") List<Long> couponIdS);

    /**
     * @date:   2019/7/27 11:48
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   deleteShopIds
     * @Desc:   desc 根据店铺id批量逻辑删除优惠卷
     */
    Integer batchDeleteSCByShopId(@Param("list") List<Long> deleteShopIds);

    /**
     * @date:   2019/7/27 11:53
     * @author: QinDaoFang
     * @version:version
     * @return: java.lang.Integer
     * @param   id
     * @Desc:   desc 根据优惠券id批量逻辑删除优惠卷
     */
    Integer batchDeleteSCByCouponId(Long id);
}