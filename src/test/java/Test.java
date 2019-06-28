import com.App;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dao.WxUserMapper;
import com.entity.TxLog;
import com.service.TxLogService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class Test {

    @Autowired
    private TxLogService txLogService;
    @Autowired
    private WxUserMapper wxUserMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @org.junit.Test
    public void test(){
        IPage<TxLog> list = txLogService.test(new Page<TxLog>(1, 10));
        int i = 0;
    }
}
