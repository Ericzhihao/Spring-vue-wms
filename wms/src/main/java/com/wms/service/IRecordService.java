package com.wms.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.entity.Record;


public interface IRecordService extends IService<Record> {
    IPage pageCC(IPage<Record> page, Wrapper wrapper);
}
