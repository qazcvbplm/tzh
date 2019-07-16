package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.FullCut;

import java.util.List;

public interface FullCutService extends IService<FullCut> {

    List<FullCut> findByShopId(Integer shopId);
}
