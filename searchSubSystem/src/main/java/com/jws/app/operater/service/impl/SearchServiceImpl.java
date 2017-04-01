package com.jws.app.operater.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.Resource;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.Module;
import org.springframework.stereotype.Service;
import com.jws.app.operater.data.SearchConfigMapper;
import com.jws.app.operater.data.SearchMapsMapper;
import com.jws.app.operater.data.SysSearchLogMapper;
import com.jws.app.operater.data.SystemTimeMapper;
import com.jws.app.operater.model.SearchDefined;
import com.jws.app.operater.model.SearchMapRecommend;
import com.jws.app.operater.model.SearchMaps;
import com.jws.app.operater.model.SearchMapsData;
import com.jws.app.operater.model.SearchMapsKeyword;
import com.jws.app.operater.model.SearchMapsKeywordNode;
import com.jws.app.operater.model.SearchSource;
import com.jws.app.operater.model.SearchViewedBy;
import com.jws.app.operater.model.SysSearchLog;
import com.jws.app.operater.model.UserConfig;
import com.jws.app.operater.service.SearchBusiService;
import com.jws.app.operater.service.SearchMapShareService;
import com.jws.app.operater.service.SearchService;
import com.jws.app.operater.service.SourceMatesearchService;
import com.jws.app.operater.service.UserService;
import com.jws.common.constant.BusiCodeConstant;
import com.jws.common.constant.ConfigConstants;
import com.jws.common.constant.Constants;
import com.jws.common.constant.SearchEnum;
import com.jws.common.util.JiveGlobe;
import com.jws.common.util.JsonUtil;
import com.jws.common.util.MD5Util;
import com.jws.common.util.ResultPackaging;
import org.apache.commons.httpclient.*;   
import org.apache.commons.httpclient.methods.*; 


