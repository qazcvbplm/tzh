package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.SourceOrder;

import java.util.List;

public interface SourceOrderService extends IService<SourceOrder> {

    List<SourceOrder> find(SourceOrder sourceOrder);


}
