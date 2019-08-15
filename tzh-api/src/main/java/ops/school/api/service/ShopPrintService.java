package ops.school.api.service;

import java.util.List;

import ops.school.api.dto.ShopPrintDTO;
import ops.school.api.entity.ShopPrint;
import ops.school.api.util.ResponseObject;

/**
 * @author: QinDaoFang
 * @date:   2019/8/11 18:20
 * @desc:
 */
public interface ShopPrintService {


    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   dto
     * @Desc:   desc 通过DTO新增
     */
    ResponseObject saveOneShopFeiEByDTO(ShopPrintDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   dto
     * @Desc:   desc 通过DTO更新
     */
    ResponseObject updateOneShopFeiEByDTO(ShopPrintDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id删除
     */
    ResponseObject deleteOneShopFeiEById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   id
     * @Desc:   desc 通过id查询一个
     */
    ShopPrint findOneShopFeiEById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<ShopFeiE>
     * @param   ids
     * @Desc:   desc 通过id集合查询
     */
    List<ShopPrint> batchFindShopFeiEByIds(List<Long> ids);

    /**
     * @date:   2019/8/11 20:57
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.entity.ShopPrint
     * @param   shopId
     * @Desc:   desc
     */
    List<ShopPrint> findOneShopFeiEByShopId(Long shopId);

}
