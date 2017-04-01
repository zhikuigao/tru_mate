package com.jwis.service.curater;

import java.io.File;

public class ZookeeperServiceAutoRegist{
	/**
	 * 使用子线程启动自动注册客户端，防止主线程被阻塞
	 * @param file
	 */
	public void registServices(File file){
		Thread thread = new Thread(new ZookeeperRegistTask(file));
		thread.start();
	};
}
