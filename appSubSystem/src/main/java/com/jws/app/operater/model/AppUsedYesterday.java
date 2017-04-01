package com.jws.app.operater.model;

import java.util.Date;

public class AppUsedYesterday {
    private Integer id;

    private String userId;

    private String appName;

    private Integer duration;
    
    private Integer hz;

    private Date dayUse;

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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getHz() {
        return hz;
    }

    public void setHz(Integer hz) {
        this.hz = hz;
    }

    public Date getDayUse() {
        return dayUse;
    }

    public void setDayUse(Date dayUse) {
        this.dayUse = dayUse;
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

}