package com.jws.app.operater.data;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.jws.app.operater.model.SearchMapRecommend;
import com.jws.app.operater.model.SearchMaps;
import com.jws.app.operater.model.SearchMapsData;
import com.jws.app.operater.model.SearchMapsKeyword;
import com.jws.app.operater.model.SearchMapsKeywordNode;



public interface SearchMapsMapper {
	   int insert(SearchMaps record);
	   int insertSearchMapsData(SearchMapsData record);
	   int insertSearchMapsKeyword(SearchMapsKeyword record);
	   int querySearchMapsIstrue(@Param("id") String id);
	   List<SearchMaps> queryHistoryMaps(@Param("userid") String userid);
	   int queryHistoryMapsCount(@Param("userid") String userid,@Param("key") String key,@Param("page") Integer page,@Param("pageNum") Integer pageNum);
	   SearchMaps queryMapById(@Param("id") String id);
	   List<SearchMaps> queryHistoryMaps(@Param("userid") String userid,@Param("key") String key,@Param("page") Integer page,@Param("pageNum") Integer pageNum);
	   int querySearchMapKeywordsIstrue(@Param("id") String id);
	   SearchMaps queryMaps(@Param("id") String id);
	   List<SearchMapsKeyword> queryKeyWords(@Param("id") String id);
	   List<SearchMapsData> queryDatas(@Param("id") String id);
	   List<SearchMapsData> queryDatasSimple(@Param("id") String id,@Param("page") Integer page,@Param("pageNum") Integer pageNum);
	  int queryDatasCount(@Param("id") String id);
	  int queryDataIsTrue(@Param("id") String id,@Param("url") String url);
	  List<SearchMapsKeyword> querySearchLikeKeyword(@Param("keyword") String keyword,@Param("userid") String userid);
	  SearchMapsKeyword querySearchMapsSameKeyWords(@Param("mapId") String mapId);
	  List<SearchMapsKeyword> querySearchMapsKeyWords(@Param("mapId") String mapId);
	  List<SearchMapsKeywordNode> querySearchMapsKeyWordsNodes(@Param("titleId") String titleId);
	   int insertNodes(SearchMapsKeywordNode record);
	  int  querySearchMapsKeyWordsNodesCount(@Param("titleId") String titleId,@Param("url") String url);
	  /**
	   * 更新地图内搜索数据标记
	   * @param record
	   * @return
	   */
	  int updateSearchDataFlag(SearchMapsData record);
	  /**
	   * 查询曾经搜索过此关键字的map
	   * @param userId
	   * @param keyWord
	   * @return
	   */
	  List<SearchMapRecommend> queryOldMapBySameKey(@Param("userId")String userId, @Param("keyword")String keyword);
	  /**
	   * 统计关键字下被标记的文章数
	   * @param keywordId
	   * @return
	   */
	  Integer countFlaggedMapData(@Param("keywordId")String keywordId);
}