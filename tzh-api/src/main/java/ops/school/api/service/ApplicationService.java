package ops.school.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.Application;

public interface ApplicationService extends IService<Application> {

    Application login(String name, String pass);
}
