package com.jws.app.operater.data;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.jws.app.operater.model.UserConfig;
import com.jws.app.operater.model.UserInfo;

public interface UserInfoMapper {

    int insertSelective(UserInfo record);

    int countAccount(@Param("record")UserInfo record);

    int updateSelective(@Param("record")UserInfo record);
    
    HashMap<String, Object>  queryByAccount(@Param("record")UserInfo record);
    
    UserInfo  queryById(String id);
    
    UserInfo queryUserInfo(@Param("record")UserInfo record);
    
    //搜索
    List<String> queryAllSourceId();
    //模块
    List<String> queryAllModuleId();
    
    void adduserConfigBatch(List<UserConfig> list);
    
    UserInfo queryPerson(@Param("id")String id);
    
    int updatePerson(@Param("record")UserInfo record);

}