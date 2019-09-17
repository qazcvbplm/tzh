package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.ApplicationMapper;
import ops.school.api.entity.Application;
import ops.school.api.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application> implements ApplicationService {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Override
    public Application login(String name, String pass) {
        Application application = new Application();
        application.setLoginName(name);
        application.setLoginPass(pass);
        return applicationMapper.login(application);
    }
}
