import com.App;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dao.TxLogMapper;
import com.dao.WxUserMapper;
import com.entity.TxLog;
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
    private TxLogMapper txLogMapper;
    @Autowired
    private WxUserMapper wxUserMapper;

    @org.junit.Test
    public void test(){
        QueryWrapper<TxLog> query = new QueryWrapper<>();
        query.lambda().eq(TxLog::getSchoolId, 15);
        System.out.println(txLogMapper.selectCount(query));
    }
}
