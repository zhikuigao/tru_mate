package com.jws.app.operater.data;

import com.jws.app.operater.model.SysAppLog;

public interface SysAppLogMapper {
    
	int logInsert(SysAppLog record);

    int logUpdateByPrimaryKey(SysAppLog record);
    
}