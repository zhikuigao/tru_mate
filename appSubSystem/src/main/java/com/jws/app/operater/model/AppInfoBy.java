package com.jws.app.operater.model;

import java.util.HashMap;

public class AppInfoBy implements Comparable<AppInfoBy>{
	private HashMap<String, Object> app;
    private Float simValue;
    
    public AppInfoBy(HashMap<String, Object> app,Float simValue) {
		this.app = app;
		this.simValue = simValue;
	}

	public HashMap<String, Object> getApp() {
		return app;
	}

	public void setApp(HashMap<String, Object> app) {
		this.app = app;
	}

	public Float getSimValue() {
		return simValue;
	}

	public void setSimValue(Float simValue) {
		this.simValue = simValue;
	}
    
	@Override
	public int compareTo(AppInfoBy o) {
		return o.getSimValue().compareTo(this.simValue);
	}
}