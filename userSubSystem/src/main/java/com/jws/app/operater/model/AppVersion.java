package com.jws.app.operater.model;

import java.util.Date;

public class AppVersion extends Pagination{
    private String id;

    private String appName;

    private String versionNum;
    
    private Integer versionSerial;

    private String useType;

    private String fileType;
    
    private String updateFlag;

    private String fileUrl;

    private String status;

    private String creater;

    private String updater;

    private Date createTime;

    private Date updateTime;
    
    private String monitorStatus;
    
    private String versionDescZh;
    
    private String versionDescImgZh;

    private String versionDescEn;
    
    private String versionDescImgEn;

    public String getVersionDescZh() {
        return versionDescZh;
    }

    public void setVersionDescZh(String versionDescZh) {
        this.versionDescZh = versionDescZh == null ? null : versionDescZh.trim();
    }

    public String getVersionDescEn() {
        return versionDescEn;
    }

    public void setVersionDescEn(String versionDescEn) {
        this.versionDescEn = versionDescEn == null ? null : versionDescEn.trim();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum == null ? null : versionNum.trim();
    }

    public String getUseType() {
        return useType;
    }

    public void setUseType(String useType) {
        this.useType = useType == null ? null : useType.trim();
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl == null ? null : fileUrl.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater == null ? null : creater.trim();
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater == null ? null : updater.trim();
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

	public Integer getVersionSerial() {
		return versionSerial;
	}

	public void setVersionSerial(Integer versionSerial) {
		this.versionSerial = versionSerial;
	}

	public String getUpdateFlag() {
		return updateFlag;
	}

	public void setUpdateFlag(String updateFlag) {
		this.updateFlag = updateFlag;
	}

	public String getMonitorStatus() {
		return monitorStatus;
	}

	public void setMonitorStatus(String monitorStatus) {
		this.monitorStatus = monitorStatus;
	}

	public String getVersionDescImgZh() {
		return versionDescImgZh;
	}

	public void setVersionDescImgZh(String versionDescImgZh) {
		this.versionDescImgZh = versionDescImgZh;
	}

	public String getVersionDescImgEn() {
		return versionDescImgEn;
	}

	public void setVersionDescImgEn(String versionDescImgEn) {
		this.versionDescImgEn = versionDescImgEn;
	}
    
}