package com.jws.app.operater.data;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.AppVersion;


public interface AppVersionMapper {
	/**
	 * 查询最新的版本信息
	 * @param version
	 * @return
	 */
    List<HashMap<String, Object>> selectNewestVersions(AppVersion version);
    /**
     *  查询版本序号
     * @param version
     * @return
     */
    Integer selectVersionSerial(AppVersion version);
    /**
     * 查询未监控的版本信息
     * @param version
     * @return
     */
    List<HashMap<String, Object>> queryVersionList(AppVersion version);
    /**
     * 批量更新监控状态
     * @param ids
     * @return
     */
    int batchUpdateStatus(@Param("ids")List<String> ids);
    
    int insert(AppVersion app);
    
    int getTotalVersion(AppVersion version);
    
    List<HashMap<String, Object>> getVersionByPage(AppVersion version);
    
    HashMap<String, Object> getVersionInfo(@Param("id")String id);
}