@Service("searchService")
public class SearchServiceImpl  implements SearchService {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private SearchMapShareService searchMapShareService;
	@Resource
	private SourceMatesearchService sourceMatesearchService;
	@Resource
	private SearchBusiService searchBusiService;
	@Resource
	private UserService userService;
	@Resource
	private SysSearchLogMapper sysSearchLogMapper;
	@Resource
	private SearchConfigMapper searchConfigMapper;
	@Resource
	private SearchMapsMapper searchMapsMapper;
	@Resource
	private SystemTimeMapper systemTimeMapper;
	
//	private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	@Override
	public JSONObject commonEntry(String param) {
		// 返回结果		
		JSONObject returnJson = new JSONObject();
		// 请求参数
		JSONObject requestJson = new JSONObject();		
		// 业务参数
		JSONObject paramJson = new JSONObject();
		// 安全参数
		JSONObject  securityJson = new JSONObject();
		//1.记录调用日志
		requestJson =JSONObject.fromObject(param);
		SysSearchLog  log = addCallLog(param, "busiSystem");
		try {
			paramJson = requestJson.optJSONObject("paramMap");
			securityJson =  requestJson.optJSONObject("securityJson");
		} catch (Exception e) {
			logger.error("参数转换失败："+e.getMessage());
			returnJson = ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_CONVERT_FAILED, null, requestJson.optString("language", "ZH"));
			updateCallLog(returnJson, log, null);
			return  returnJson;
		}
		// 参数验证
		if (!securityJson.has("time") || !securityJson.has("Md5Str") ||!paramJson.has("busiCode") || !paramJson.has("language") || 
				 !paramJson.has("source")) {
			returnJson = ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "ZH"));
			updateCallLog(returnJson, log, null);
			return  returnJson;
		}
		//2.安全校验
		securityJson.put("language", paramJson.optString("language", "ZH"));
		JSONObject securityObject = securityVerification(securityJson);
		if (null != securityObject) {
			updateCallLog(securityObject, log, paramJson);
			return  securityObject;
		}		
		//3.子业务跳转
		try {
			//分配到业务层 appBusiService
			if (paramJson.has("keyCode") && StringUtils.equals(paramJson.optString("keyCode"), SearchEnum.Share.toString())) {
				returnJson =searchMapShareService.entry(paramJson);
			}
			if (paramJson.has("keyCode") && StringUtils.equals(paramJson.optString("keyCode"), SearchEnum.Source.toString())) {
				returnJson =sourceMatesearchService.entry(paramJson);
			}
			if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.SAVE_CONFIG)) {
				// 保存配置
				returnJson = saveConfig(paramJson);
			}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.QUERY_CONFIG)) {
				// 查询配置
				returnJson = queryConfig(paramJson);
			}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.INTERNAL_SEARCH)) {
				//小美内部搜索接口
				returnJson = internalSearch(paramJson);	
			}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.GOOGLE_DATAS)) {
				//小美内部搜索接口
				returnJson = requestGoogleDatas(paramJson);	
			}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.ADDMAPS_SEARCH)) {
				returnJson = addMapsSearch(paramJson);	
			}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.QUERYHISTORY_MAPS)) {
				returnJson = queryHistoryMaps(paramJson);	
			}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.QUERYHISTORY_MAPSDATAS)) {
				returnJson = querySearchMapsDatas(paramJson);	
			}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.QUERQUERY_MAPKEYWORDDATAS)) {
				returnJson = querySearchMapKeywordDatas(paramJson);	
			}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.QUERQUERY_SEARCHLIKEKEYWORD)) {
				returnJson = querySearchLikeKeyword(paramJson);	
			}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.QUERQUERY_SEARCHDATANODES)) {
				returnJson = querySearchMapsDataNodes(paramJson);	
			}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.ADD_MAPSNODES)) {
				returnJson = addMapNodes(paramJson);	
			}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.FLAG_MAP_DATA)) {
				//给地图内的文章加标记
				returnJson = flagMapData(paramJson);	
			}else if (StringUtils.equals(paramJson.optString("busiCode"), BusiCodeConstant.OLD_MAP_RECOMMEND)) {
				//搜索关键词时旧地图推荐
				returnJson = oldMapRecommend(paramJson);	
			}
		} catch (Exception e) {
			logger.error("子系统程序异常"+e.getMessage());
			returnJson = ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_EXCEPTION, null, requestJson.optString("language", "ZH"));
		}
		//4.更新调用日志
		updateCallLog(returnJson, log, paramJson);
		return returnJson;
	}
	
	/**
	 * 搜索关键词时旧地图推荐
	 * @param json
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private JSONObject oldMapRecommend(JSONObject requestJson){
		if (!requestJson.has("userId")|| !requestJson.has("keyword") ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "ZH"));
		}
		try {
			String userId = requestJson.getString("userId");
			String keyword = requestJson.getString("keyword");
			//获取同一关键字历史map
			List<SearchMapRecommend> list = searchMapsMapper.queryOldMapBySameKey(userId, keyword);
			if (null == list || list.size() == 0) {
				return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, new JSONObject(), requestJson.optString("language", "ZH"));
			}
			//获取每个关键字下，标记的文章数
			HashMap<String, Object> idMap = new HashMap<>();
			Boolean flagged = false;
			for (int i = 0; i < list.size(); i++) {
				SearchMapRecommend map = list.get(i);
				map.setDataCount(searchMapsMapper.countFlaggedMapData(map.getKeywordId()));
				if (map.getDataCount()>0) {
					flagged = true;
				}
				//过滤同一个map下搜索多次此关键字，并叠加标记文章
				if (idMap.containsKey(map.getId())) {
					SearchMapRecommend oldMap = (SearchMapRecommend) idMap.get(map.getId());
					//同一个地图下不同关键字标记文章数叠加
					oldMap.setDataCount(map.getDataCount()+oldMap.getDataCount());				
				}else{
					idMap.put(map.getId(), map);
				}			
			}
			//如果不存在标记的文章，直接返回最新的map
			if (!flagged) {
				return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, list.get(0), requestJson.optString("language", "ZH"));
			}
			list = new ArrayList<>();
			Iterator iter = idMap.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				list.add((SearchMapRecommend) entry.getValue());
			}
			//排序
			Collections.sort(list);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, list.get(0), requestJson.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("搜索关键字时推荐地图异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_EXCEPTION, null, requestJson.optString("language", "ZH"));
		}
	}

	/**
	 * 给地图内的文章加标记
	 * @param json
	 * @return
	 */
	private JSONObject flagMapData(JSONObject requestJson){
		if (!requestJson.has("id")|| !requestJson.has("flag") ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "ZH"));
		}
		SearchMapsData data = new SearchMapsData();
		try {
			data.setId(requestJson.getInt("id"));
			data.setKeepFlag(requestJson.getString("flag"));
			int result = searchMapsMapper.updateSearchDataFlag(data);
			if (result>0) {
				return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, requestJson.optString("language", "ZH"));
			}
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.ERROR_MAPS_DATA_ID, null, requestJson.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("给地图内的文章加标记程序异常");
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_EXCEPTION, null, requestJson.optString("language", "ZH"));
		}
	}

	
	private JSONObject	addMapsSearch(JSONObject requestJson){
		if (!requestJson.has("mapId") ||StringUtils.isEmpty(requestJson.getString("mapId")) || !requestJson.has("userId") || !requestJson.has("keyword") || !requestJson.has("keywordId") || !requestJson.has("title") || !requestJson.has("url")  || !requestJson.has("sourceSearch")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "ZH"));
		}
		try {
				String mapsId =  requestJson.getString("mapId");
				String userId = requestJson.getString("userId");
				String keyword = requestJson.getString("keyword");
				String keywordId = requestJson.getString("keywordId");
				String title = requestJson.getString("title");
				String url = requestJson.getString("url");
				String sourceSearch = requestJson.getString("sourceSearch");
				int count  = this.searchMapsMapper.querySearchMapsIstrue(mapsId);
				if(count<1){
					//查询rodmap表是否已经存在了这个搜索记录，若没有就添加
					SearchMaps mp = new SearchMaps();
					mp.setCreateTime(systemTimeMapper.getSystemTime());
					mp.setFirstKeyword(keyword);
					mp.setId(mapsId);
					mp.setUserId(userId);
				//	mp.setSource(sourceSearch);
					this.searchMapsMapper.insert(mp);
				}
				int countKeyWorldId  = this.searchMapsMapper.querySearchMapKeywordsIstrue(keywordId);
				//若有重复的keyword则不需要添加进数据库，若没重复的需要到数据库查询是否有相同关键字的keywrod
				if(countKeyWorldId<1){
					SearchMapsKeyword sm = this.searchMapsMapper.querySearchMapsSameKeyWords(mapsId);
					if(!JiveGlobe.isEmpty(sm) && sm.getKeyword().toString().equals(keyword)){
							keywordId = sm.getId();
					}else{
						SearchMapsKeyword sd = new SearchMapsKeyword();
						sd.setId(keywordId);
						sd.setCreateTime(systemTimeMapper.getSystemTime());
						sd.setKeyword(keyword);
						sd.setMapId(mapsId);
						sd.setSource(sourceSearch);
						this.searchMapsMapper.insertSearchMapsKeyword(sd);
					}
				}
				//添加详情
				int countData = this.searchMapsMapper.queryDataIsTrue(keywordId, url);
				if(countData<1){
					SearchMapsData sa = new SearchMapsData();
					sa.setCreateTime(systemTimeMapper.getSystemTime());
					sa.setKeywordId(keywordId);
					sa.setUrl(url);
					sa.setTitle(title);
					this.searchMapsMapper.insertSearchMapsData(sa);
				}
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, requestJson.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("添加rodmap数据异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_KEY_FAILED, null, requestJson.optString("language", "ZH"));
		}
	}
	
	private JSONObject	queryHistoryMaps(JSONObject requestJson){
		if (!requestJson.has("userId") ||!requestJson.has("key") || !requestJson.has("page") ||StringUtils.isEmpty(requestJson.getString("page")) || !requestJson.has("pageNum") ||StringUtils.isEmpty(requestJson.getString("pageNum")) ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		try {
			    JSONObject json = new JSONObject();
			    int count = 0;
				String userId = requestJson.getString("userId");
				String key = requestJson.getString("key");
				Integer page = requestJson.optInt("page");
				Integer pageNum = requestJson.optInt("pageNum");
				if(JiveGlobe.isEmpty(key)){
					key = null;
				}else{
					key = "%" + key +"%";
				}
				List<SearchMaps> queryHistoryMaps = this.searchMapsMapper.queryHistoryMaps(userId,key,page*pageNum,pageNum);
				if(!JiveGlobe.isEmpty(queryHistoryMaps)){
					List<SearchMaps> queryMaps = new ArrayList<SearchMaps>();
					count = this.searchMapsMapper.queryHistoryMapsCount(userId,key,page,pageNum);
					for(int i=0;i<queryHistoryMaps.size();i++){
						SearchMaps sm = queryHistoryMaps.get(i);
						String mapId = sm.getId();
						//查询出所有的关键字
						List<String> labellist = new ArrayList<String>();
						List<SearchMapsKeyword> sk = this.searchMapsMapper.querySearchMapsKeyWords(mapId);
						for(int j=0;j<sk.size();j++){
							SearchMapsKeyword sd = sk.get(j);
							if(j<10){	
								//label += sd.getKeyword() + ","; 
								labellist.add(sd.getKeyword());
							}
						}
						//System.out.println(labellist.toString());
						sm.setLabel(labellist);
						queryMaps.add(sm);
					}
					queryHistoryMaps = queryMaps;
				}
				json = JsonUtil.addJsonObject("total", count, json);
				json = JsonUtil.addJsonObject("list", queryHistoryMaps, json);
				json = JsonUtil.addJsonObject("page", page, json);
				//更新用户现使用的版本信息
				if (page==0 && StringUtils.isNotEmpty(requestJson.optString("mateVersion")) && StringUtils.isNotEmpty(requestJson.optString("device"))) {
					userService.synUserMateVersion(requestJson.optString("mateVersion"), requestJson.optString("device"), userId);
				} 
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, json, requestJson.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("查询rodmap数据异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_KEY_FAILED, null, requestJson.optString("language", "ZH"));
		}
	}
		
	
	
	
	private JSONObject	addMapNodes(JSONObject requestJson){
		if (!requestJson.has("titleId") ||!requestJson.has("title") || !requestJson.has("url") ||StringUtils.isEmpty(requestJson.getString("titleId"))  ||StringUtils.isEmpty(requestJson.getString("title")) ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		try {
			SearchMapsKeywordNode sn = new SearchMapsKeywordNode();
			String titleId = requestJson.optString("titleId");
			String	 title = requestJson.optString("title");
			String	 url = requestJson.optString("url");
			sn.setCreateTime(systemTimeMapper.getSystemTime());
			sn.setTitle(title);
			sn.setTitleId(Integer.valueOf(titleId));
			sn.setUrl(url);
			 int  querySearchMapsKeyWordsNodes = this.searchMapsMapper.querySearchMapsKeyWordsNodesCount(titleId,url);
			if(querySearchMapsKeyWordsNodes>0){
				return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.EXIST_DEFINED, null, requestJson.optString("language", "ZH"));
			}
			this.searchMapsMapper.insertNodes(sn);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, requestJson.optString("language", "ZH"));
		} catch (Exception e) {
			System.out.println(e);
			logger.error("保存数据异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_KEY_FAILED, null, requestJson.optString("language", "ZH"));
		}
	}

	private JSONObject	querySearchMapsDatas(JSONObject requestJson){
		JSONObject jsonObject = new JSONObject();
		if (!requestJson.has("id")||StringUtils.isEmpty(requestJson.getString("id")) ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		try {
			String id=  requestJson.getString("id");
			SearchMaps searchMaps = this.searchMapsMapper.queryMaps(id);
			if(!JiveGlobe.isEmpty(searchMaps)){
				jsonObject = JsonUtil.addJsonObject("maps", searchMaps, jsonObject);
				List<SearchMapsKeyword> queryKeyWords = this.searchMapsMapper.queryKeyWords(id);
				if(!JiveGlobe.isEmpty(queryKeyWords)){
					jsonObject = JsonUtil.addJsonObject("mapKeywords", queryKeyWords, jsonObject);
					String keywordId = "";
					List<SearchMapsData> dataList = new ArrayList<SearchMapsData>();
					//迭代keyword数据，得到详情
					List<SearchMapsData> queryDatas = new ArrayList<SearchMapsData>();
					for(int i=0;i<queryKeyWords.size();i++){
						SearchMapsKeyword sw = queryKeyWords.get(i);
						keywordId = sw.getId();
						 queryDatas = this.searchMapsMapper.queryDatas(keywordId);
						if(!JiveGlobe.isEmpty(queryDatas)){
							dataList.addAll(queryDatas);
						}
					}
					//组装数据
					jsonObject = JsonUtil.addJsonObject("mapDatas", dataList, jsonObject);
				}
			}
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, jsonObject, requestJson.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("查询rodmap数据异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.QUERY_MAP_FAILED, null, requestJson.optString("language", "ZH"));
		}
	}
	
	private JSONObject	querySearchMapsDataNodes(JSONObject requestJson){
		JSONObject jsonObject = new JSONObject();
		if (!requestJson.has("id")||StringUtils.isEmpty(requestJson.getString("id")) ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		try {
			//新版search地图
			String id=  requestJson.getString("id");
			SearchMaps searchMaps = this.searchMapsMapper.queryMaps(id);
			List<SearchMapsKeyword> queryKeyWords = this.searchMapsMapper.queryKeyWords(id);
			if(!JiveGlobe.isEmpty(searchMaps)){
				jsonObject = JsonUtil.addJsonObject("maps", searchMaps, jsonObject);
				if(!JiveGlobe.isEmpty(queryKeyWords)){
					jsonObject = JsonUtil.addJsonObject("mapKeywords", queryKeyWords, jsonObject);
					String keywordId = "";
					List<SearchMapsData> dataList = new ArrayList<SearchMapsData>();
					//迭代keyword数据，得到详情
					List<SearchMapsData> queryDatas = new ArrayList<SearchMapsData>();
					for(int i=0;i<queryKeyWords.size();i++){
						SearchMapsKeyword sw = queryKeyWords.get(i);
						keywordId = sw.getId();
						 queryDatas = this.searchMapsMapper.queryDatas(keywordId);
						//查询二级url
						if(!JiveGlobe.isEmpty(queryDatas)){
							for(int j=0;j<queryDatas.size();j++){
								SearchMapsData sd = new SearchMapsData();
								sd = queryDatas.get(j);
								//查询是否有二级url
								 List<SearchMapsKeywordNode> nodelist= this.searchMapsMapper.querySearchMapsKeyWordsNodes( sd.getId()+"");
								 sd.setDataNode(nodelist);
								dataList.add(sd);
							}
						}
					}
					//组装数据
					jsonObject = JsonUtil.addJsonObject("mapDatas", dataList, jsonObject);
				}
			}
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, jsonObject, requestJson.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("查询rodmap数据异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_KEY_FAILED, null, requestJson.optString("language", "ZH"));
		}
	}
	
	private JSONObject	querySearchMapKeywordDatas(JSONObject requestJson){
		JSONObject jsonObject = new JSONObject();
		if (!requestJson.has("keyWord")||StringUtils.isEmpty(requestJson.getString("page")) ||StringUtils.isEmpty(requestJson.getString("pageNum"))) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		try { 
			String keyWord = requestJson.getString("keyWord");
			int count = 0;
			String page = requestJson.getString("page");
			String pageNum = requestJson.getString("pageNum");
			List<SearchMapsData> queryDatas = this.searchMapsMapper.queryDatasSimple(keyWord,Integer.valueOf(page)*Integer.valueOf(pageNum),Integer.valueOf(pageNum));
			jsonObject = JsonUtil.addJsonObject("list", queryDatas, jsonObject);
			if(!JiveGlobe.isEmpty(queryDatas)){
				count = this.searchMapsMapper.queryDatasCount(keyWord);
			}
			jsonObject = JsonUtil.addJsonObject("total", count, jsonObject);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, jsonObject, requestJson.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("查询rodmap数据异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_KEY_FAILED, null, requestJson.optString("language", "ZH"));
		}
	}

	private JSONObject	querySearchLikeKeyword(JSONObject requestJson){
		if (!requestJson.has("key")||StringUtils.isEmpty(requestJson.getString("userId")) ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "EN"));
		}
		try {
			String keyWord = requestJson.getString("key");
			String page = requestJson.getString("userId");
			List<SearchMapsKeyword>  ls = this.searchMapsMapper.querySearchLikeKeyword(keyWord+"%", page);
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, ls, requestJson.optString("language", "ZH"));
		} catch (Exception e) {
			logger.error("查询keyword数据异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_KEY_FAILED, null, requestJson.optString("language", "ZH"));
		}
	}
	
	private JSONObject internalSearch(JSONObject requestJson){
		if (!requestJson.has("searchKey")|| StringUtils.isEmpty(requestJson.getString("searchKey"))
				|| !requestJson.has("number")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "ZH"));
		}
		int number = Integer.valueOf(requestJson.getString("number"));
		List<SearchViewedBy> list= searchBusiService.querySearchRecommend(requestJson);
		if (list.size()>number) {
			list = list.subList(0, number);
		}
		return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, list, requestJson.optString("language", "ZH"));
	}
	private JSONObject queryConfig(JSONObject requestJson){
		if (!requestJson.has("userId")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "ZH"));
		}
		String userId = requestJson.getString("userId");
		//查询系统搜索源
		List<SearchSource>  searchSource = searchConfigMapper.queryAllSource(userId);
		//查询用户自定义搜索源
		List<SearchDefined> searchDefined = searchConfigMapper.queryUserDefined(userId);
		//模块
		List<Module> module = searchConfigMapper.queryModule(userId);
		HashMap<String, Object> resultMap = new HashMap<>();
		if (null != searchSource) {
			resultMap.put("searchSource", searchSource);
		}else {
			resultMap.put("searchSource", "");
		}
		if (null != searchDefined) {
			resultMap.put("searchDefined", searchDefined);
		}else {
			resultMap.put("searchDefined", "");
		}
		if (null != module) {
			resultMap.put("module", module);
		}else {
			resultMap.put("module", "");
		}
		return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, resultMap, requestJson.optString("language", "ZH"));
	}
	
	
	
	
	private JSONObject saveConfig(JSONObject requestJson){
		if (!requestJson.has("userId") || !requestJson.has("type")|| !requestJson.has("operate")
				|| (!requestJson.has("id") && (!requestJson.has("name") || !requestJson.has("url"))) ) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "ZH"));
		}
		
		String userId = requestJson.getString("userId");
		String type = requestJson.getString("type");
		String operate = requestJson.getString("operate");
		//type 1 模块 2搜索 3 自定义搜索
		if (StringUtils.equals("1", type) || StringUtils.equals("2", type)) {
			JSONObject json = saveSubscribe(type, operate, userId, requestJson.getString("id"), requestJson.optString("language", "ZH"));
			if (null != json) {
				return json;
			}
		}else if(StringUtils.equals("3", type)){
			//删除
			if (requestJson.has("id") && StringUtils.equals("delete", requestJson.getString("operate"))) {
				searchConfigMapper.deleteUserDefined(requestJson.getString("id"));
			}else if (requestJson.has("name") && requestJson.has("url") && StringUtils.equals("add", requestJson.getString("operate"))) {
				String name= requestJson.getString("name");
				String url= requestJson.getString("url");
				//检索重复数据
				int count = searchConfigMapper.countUserDefined(name, url, userId);
				if (count >0 ) {
					return ResultPackaging.dealJsonObject(Constants.EXIST_DEFINED, Constants.EXIST_DEFINED, null, requestJson.optString("language", "ZH"));
				}else{
					SearchDefined defined = new SearchDefined();
					defined.setId(UUID.randomUUID().toString().replaceAll("-", ""));
					defined.setName(name);
					defined.setUrl(url);
					defined.setUserId(userId);
					searchConfigMapper.addUserDefined(defined);
					return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, defined, requestJson.optString("language", "ZH"));
				}
			}else{
				return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.ERROR_PARAMTER, null, requestJson.optString("language", "ZH"));
			}
		}else{
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.ERROR_PARAMTER, null, requestJson.optString("language", "ZH"));
		}
		return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, requestJson.optString("language", "ZH"));
	}
	
	
	private JSONObject requestGoogleDatas(JSONObject requestJson){
		if (!requestJson.has("start") || !requestJson.has("queryString")) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "ZH"));
		}
		String start = requestJson.getString("start");
		String queryString = requestJson.getString("queryString");
		JSONObject json = new JSONObject();
		try {
			 HttpClient client = new HttpClient();//定义client对象
			 client.getHttpConnectionManager().getParams().setConnectionTimeout(2000);//设置连接超时时间为2秒（连接初始化时间）
			 GetMethod method = new GetMethod(""+ConfigConstants.GOOGLESEARCH_IP+"/googleSearch/data.do?start="+start+"&queryString="+queryString+"");//访问下谷歌的首页
			 int statusCode = client.executeMethod(method);//状态，一般200为OK状态，其他情况会抛出如404,500,403等错误
			if (statusCode != 200) {
				return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_KEY_FAILED, null, requestJson.optString("language", "ZH"));
			}
		//	System.out.println(method.getResponseBodyAsString());//输出反馈结果						    
			client.getHttpConnectionManager().closeIdleConnections(1);
		//	System.out.println(method.getResponseBodyAsString());
			json = JSONObject.fromObject( method.getResponseBodyAsString());
			return json;
		} catch (Exception e) {
			logger.error("获取google数据异常"+e.getMessage());
			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_KEY_FAILED, null, requestJson.optString("language", "ZH"));
		}
		
	}
	
	
	
	private JSONObject saveSubscribe(String type,String operate, String userId,String id,String language){
		if (StringUtils.equals("subscribe", operate)) {
			//订阅
			UserConfig config = new UserConfig();
			config.setUserId(userId);
			config.setSourceId(id);
			config.setType(type);
			List<UserConfig> oldConfigs = searchConfigMapper.queryUserConfig(config);
			if (null != oldConfigs && oldConfigs.size()>0) {
				config.setFlag("1");
				searchConfigMapper.updateUserConfig(config);
			}else{
				config.setId(UUID.randomUUID().toString().replaceAll("-", ""));
				searchConfigMapper.addUserConfig(config);
			}
		}else if (StringUtils.equals("cancel", operate)) {
			//取消订阅
			UserConfig config = new UserConfig();
			config.setUserId(userId);
			config.setSourceId(id);
			config.setFlag("2");
			config.setType(type);
			searchConfigMapper.updateUserConfig(config);
		}else{
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.ERROR_PARAMTER, null, language);
		}
		return null;
	}

	/**
	 * 保存搜索条件
	 * @param requestJson
	 * @return
	 */
