package com.serviceimple;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.TxLogMapper;
import com.entity.TxLog;
import com.service.TxLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TxLogServiceImple extends ServiceImpl<TxLogMapper, TxLog> implements TxLogService {

    @Autowired
    private TxLogMapper txLogMapper;

    @Override
    public IPage<TxLog> test(Page<TxLog> txLogPage) {
        txLogPage.setRecords(txLogMapper.test(txLogPage));
        return txLogPage;
    }
}
