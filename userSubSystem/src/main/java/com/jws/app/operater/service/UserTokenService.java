package com.jws.app.operater.service;

public interface UserTokenService {
	/**
	 * 
	 * @param userId
	 * @param device 设备
	 * @return
	 */
	public String addToken(String userId, String device);
	
}
