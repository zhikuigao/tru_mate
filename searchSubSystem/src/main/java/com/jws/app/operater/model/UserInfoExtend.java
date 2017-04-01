package com.jws.app.operater.model;

import java.util.Date;

public class UserInfoExtend {
    private Integer id;

    private String userId;

    private String versionPc;

    private String versionAndroid;

    private String versionIos;

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

    public String getVersionPc() {
        return versionPc;
    }

    public void setVersionPc(String versionPc) {
        this.versionPc = versionPc == null ? null : versionPc.trim();
    }

    public String getVersionAndroid() {
        return versionAndroid;
    }

    public void setVersionAndroid(String versionAndroid) {
        this.versionAndroid = versionAndroid == null ? null : versionAndroid.trim();
    }

    public String getVersionIos() {
        return versionIos;
    }

    public void setVersionIos(String versionIos) {
        this.versionIos = versionIos == null ? null : versionIos.trim();
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