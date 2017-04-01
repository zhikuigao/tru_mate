package com.jws.app.operater.serviceBlock.user.service;

public interface UserTokenService {
	
	public Boolean validToken(String token, String device);
	
}
