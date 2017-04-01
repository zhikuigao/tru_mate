package com.jws.app.operater.data;

import com.jws.app.operater.model.SysBusiLog;

public interface SysBusiLogMapper {

    int logInsertSelective(SysBusiLog record);

    int logUpdateByPrimaryKey(SysBusiLog record);
}