//	private JSONObject saveSearchKey(JSONObject requestJson) {
//		JSONObject returnJson = new JSONObject();
//
//		if (!requestJson.has("userId") || !requestJson.has("keyWord")) {
//			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "ZH"));
//		}
//		
//		String keyId = UUID.randomUUID().toString().replaceAll("-", "");
//		SearchKey searchKey = new SearchKey();
//		searchKey.setId(keyId);
//		searchKey.setUserId(requestJson.optString("userId"));
//		searchKey.setKeyWord(requestJson.optString("keyWord"));
//		
//		try {
//			searchBusiService.saveSearchKey(searchKey);
//			returnJson.put("keyId", keyId);
//			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, returnJson, requestJson.optString("language", "ZH"));
//		} catch (ServiceException e) {
//			logger.error("保存搜索条件异常"+e.getMessage());
//			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_KEY_FAILED, null, requestJson.optString("language", "ZH"));
//		}
//	}

	/**
	 * 保存查看链接
	 * @param requestJson
	 * @return
	 */
//	private JSONObject saveSearchViewed(JSONObject requestJson) {
//		JSONObject returnJson = new JSONObject();
//
//		if (!requestJson.has("userId") || !requestJson.has("url")) {
//			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "ZH"));
//		}
//		
//		String viewedId = UUID.randomUUID().toString().replaceAll("-", "");
//
//		SearchViewed searchViewed = new SearchViewed();
//		searchViewed.setId(viewedId);
//		searchViewed.setUserId(requestJson.optString("userId"));
//		searchViewed.setKeyWord(requestJson.optString("keyWord"));
//		searchViewed.setPreUrl(requestJson.optString("preUrl"));		
//		searchViewed.setUrl(requestJson.optString("url"));
//		searchViewed.setTitleTxt(requestJson.optString("titleTxt"));
//		searchViewed.setAbstractTxt(requestJson.optString("abstractTxt"));
//		
//		try {
//			searchBusiService.saveSearchViewed(searchViewed);
//			returnJson.put("viewedId", viewedId);
//			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, returnJson, requestJson.optString("language", "ZH"));
//		} catch (ServiceException e) {
//			logger.error("保存查看链接异常"+e.getMessage());
//			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_VIEWED_FAILED, null, requestJson.optString("language", "ZH"));
//		}
//	}

	/**
	 * 查询用户搜索历史
	 * @param requestJson
	 * @return
	 */
