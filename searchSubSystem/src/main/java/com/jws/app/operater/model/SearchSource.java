package com.jws.app.operater.model;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class SearchSource {
    private String id;

    private String name;

    private String url;
    
    private String flag;
    
    private String deleteFlag;

    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public String getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		if (null == flag || StringUtils.isEmpty(flag)) {
			this.flag = "0";
		}else {
			this.flag = "1";
		}
		
	}
}