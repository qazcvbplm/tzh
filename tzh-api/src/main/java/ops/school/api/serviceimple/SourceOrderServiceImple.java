package ops.school.api.serviceimple;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.SourceOrderMapper;
import ops.school.api.entity.SourceOrder;
import ops.school.api.service.SourceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceOrderServiceImple extends ServiceImpl<SourceOrderMapper, SourceOrder> implements SourceOrderService {

    @Autowired
    private SourceOrderMapper sourceOrderMapper;


    @Override
    public List<SourceOrder> find(SourceOrder sourceOrder) {
        return sourceOrderMapper.find(sourceOrder);
    }

}
