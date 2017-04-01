package com.jws.app.operater.model;

import java.util.Date;

public class AppType {
    private Integer id;

    private String type;

    private String secondType;

    private Date createTime;

    private Date updateTime;

    private String industryTag;

    private String professionalTag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getSecondType() {
        return secondType;
    }

    public void setSecondType(String secondType) {
        this.secondType = secondType == null ? null : secondType.trim();
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

    public String getIndustryTag() {
        return industryTag;
    }

    public void setIndustryTag(String industryTag) {
        this.industryTag = industryTag == null ? null : industryTag.trim();
    }

    public String getProfessionalTag() {
        return professionalTag;
    }

    public void setProfessionalTag(String professionalTag) {
        this.professionalTag = professionalTag == null ? null : professionalTag.trim();
    }
}