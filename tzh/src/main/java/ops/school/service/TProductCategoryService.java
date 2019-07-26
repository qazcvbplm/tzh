package ops.school.service;

import ops.school.api.util.ResponseObject;
import org.springframework.stereotype.Service;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/7/26
 * 10:59
 * #
 */

public interface TProductCategoryService {

    /**
     * @date:   2019/7/26 11:17
     * @author: QinDaoFang
     * @version:version
     * @return: ops.school.api.util.ResponseObject
     * @param   categoryId
     * @Desc:   desc
     */
    ResponseObject deleteCategoryAndProdSById(Integer categoryId);
}
