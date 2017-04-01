package com.jws.app.operater.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.jws.app.operater.data.AppUsedPercentMapper;
import com.jws.app.operater.model.AppUsedPercent;
import com.jws.app.operater.model.AppUsedYesterday;
import com.jws.app.operater.service.CalculationSubService;
import com.jws.common.constant.ConfigConstants;
@Service("calculationSubService")
public class CalculationSubServiceImpl implements CalculationSubService{
	private final Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private AppUsedPercentMapper appUsedPercentMapper;

	@SuppressWarnings("unchecked")
	@Override
	public void calculationDurationPercent(String start, String end, String type) {
//		System.out.println("start="+start);
//		System.out.println("end="+end);
//		System.out.println("type="+type);
		//1.查询应用使用时长原始表
		List<AppUsedYesterday> list = appUsedPercentMapper.queryAppUsedYes(start, end);
		HashMap<String, Object> mapList = new HashMap<>();
		//根据userId组装集合mapList
		if (null != list && list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				AppUsedYesterday yes = list.get(i);
				if (null == yes.getDuration() || 0==yes.getDuration()) {
					continue;
				}
				Float durationF = convertToHour(yes.getDuration());
				if (durationF<1F) {
					continue;
				}
				if (yes.getAppName().indexOf("exe")>-1 || yes.getAppName().indexOf("EXE")>-1) {
					yes.setAppName(yes.getAppName().substring(0, yes.getAppName().length()-4));
				}
				if (mapList.containsKey(yes.getUserId())) {
					List<AppUsedPercent> exitList=(List<AppUsedPercent>) mapList.get(yes.getUserId());
					//计算时长，秒转成小时
					AppUsedPercent percent = new AppUsedPercent();
					percent.setDuration(durationF);
					//转换对象
					percent = convertToPercent(yes, percent, type);
					//list中的第一个对象用来统计总时长
					exitList.get(0).setTotalDuration(addFloat(exitList.get(0).getTotalDuration(),durationF,1));
					if (exitList.size()<ConfigConstants.TIME_SHOW_NUMBER) {
						exitList.add(percent);
					}else if(exitList.size()==ConfigConstants.TIME_SHOW_NUMBER){
						percent.setAppName("other");
						exitList.add(percent);
					}else{
						for (int j = exitList.size()-1; j > -1; j--) {
							if (StringUtils.equals(exitList.get(j).getAppName(), "other")) {
								exitList.get(j).setDuration(addFloat(exitList.get(j).getDuration(),durationF,1));
							}
						}
					}
				}else{
					List<AppUsedPercent> useList= new ArrayList<>();
					AppUsedPercent percent = new AppUsedPercent();
					percent.setDuration(durationF);
					percent.setTotalDuration(durationF);
					//转换对象
					percent = convertToPercent(yes, percent, type);
					useList.add(percent);
					mapList.put(yes.getUserId(), useList);
				}
			}
			//释放list
			list = null;
		}
		//计算百分比
		List<AppUsedPercent> allList = new ArrayList<>();
		if (mapList.size()>0) {
			for(Map.Entry<String, Object> entry : mapList.entrySet()) {
				List<AppUsedPercent> percentList = (List<AppUsedPercent>) entry.getValue();
				int total = 0;
				for (int i = 0; i < percentList.size(); i++) {
					AppUsedPercent percent = percentList.get(i);
					try {
						//获取百分比，取整数部分
						if (i<percentList.size()-1) {
							Float f = percent.getDuration();
							Float t = percentList.get(0).getTotalDuration();
							percent.setUsePercent((int)Math.round(f/t*100));
							total+=(int)Math.round(f/t*100);
						}else{
							percent.setUsePercent(100-total);
						}						
					} catch (Exception e) {
						logger.error("计算使用时长百分比异常："+e.getMessage());
						continue;
					}
				}
				allList.addAll(percentList);
			}
			//释放mapList
			mapList = null;
		}
		//批量入表
		batchInsertPercent(allList);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void calculationDurationMore(String start, String end, String type) {
//		System.out.println("start="+start);
//		System.out.println("end="+end);
//		System.out.println("type="+type);
		//1.查询应用使用时长原始表
		List<AppUsedYesterday> list = appUsedPercentMapper.queryAppUsedYes(start, end);
		int size = list.size();
		HashMap<String, Object> mapList = new HashMap<>();
		if (size>0) {
			for (int i = 0; i < size; i++) {
				AppUsedYesterday yes = list.get(i);
				//过滤掉无意义的数据
				if (null == yes.getDuration() || 0==yes.getDuration()) {
					continue;
				}
				Float durationF = convertToHour(yes.getDuration());
				if (durationF<1F) {
					continue;
				}
//				System.out.println("durationF="+durationF);
				if (yes.getAppName().indexOf("exe")>-1 || yes.getAppName().indexOf("EXE")>-1) {
					yes.setAppName(yes.getAppName().substring(0, yes.getAppName().length()-4));
				}
				//根据userId组装到map
				if (mapList.containsKey(yes.getUserId())) {
					List<AppUsedPercent> exitList=(List<AppUsedPercent>) mapList.get(yes.getUserId());
					Boolean flag = true;
					//将相同的应用使用时长叠加起来
					for (int j = 0; j < exitList.size(); j++) {
						if (StringUtils.equals(yes.getAppName(), exitList.get(j).getAppName())) {
							Float add = addFloat(durationF,exitList.get(j).getDuration(),1);
//							System.out.println("preAdd="+exitList.get(j).getDuration());
//							System.out.println("add="+add);
							exitList.get(j).setDuration(add);
							flag = false;
							break;
						}
					}
					//如果没有找到相同的，直接add到List内
					if (flag) {
						AppUsedPercent percent = new AppUsedPercent();
						percent.setDuration(durationF);
						//转换对象
						percent = convertToPercent(yes, percent, type);
						exitList.add(percent);
					}
				}else{
					List<AppUsedPercent> useList= new ArrayList<>();
					AppUsedPercent percent = new AppUsedPercent();
					percent.setDuration(durationF);
					//转换对象
					percent = convertToPercent(yes, percent, type);
					useList.add(percent);
					mapList.put(yes.getUserId(), useList);
				}
			}
		}
		List<AppUsedPercent> allList = new ArrayList<>();
		if (mapList.size()>0) {
			for(Map.Entry<String, Object> entry : mapList.entrySet()) {
				//获取每个userid下的应用list
				List<AppUsedPercent> percentList = (List<AppUsedPercent>) entry.getValue();
				//根据使用时长排序
				Collections.sort(percentList);
				//计算总时长
				Float totalDuration = 0F;
				Float preConf = 0F;
				int perSize = percentList.size();
				for (int i = 0; i < perSize; i++) {
					if (perSize>ConfigConstants.TIME_SHOW_NUMBER && i<ConfigConstants.TIME_SHOW_NUMBER) {
						preConf=addFloat(preConf,percentList.get(i).getDuration(),1);
					}
					totalDuration=addFloat(totalDuration,percentList.get(i).getDuration(),1);
				}
//				System.out.println("totalDuration="+totalDuration);
				//计算百分比
				int initPer = 0;
				for (int i = 0; i <perSize; i++) {
					AppUsedPercent per = percentList.get(i);
					int every = (int)Math.round(per.getDuration()/totalDuration*100);
//					System.out.println("every="+every);
					if (perSize>ConfigConstants.TIME_SHOW_NUMBER) {
						if (i<ConfigConstants.TIME_SHOW_NUMBER) {
							per.setUsePercent(every);
							initPer+=every;
//							System.out.println("initPer="+initPer);
						}
						if (i==ConfigConstants.TIME_SHOW_NUMBER) {
							per.setAppName("other");
							per.setDuration(addFloat(totalDuration,preConf,2));
							per.setUsePercent(100-initPer);
							allList.addAll(percentList.subList(0, ConfigConstants.TIME_SHOW_NUMBER+1));
							break;
						}
					}else{
						if (i==perSize-1) {
							per.setUsePercent(100-initPer);
							allList.addAll(percentList);
						}else{
							per.setUsePercent(every);
							initPer+=every;
						}
					}
				}
			}
		}
		//批量入表
		batchInsertPercent(allList);
	}
	
