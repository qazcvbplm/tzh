import com.App;
import com.dao.WxUserMapper;
import com.util.SpringUtil;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
@WebAppConfiguration
public class Test {

    @Autowired
    private WxUserMapper wxUserMapper;

    @org.junit.Test
    public void test(){
        System.out.println(SpringUtil.redisCache());
    }
}
