package com.jws.common.task;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.jwis.service.curater.ZookeeperServiceAutoRegist;
import com.jws.common.constant.ConfigConstants;
public class TimerTaskManager  implements  ServletContextListener {
	private final Logger logger = Logger.getLogger(this.getClass());
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		try {
		//	String rootPath = TimerTaskManager.class.getClass().getResource("/").getFile().toString();
	    	String newPath = ConfigConstants.zookeeper_URL   + "service.json";
	    	File file = new File(newPath);
	    	if(file.exists()){
	    		ZookeeperServiceAutoRegist regist = new ZookeeperServiceAutoRegist();
	        	regist.registServices(file);
	    	}else
	    		logger.error("service.json does not exist");
		} catch (Exception e) {
			// TODO: handle exception
			logger.error(e);
		}

	
	}

}
