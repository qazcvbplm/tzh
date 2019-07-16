package ops.school.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.Floor;

import java.util.List;

public interface FloorService extends IService<Floor> {

    void add(Floor floor);

    List<Floor> find(Floor floor);


}
