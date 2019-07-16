package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.SecondHandMapper;
import ops.school.api.entity.SecondHand;
import ops.school.api.service.SecondHandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SecondHandServiceImple extends ServiceImpl<SecondHandMapper, SecondHand> implements SecondHandService {

    @Autowired
    private SecondHandMapper secondHandMapper;

    @Override
    public List<SecondHand> find(SecondHand secondHand) {
        return secondHandMapper.find(secondHand);
    }

}
