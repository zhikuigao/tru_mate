package com.jws.app.operater.model;

import java.util.Date;

public class SearchMapRecommend implements Comparable<SearchMapRecommend>{
	
	private String id;
	
	private String firstKeyword;
	
	private String keywordId;
	
	private Date createTime;
	
	private Integer dataCount;//关键字下被标记的文章数量

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstKeyword() {
		return firstKeyword;
	}

	public void setFirstKeyword(String firstKeyword) {
		this.firstKeyword = firstKeyword;
	}


	public String getKeywordId() {
		return keywordId;
	}

	public void setKeywordId(String keywordId) {
		this.keywordId = keywordId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getDataCount() {
		return dataCount;
	}

	public void setDataCount(Integer dataCount) {
		this.dataCount = dataCount;
	}

	@Override
	public int compareTo(SearchMapRecommend o) {
		return o.getDataCount()-this.dataCount;
	}
	
}
