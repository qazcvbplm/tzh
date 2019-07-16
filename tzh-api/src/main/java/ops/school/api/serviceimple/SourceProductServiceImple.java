package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.SourceProductMapper;
import ops.school.api.entity.SourceProduct;
import ops.school.api.service.SourceProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceProductServiceImple extends ServiceImpl<SourceProductMapper, SourceProduct> implements SourceProductService {

    @Autowired
    private SourceProductMapper sourceProductMapper;


    @Override
    public List<SourceProduct> find(SourceProduct sourceProduct) {
        return sourceProductMapper.find(sourceProduct);
    }

}
