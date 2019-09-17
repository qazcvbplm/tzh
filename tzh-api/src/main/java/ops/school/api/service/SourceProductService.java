package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.SourceProduct;

import java.util.List;

public interface SourceProductService extends IService<SourceProduct> {


    List<SourceProduct> find(SourceProduct sourceProduct);


}
