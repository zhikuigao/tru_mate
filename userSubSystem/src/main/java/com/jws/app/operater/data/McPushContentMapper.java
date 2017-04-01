package com.jws.app.operater.data;

import java.util.HashMap;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.jws.app.operater.model.McPushContent;

public interface McPushContentMapper {

    int batchInsertPushMsg(List<McPushContent> list);
    int batchUpdatePushStatus(@Param("ids")List<String> ids);
    List<McPushContent> selectPushType(@Param("msgType")String msgType,@Param("userid")String userid);
    List<McPushContent> selectPushContentAll(@Param("msgType")String msgType,@Param("msgSubType")String msgSubType,@Param("page")Integer page,@Param("pageNum")Integer pageNum,@Param("userid")String userid,@Param("lasttime")String lasttime);
    List<McPushContent> selectSystemContentAll(@Param("page")Integer page,@Param("pageNum")Integer pageNum,@Param("userid")String userid,@Param("lasttime")String lasttime);
    List<McPushContent> selectSystemContentNoAll(@Param("page")Integer page,@Param("pageNum")Integer pageNum,@Param("userid")String userid,@Param("lasttime")String lasttime);
    List<McPushContent> queryPushFailData();
    List<McPushContent> queryTimingData(McPushContent content);
    List<McPushContent> selectSystemFirst(@Param("page")Integer page,@Param("pageNum")Integer pageNum,@Param("userid")String userid,@Param("lasttime")String lasttime);
    List<McPushContent> selectSystemMore(@Param("page")Integer page,@Param("pageNum")Integer pageNum,@Param("userid")String userid,@Param("lasttime")String lasttime);
    List<McPushContent> getFirstSystem();
    List<HashMap<String, Object>> getSystemByPage(@Param("pageFrom")Integer pageFrom,@Param("pageNum")Integer pageNum,@Param("lastTime")String lastTime);
}