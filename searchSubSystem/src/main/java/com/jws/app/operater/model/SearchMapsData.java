package com.jws.app.operater.model;

import java.util.Date;
import java.util.List;

public class SearchMapsData {
    private Integer id;

    private String keywordId;

    private String title;

    private String url;

    private Date createTime;
    
    private String keepFlag;
    
    private List<SearchMapsKeywordNode> dataNode;
    
    private Date flagTime;

	public List<SearchMapsKeywordNode> getDataNode() {
		return dataNode;
	}

	public void setDataNode(List<SearchMapsKeywordNode> dataNode) {
		this.dataNode = dataNode;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKeywordId() {
        return keywordId;
    }

    public void setKeywordId(String keywordId) {
        this.keywordId = keywordId == null ? null : keywordId.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
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

	public String getKeepFlag() {
		return keepFlag;
	}

	public void setKeepFlag(String keepFlag) {
		this.keepFlag = keepFlag;
	}

	public Date getFlagTime() {
		return flagTime;
	}

	public void setFlagTime(Date flagTime) {
		this.flagTime = flagTime;
	}
}