//	private JSONObject querySearchKey(JSONObject requestJson) {
//		JSONObject returnJson = new JSONObject();
//		
//		if (!requestJson.has("userId") || !requestJson.has("pageNum") || !requestJson.has("currPage")) {
//			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "ZH"));
//		}
//		String userId = requestJson.optString("userId");
//		String keyWord = requestJson.optString("keyWord");
//		int pageNum = requestJson.optInt("pageNum");
//		int currPage = requestJson.optInt("currPage");
//		
//		if (StringUtils.isEmpty(keyWord)) {
//			keyWord = null;
//		} else {
//			keyWord = "%"+ keyWord + "%";
//		}
//		try {
//			int totalNum = searchBusiService.countKeyHistory(userId, keyWord);
//			List<SearchKey> searchKeys = searchBusiService.querySearchKey(userId, keyWord, currPage, pageNum);
//			returnJson.put("searchHistory", gson.toJson(searchKeys));
//			returnJson.put("currPage", currPage + 1);
//			returnJson.put("totalNum", totalNum);
//			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, returnJson, requestJson.optString("language", "ZH"));
//		} catch (ServiceException e) {
//			logger.error("查询用户搜索历史异常"+e.getMessage());
//			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_QUERY_KEY_HISTORY, null, requestJson.optString("language", "ZH"));
//		}
//	}

	/**
	 * 查询搜索条件对应的所有链接
	 * @param requestJson
	 * @return
	 */
