package com.jws.app.operater.data;

import com.jws.app.operater.model.SysUserLog;

public interface SysUserLogMapper {

    int userLogInsert(SysUserLog record);

    int userLogUpdateByPrimaryKey(SysUserLog record);
}