	//批量插入
	private void batchInsertPercent(List<AppUsedPercent> allList){
		int total = allList.size();
		if (total>0) {
			try {
				int page = total/ConfigConstants.BATCH_INSERT;
				if (page>0) {
					for (int i = 0; i < page; i++) {
						appUsedPercentMapper.batchInsertAppUsedPercent(allList.subList(ConfigConstants.BATCH_INSERT*i, ConfigConstants.BATCH_INSERT*(i+1)));
					}
				}
				int reste = total%ConfigConstants.BATCH_INSERT;
				if (reste>0) {
					appUsedPercentMapper.batchInsertAppUsedPercent(allList.subList(ConfigConstants.BATCH_INSERT*page, total));
				}	
			} catch (Exception e) {
				logger.error("批量插入数据库异常："+e.getMessage());
				allList = null;
			}
			allList = null;		
		}
	}

	/**
	 * 转换对象
	 * @param yes
	 * @param percent
	 * @param type
	 * @return
	 */
	private AppUsedPercent convertToPercent(AppUsedYesterday yes, AppUsedPercent percent, String type){
		percent.setAppName(yes.getAppName());
		percent.setDayUse(yes.getDayUse());
		percent.setType(type);
		percent.setUserId(yes.getUserId());
		return percent;
	}
	/**
	 * 转换成小时
	 * @param sencond
	 * @return
	 */
	private Float convertToHour(Integer sencond){
		Float sec = Float.valueOf(sencond.toString());
		Float duration = sec/60F/60F;
		Float durationF =Math.round(duration*10)/10F;
		return durationF;
	}
	/**
	 * float 类型运算
	 * @param f1
	 * @param f2
	 * @param type
	 * @return
	 */
	private Float addFloat(Float f1, Float f2, int type){
		if (type == 1) {
			return Math.round((f1+f2)*10)/10F;
		}else if (type == 2) {
			return Math.round((f1-f2)*10)/10F;
		}
		return null;
	}
	

}