//	private JSONObject querySearchViewed(JSONObject requestJson) {
//		JSONObject returnJson = new JSONObject();
//		String keyWord = requestJson.optString("keyWord");
//		
//		if (StringUtils.isEmpty(keyWord) || !requestJson.has("pageNum") || !requestJson.has("currPage")) {
//			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "ZH"));
//		}
//
//		String userId = requestJson.optString("userId");
//		if (StringUtils.isEmpty(userId)) {
//			userId = null;
//		}
//		keyWord = "%"+ keyWord + "%";
//		int pageNum = requestJson.optInt("pageNum");
//		int currPage = requestJson.optInt("currPage");
//		try {
//			int totalNum = searchBusiService.countViewedHistory(userId, keyWord);
//			List<SearchViewed> searchVieweds = searchBusiService.querySearchViewed(userId, keyWord, currPage, pageNum);
//			returnJson.put("viewedHistory", gson.toJson(searchVieweds));
//			returnJson.put("currPage", currPage + 1);
//			returnJson.put("totalNum", totalNum);
//			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, returnJson, requestJson.optString("language", "ZH"));
//		} catch (ServiceException e) {
//			logger.error("查询搜索条件对应的所有链接异常"+e.getMessage());
//			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_QUERY_VIEWED_HISTORY, null, requestJson.optString("language", "ZH"));
//		}
//	}

	/**
	 * 保存搜索记录文件
	 * @param paramJson
	 * @return
	 */
