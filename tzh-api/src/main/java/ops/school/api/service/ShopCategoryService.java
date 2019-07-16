package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.ShopCategory;

import java.util.List;

public interface ShopCategoryService extends IService<ShopCategory> {

    void add(ShopCategory shopCategory);

    List<ShopCategory> find(ShopCategory shopCategory);

}
