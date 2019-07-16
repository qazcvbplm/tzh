package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.Shop;

import javax.validation.Valid;
import java.util.List;

public interface ShopService extends IService<Shop> {

    void add(@Valid Shop shop);

    List<Shop> find(Shop shop);

    int update(Shop shop);

    Shop login(String loginName, String enCode);

    int openorclose(Integer id);


}
