package com.jws.app.operater.model;

import java.util.Date;
import java.util.List;

public class SearchMaps {
    private String id;

    private String userId;

    private String firstKeyword;

    private String mapPcUrl;

    private String mapMobileUrl;
    
    private String barCodeUrl;
    
    private List<String> label;





	public List<String> getLabel() {
		return label;
	}

	public void setLabel(List<String> label) {
		this.label = label;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	private Date createTime;
    
    private String source;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getFirstKeyword() {
        return firstKeyword;
    }

    public void setFirstKeyword(String firstKeyword) {
        this.firstKeyword = firstKeyword == null ? null : firstKeyword.trim();
    }

    public String getMapPcUrl() {
        return mapPcUrl;
    }

    public void setMapPcUrl(String mapPcUrl) {
        this.mapPcUrl = mapPcUrl == null ? null : mapPcUrl.trim();
    }

    public String getMapMobileUrl() {
        return mapMobileUrl;
    }

    public void setMapMobileUrl(String mapMobileUrl) {
        this.mapMobileUrl = mapMobileUrl == null ? null : mapMobileUrl.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getBarCodeUrl() {
		return barCodeUrl;
	}

	public void setBarCodeUrl(String barCodeUrl) {
		this.barCodeUrl = barCodeUrl;
	}
}