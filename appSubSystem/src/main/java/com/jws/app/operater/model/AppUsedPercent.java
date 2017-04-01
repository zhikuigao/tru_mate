package com.jws.app.operater.model;

import java.util.Date;

public class AppUsedPercent implements Comparable<AppUsedPercent>{
    private Integer id;

    private String userId;

    private String type;

    private String appName;

    private Date dayUse;

    private Float duration;
    
    private Float totalDuration;

    private Integer usePercent;

    private Date createTime;

    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public Date getDayUse() {
        return dayUse;
    }

    public void setDayUse(Date dayUse) {
        this.dayUse = dayUse;
    }

    public Float getDuration() {
        return duration;
    }

    public void setDuration(Float duration) {
        this.duration = duration;
    }

    public Integer getUsePercent() {
        return usePercent;
    }

    public void setUsePercent(Integer usePercent) {
        this.usePercent = usePercent ;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public Float getTotalDuration() {
		return totalDuration;
	}

	public void setTotalDuration(Float totalDuration) {
		this.totalDuration = totalDuration;
	}

	@Override
	public int compareTo(AppUsedPercent o) {
		return o.getDuration().compareTo(this.duration);
	}
}