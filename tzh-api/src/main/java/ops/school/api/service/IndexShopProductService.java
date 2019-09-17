package ops.school.api.service;

import ops.school.api.entity.IndexShopProduct;
import ops.school.api.util.ResponseObject;

import java.util.List;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/8/22
 * 14:56
 * #
 */
public interface IndexShopProductService {

    /**
     * @date:   2019/8/22 15:09
     * @author: QinDaoFang
     * @version:version
     * @return: java.util.List<ops.school.api.entity.IndexShopProduct>
     * @param   schoolId
     * @Desc:   desc
     */
    ResponseObject findIndexShopProBySchoolId(Long schoolId);

    /**
     * @date:   2019/8/22 16:12
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   shops
     * @param   products
     * @param   schoolId
     * @Desc:   desc
     */
    ResponseObject addIndexShopProduct(String shops, String products, Integer schoolId);

    /**
     * @date:   2019/8/22 16:54
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   schoolId
     * @Desc:   desc
     */
    ResponseObject findIndexStringBySchoolId(Long schoolId);
}
