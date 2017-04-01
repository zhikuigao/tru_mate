package com.jws.app.operater.data;

import com.jws.app.operater.model.SysSearchLog;


public interface SysSearchLogMapper {

	int logInsert(SysSearchLog record);

    int logUpdateByPrimaryKey(SysSearchLog record);
}