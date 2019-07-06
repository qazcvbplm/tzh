import ops.school.App;
import ops.school.service.TxLogService;
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
    private StringRedisTemplate stringRedisTemplate;

    @org.junit.Test
    public void test(){
        //stringRedisTemplate.delete(stringRedisTemplate.keys("WX_USER*"));
    }
}
