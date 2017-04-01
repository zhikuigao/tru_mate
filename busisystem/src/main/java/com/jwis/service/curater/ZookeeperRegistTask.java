package com.jwis.service.curater;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ZookeeperRegistTask implements Runnable {
	private static Logger logger = Logger.getLogger(ZookeeperRegistTask.class);
	private CuratorFramework client = null;
	private final RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
	private CuraterConfig config = null;
	private final String ipFlag = "<serverip>";
	private final String ipRegExpStr = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                    + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	private final String preServicePath = "/service";
	private final String serversPath = "/servers";
	private final String serverSubPath = "/server-";
	private final String PERSISTENT = "PERSISTENT";
	private final String EPHEMERAL_SEQUENTIAL = "EPHEMERAL_SEQUENTIAL";
	private List<ZookeeperServiceData> serviceDataList = new ArrayList<ZookeeperServiceData>();
	
	private File jsonConfigFile = null;
	public ZookeeperRegistTask(File jsonConfigFile){
		this.jsonConfigFile = jsonConfigFile;
	}
	
	private void initConfig() throws Exception{
		if(jsonConfigFile.isFile() && jsonConfigFile.exists()){
			BufferedReader reader = new BufferedReader(new FileReader(jsonConfigFile));
			String fileString = "";
			StringBuilder configStr = new StringBuilder();
			while((fileString = reader.readLine()) != null){
				configStr.append(fileString);
			}
			if(reader != null)
				reader.close();
			
			JSONObject configObj = JSONObject.fromObject(configStr.toString());
			config = initConfigObject(configObj);
		}
	}
	
	private CuraterConfig initConfigObject(JSONObject configObj){
		CuraterConfig config = null;
		if(!configObj.isNullObject() && !configObj.isEmpty()){
			config = new CuraterConfig();
			config.setZooKeeperAddress(configObj.getString("zookeeper"));
			JSONArray jsonArray = configObj.getJSONArray("serviceList");
			if(jsonArray.isArray() && !jsonArray.isEmpty()){
				List<ZooKeeperService> serviceList = new ArrayList<ZooKeeperService>();
				for(int index = 0; index < jsonArray.size() ; index ++){
					JSONObject serviceObj = jsonArray.getJSONObject(index);
					if(!serviceObj.isNullObject() && !serviceObj.isEmpty()){
						ZooKeeperService service = (ZooKeeperService) JSONObject.toBean(serviceObj, ZooKeeperService.class);
						serviceList.add(service);
					}
				}
				config.setServerList(serviceList);
			}
		}
		return config;
	}
	
	private CuratorFramework initClient(){
		client = CuratorFrameworkFactory.builder().
                connectString(config.getZooKeeperAddress())
                .connectionTimeoutMs(1000)
                .canBeReadOnly(false)
                .retryPolicy(retryPolicy)
                .defaultData(null)
                .build();
		return client;
	}
	
	private void doRegist(List<ZookeeperServiceData> serviceDataList) throws Exception{
		if(serviceDataList != null && serviceDataList.size() > 0){
			for(ZookeeperServiceData serviceData : serviceDataList){
				if(serviceData.getDataType() == PERSISTENT){
					Stat forPath = client.checkExists().forPath(serviceData.getPathStr());
					if(forPath == null)
						client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(serviceData.getPathStr(), serviceData.getDataStr().getBytes());
					else
						client.setData().forPath(serviceData.getPathStr(), serviceData.getDataStr().getBytes());
				} else if(serviceData.getDataType() == EPHEMERAL_SEQUENTIAL){
					client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(serviceData.getPathStr()+serverSubPath, serviceData.getDataStr().getBytes());
				}
			}
		}
	}
	
	public void registServices() throws Exception{
		initConfig();
		if(config != null){
			initClient();
			List<ZooKeeperService> serviceList = config.getServerList();
			if(serviceList != null && serviceList.size() > 0){
				client.start();
				for(ZooKeeperService service : serviceList){
					StringBuilder registServicePath = new StringBuilder(preServicePath);
					registServicePath.append("/").append(service.getServiceName());
					if(!service.getPath().equals("")){
						if(service.getPath().endsWith("/")){
							String endServicePath = service.getPath().substring(0, service.getPath().length() - 1);
							registServicePath.append(endServicePath);
						}
					}
					JSONObject persistentData = new JSONObject();
					persistentData.put("mode", "product");
					ZookeeperServiceData serviceData = new ZookeeperServiceData();
					serviceData.setPathStr(registServicePath.toString());
					serviceData.setDataStr(persistentData.toString());
					serviceData.setDataType(PERSISTENT);
					serviceDataList.add(serviceData);
					JSONObject ephemeralSequential = new JSONObject();
					ZoomkeeperServer server = new ZoomkeeperServer();
					IPUtil ipUtil = new IPUtil();
					server.setIp(isIpIllegal(service.getIp())? ipUtil.getIntranetIpAddress() : service.getIp());
					server.setPath(service.getPath());
					server.setPort(service.getPort());
					ephemeralSequential.put("mode", "product");
					ephemeralSequential.put("product", server);
					serviceData = new ZookeeperServiceData();
					serviceData.setPathStr(registServicePath.toString() + serversPath);
					serviceData.setDataStr(ephemeralSequential.toString());
					serviceData.setDataType(EPHEMERAL_SEQUENTIAL);
					serviceDataList.add(serviceData);
				}
				doRegist(serviceDataList);
			}
			synchronized (this) {
				wait();
			}
		}
	}
	
	/**
	 * 判断IP地址是否是非法的
	 * 非法则返回true,合法则返回false
	 * @return
	 */
	private boolean isIpIllegal(String targetIpAddress){
		boolean isIllegal = false;
		if(targetIpAddress == null || targetIpAddress.equals(""))
			isIllegal = true;
		else if(targetIpAddress.equalsIgnoreCase(ipFlag))
			isIllegal = true;
		else{
			if(!targetIpAddress.matches(ipRegExpStr))
				isIllegal = true;
		}
		return isIllegal;
	}
	
	@Override
	public void run() {
		try {
			registServices();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
}
