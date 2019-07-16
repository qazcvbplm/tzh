package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.SignMapper;
import ops.school.api.entity.Sign;
import ops.school.api.service.SignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignServiceImpl extends ServiceImpl<SignMapper, Sign> implements SignService {

    @Autowired
    private SignMapper signMapper;

    @Override
    public Sign findLast(String openId) {
        return signMapper.findLast(openId);
    }
}
