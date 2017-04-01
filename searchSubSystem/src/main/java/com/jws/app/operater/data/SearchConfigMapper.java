package com.jws.app.operater.data;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.codehaus.jackson.map.Module;

import com.jws.app.operater.model.SearchDefined;
import com.jws.app.operater.model.SearchSource;
import com.jws.app.operater.model.UserConfig;

public interface SearchConfigMapper {
	//查询搜索源
    List<String> queryAllSourceId();
    //查询用户已配置
    List<UserConfig> queryUserConfig(@Param("config")UserConfig config);
    //订阅/取消
    int updateUserConfig(@Param("config")UserConfig config);
    //配置
    void addUserConfig(@Param("config")UserConfig config);
    
    List<SearchSource> queryAllSource(@Param("userId")String userId);
    
    //自定义搜索
    List<SearchDefined> queryUserDefined(@Param("userId")String userId);
    
    int addUserDefined(@Param("defined")SearchDefined defined);
    
    int deleteUserDefined(String id);
    
    int countUserDefined(@Param("name")String name, @Param("url")String url, @Param("userId")String userId);
    
    //模块
    List<Module> queryModule(@Param("userId")String userId);
   
    
}