//	private JSONObject saveSearchFile(JSONObject requestJson) {
//
//		if (!requestJson.has("searchFile") || !requestJson.has("fileType")) {
//			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, requestJson.optString("language", "ZH"));
//		}
//		// 获取记录文件
//		try {
//			File searchFile = new File(requestJson.optString("searchFile"));
//			JSONObject readFileObject = FileUtil.readFromFile(searchFile);
//			String code = readFileObject.optString("code");
//			if (StringUtils.equals("-1", code)) {
//				logger.error("读取搜索记录文件异常");
//				return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_FILE_FAILED, null, requestJson.optString("language", "ZH"));
//			} else if (StringUtils.equals("0", code)) {
//				return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, requestJson.optString("language", "ZH"));
//			} else if (StringUtils.equals("1", code)) {
//				String resultStr = readFileObject.optString("result");
//				if (resultStr.indexOf("}]") > -1) {
//					//只有一个json对象
//					if (resultStr.indexOf("}]") == resultStr.lastIndexOf("}]")) {
//						if (StringUtils.equals("searchKey", requestJson.optString("fileType"))) {
//							Type type = new TypeToken<ArrayList<SearchViewed>>(){}.getType();
//							List<SearchKey> searchKeys = gson.fromJson(resultStr, type);
//							searchBusiService.saveSearchKeys(searchKeys);
//							//清空对象
//							searchKeys = null;
//						} else if (StringUtils.equals("searchViewed", requestJson.optString("fileType"))) {
//							Type type = new TypeToken<ArrayList<SearchViewed>>(){}.getType();
//							List<SearchViewed> searchVieweds = gson.fromJson(resultStr, type);
//							searchBusiService.saveSearchVieweds(searchVieweds);
//							//清空对象
//							searchVieweds = null;
//						}
//					}else{
//						//有多个json对象
//						String[] strArray = resultStr.split("}]");
//						for (int i = 0; i < strArray.length; i++) {
//							String str = strArray[i]+"}]";
//							if (StringUtils.equals("searchKey", requestJson.optString("fileType"))) {
//								Type type = new TypeToken<ArrayList<SearchViewed>>(){}.getType();
//								List<SearchKey> searchKeys = gson.fromJson(str, type);
//								searchBusiService.saveSearchKeys(searchKeys);
//								//清空对象
//								searchKeys = null;
//							} else if (StringUtils.equals("searchViewed", requestJson.optString("fileType"))) {
//								Type type = new TypeToken<ArrayList<SearchViewed>>(){}.getType();
//								List<SearchViewed> searchVieweds = gson.fromJson(str, type);
//								searchBusiService.saveSearchVieweds(searchVieweds);
//								//清空对象
//								searchVieweds = null;
//							}
//						}
//					}
//					
//				}
//				//清空字符串
//				resultStr = null;
//			}
//			readFileObject = null;
//			FileUtil.deleteFile(searchFile.getParentFile());
//		} catch (Exception e) {
//			logger.error("保存搜索记录文件" + e.getMessage());
//			return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_SAVE_FILE_FAILED, null, requestJson.optString("language", "ZH"));
//		}
//		
//		return ResultPackaging.dealJsonObject( Constants.RESULT_CODE_SUCCESS, Constants.RESULT_CODE_SUCCESS, null, requestJson.optString("language", "ZH"));
//	}
	
	/**
	 * 安全校验
	 * @param securityJson
	 * @return
	 */
	private JSONObject  securityVerification(JSONObject  securityJson){
		String time = securityJson.getString("time");
		String Md5Str = securityJson.getString("Md5Str");
		String key = ConfigConstants.SECURITY_KEY;
		String newMd5Str = MD5Util.getMD5String(key+time);
		if (!StringUtils.equals(Md5Str, newMd5Str)) {
			return ResultPackaging.dealJsonObject(Constants.RESULT_CODE_FAIL, Constants.RESULT_CODE_LACK_PARAM, null, securityJson.optString("language", "ZH"));
		}
		return  null;
	}
	
	/**
	 * 添加日志
	 * @param param
	 * @return
	 */
	private  SysSearchLog addCallLog(String param, String source){
		try {
			SysSearchLog  log = new SysSearchLog();
			log.setId(UUID.randomUUID().toString().replaceAll("-", ""));
			log.setRequest(param);
			log.setSource(source);
			sysSearchLogMapper.logInsert(log);
			return log;
		} catch (Exception e) {
			logger.error("记录日志异常"+e.getMessage());
		}
		return null;
	}
	
	/**
	 * 更新日志
	 * @param returnJson
	 * @param oldLog
	 * @param paramObject
	 */
	private  void  updateCallLog(JSONObject  returnJson, SysSearchLog  oldLog , JSONObject paramObject){
		try {
			oldLog.setResponse(String.valueOf(returnJson));
			if (null != paramObject) {
				oldLog.setSource(paramObject.getString("source"));
			}
			sysSearchLogMapper.logUpdateByPrimaryKey(oldLog);
		} catch (Exception e) {
			logger.error("更新日志异常"+e.getMessage());
		}
	}

}
