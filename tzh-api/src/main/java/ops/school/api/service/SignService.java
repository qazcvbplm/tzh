package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.Sign;

public interface SignService extends IService<Sign> {

    Sign findLast(String openId);
}
