package com.jws.app.operater.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class SearchMapShare extends Pagination{
    private Integer id;

    private String userId;

    private String mapId;

    private String firstKeyword;

    private String shareDevice;

    private String shareTo;

    private String thumbnailUrl;
    
    private String picUrl;

    private Date createTime;

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

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId == null ? null : mapId.trim();
    }

    public String getFirstKeyword() {
        return firstKeyword;
    }

    public void setFirstKeyword(String firstKeyword) {
        this.firstKeyword = firstKeyword == null ? null : firstKeyword.trim();
    }

    public String getShareDevice() {
        return shareDevice;
    }

    public void setShareDevice(String shareDevice) {
        this.shareDevice = shareDevice == null ? null : shareDevice.trim();
    }

    public String getShareTo() {
        return shareTo;
    }

    public void setShareTo(String shareTo) {
        this.shareTo = shareTo == null ? null : shareTo.trim();
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl == null ? null : thumbnailUrl.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
}