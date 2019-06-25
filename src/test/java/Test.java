import com.App;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
        TxLog txLog = new TxLog();
        query.setEntity(txLog);
        txLog.setSchoolId(15);
        query.orderByDesc("create_time");
        IPage<TxLog> rs = txLogMapper.selectPage(new Page<>(0, 1000), query);
        System.out.println(rs.getTotal());
    }
}
