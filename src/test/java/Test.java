import com.alibaba.fastjson.JSON;
import com.github.qcloudsms.httpclient.HTTPException;
import ops.school.App;
import ops.school.api.entity.Orders;
import ops.school.api.service.TxLogService;
import ops.school.api.util.Util;
import org.json.JSONException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.IOException;


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
        try {
            Util.qqsms(1400169549, "0eb188f83ef4b2dc8976b5e76c70581e", "13958933693", 372755, "123123,ghuighiu", null);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
