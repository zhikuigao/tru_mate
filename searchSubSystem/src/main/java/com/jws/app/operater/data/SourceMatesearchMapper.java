package com.jws.app.operater.data;



import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.SourceMatesearch;

public interface SourceMatesearchMapper {
    

    int insert(SourceMatesearch record);
    int userSourceCount(@Param("name") String name, @Param("userId") String userId);
    List<SourceMatesearch> querySearchSource(@Param("userId") String userId);
    int delSearchSource(@Param("id") String id);

   
}