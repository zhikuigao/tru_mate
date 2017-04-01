package com.jws.app.operater.data;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.AppUsedPercent;
import com.jws.app.operater.model.AppUsedYesterday;

public interface AppUsedPercentMapper {
    /**
     * 根据时间段查询应用使用情况
     * @param start
     * @param end
     * @return
     */
    List<AppUsedYesterday> queryAppUsedYes(@Param("start")String start, @Param("end")String end);
    /**
     * 批量添加应用使用百分比
     * @param list
     * @return
     */
    int batchInsertAppUsedPercent(List<AppUsedPercent> list);
    /**
     * 查询 日、周、月 应用百分比
     * @param userId
     * @return
     */
    List<HashMap<Object, Object>> queryAppPercentOther(@Param("userId")String userId, @Param("day")String day);
    
    List<HashMap<Object, Object>> queryAppPercentYes(@Param("userId")String userId, @Param("day")String day);
    /**
     * 根据userId获取记录id最大值
     * @param userId
     * @return
     */
    Integer getMaxIdByUserId(@Param("userId")String userId);
}