package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.SecondHand;

import java.util.List;

public interface SecondHandService extends IService<SecondHand> {


    List<SecondHand> find(SecondHand secondHand);

}
