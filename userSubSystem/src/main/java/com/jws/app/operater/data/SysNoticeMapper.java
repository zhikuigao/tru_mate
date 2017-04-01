package com.jws.app.operater.data;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.AppCategoryRec;
import com.jws.app.operater.model.AppRecommend;
import com.jws.app.operater.model.CategoryRec;
import com.jws.app.operater.model.NewsRecommend;
import com.jws.app.operater.model.SysNotice;
/**
 * 公告、资讯推荐、应用推荐
 * @author Administrator
 *
 */
public interface SysNoticeMapper {

    int insert(SysNotice record);
    /**
     * 根据条件查询通告
     * @param record
     * @return
     */
    List<HashMap<String, Object>> queryNoticeList(SysNotice record);
    /**
     * 批量更新公告推送状态
     * @param record
     * @return
     */
    int batchUpdateStatus(@Param("ids")List<String> ids);
    /**
     * 根据条件查询推荐应用
     * @param app
     * @return
     */
    List<HashMap<String, Object>> queryAppRecList(AppRecommend app);
    /**
     * 根据条件查询推荐资讯
     * @param news
     * @return
     */
    List<HashMap<String, Object>> queryNewsRecList(NewsRecommend news);
    /**
     * 批量更新应用推送状态
     * @param record
     * @return
     */
    int batchUpdateAppStatus(@Param("ids")List<String> ids);
    /**
     * 批量更新资讯推送状态
     * @param record
     * @return
     */
    int batchUpdateNewsStatus(@Param("ids")List<String> ids);
    
    int countNewsCategory(CategoryRec rec);
    
    int insertNewsCategoryRec(CategoryRec rec);
    
    int insertNews(NewsRecommend news);
    
    HashMap<String, Object> queryAppInfo(@Param("id")Integer id);
    
    int countAppCategory(AppCategoryRec rec);
    
    int insertAppCategory(AppCategoryRec rec);
    
    int insertAppRec(AppRecommend app);

}