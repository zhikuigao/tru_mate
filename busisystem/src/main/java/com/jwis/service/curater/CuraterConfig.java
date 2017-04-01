package com.jwis.service.curater;

import java.util.List;

public class CuraterConfig {
	private String zooKeeperAddress;
	private List<ZooKeeperService> serverList;
	public String getZooKeeperAddress() {
		return zooKeeperAddress;
	}
	public void setZooKeeperAddress(String zooKeeperAddress) {
		this.zooKeeperAddress = zooKeeperAddress;
	}
	public List<ZooKeeperService> getServerList() {
		return serverList;
	}
	public void setServerList(List<ZooKeeperService> serverList) {
		this.serverList = serverList;
	}
}
