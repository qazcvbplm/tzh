package ops.school.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.TxLog;

public interface TxLogService extends IService<TxLog> {
    IPage<TxLog> test(Page<TxLog> txLogPage);
}
