package ops.school.api.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ops.school.api.entity.TxLog;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TxLogMapper extends BaseMapper<TxLog> {

    @Select("select * from tx_log")
    List<TxLog> test(Page<TxLog> txLogPage);
}