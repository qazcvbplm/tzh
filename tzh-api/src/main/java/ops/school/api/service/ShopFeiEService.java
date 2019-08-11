package ops.school.api.service;

import java.util.List;
import java.util.Map;

import ops.school.api.dto.ShopFeiEDTO;
import ops.school.api.entity.ShopFeiE;
import ops.school.api.util.ResponseObject;

/**
 * @author: QinDaoFang
 * @date:   2019/8/11 18:20
 * @desc:
 */
public interface ShopFeiEService {


    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   dto
     * @Desc:   desc 通过DTO新增
     */
    ResponseObject saveOneShopFeiEByDTO(ShopFeiEDTO dto);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: cn.fang.result.ResponseObject
     * @param   dto
     * @Desc:   desc 通过DTO更新
     */
    ResponseObject updateOneShopFeiEByDTO(ShopFeiEDTO dto);

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
    ShopFeiE findOneShopFeiEById(Long id);

    /**
     * @date:
     * @author: Fang
     * @version:version
     * @return: java.util.List<ShopFeiE>
     * @param   ids
     * @Desc:   desc 通过id集合查询
     */
    List<ShopFeiE> batchFindShopFeiEByIds(List<Long> ids);

}
