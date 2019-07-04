package com.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ops.school.api.entity.TxLog;

public interface TxLogService {
    IPage<TxLog> test(Page<TxLog> txLogPage);
}
