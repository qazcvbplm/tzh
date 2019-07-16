package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.ShopCategoryMapper;
import ops.school.api.entity.ShopCategory;
import ops.school.api.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopCategoryServiceImple extends ServiceImpl<ShopCategoryMapper, ShopCategory> implements ShopCategoryService {

    @Autowired
    private ShopCategoryMapper shopCategoryMapper;

    @Override
    public void add(ShopCategory shopCategory) {
        shopCategory.setSort(System.currentTimeMillis());
        this.add(shopCategory);
    }

    @Override
    public List<ShopCategory> find(ShopCategory shopCategory) {
        return shopCategoryMapper.find(shopCategory);
    }


}
