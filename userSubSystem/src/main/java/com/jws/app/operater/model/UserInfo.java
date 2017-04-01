package com.jws.app.operater.model;

import java.util.Date;

public class UserInfo {
    private String id;

    private String isTour;

    private String nickname;
    
    private String sex;
    
    private Date birthday;
    
    private String userPhoto;

	private String industry;

    private String industryId;

    private String professional;

    private String proId;

    private String password;

    private String email;

    private String phone;

    private String qq;

    private String qqToken;

    private String qqOpenId;

    private String sina;

    private String sinaToken;

    private String sinaOpenId;

    private String wchat;

    private String wchatToken;

    private String wchatOpenId;

    private Date createTime;

    private Date updateTime;
    
    private String type;
    
    private String synFlag;
    
    public String getUserPhoto() {
		return userPhoto;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getIsTour() {
        return isTour;
    }

    public void setIsTour(String isTour) {
        this.isTour = isTour == null ? null : isTour.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getIndustryId() {
		return industryId;
	}

	public void setIndustryId(String industryId) {
		this.industryId = industryId;
	}

	public String getProId() {
		return proId;
	}

	public void setProId(String proId) {
		this.proId = proId;
	}

	public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry == null ? null : industry.trim();
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional == null ? null : professional.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getQqToken() {
        return qqToken;
    }

    public void setQqToken(String qqToken) {
        this.qqToken = qqToken == null ? null : qqToken.trim();
    }

    public String getQqOpenId() {
        return qqOpenId;
    }

    public void setQqOpenId(String qqOpenId) {
        this.qqOpenId = qqOpenId == null ? null : qqOpenId.trim();
    }

    public String getSina() {
        return sina;
    }

    public void setSina(String sina) {
        this.sina = sina == null ? null : sina.trim();
    }

    public String getSinaToken() {
        return sinaToken;
    }

    public void setSinaToken(String sinaToken) {
        this.sinaToken = sinaToken == null ? null : sinaToken.trim();
    }

    public String getSinaOpenId() {
        return sinaOpenId;
    }

    public void setSinaOpenId(String sinaOpenId) {
        this.sinaOpenId = sinaOpenId == null ? null : sinaOpenId.trim();
    }

    public String getWchat() {
        return wchat;
    }

    public void setWchat(String wchat) {
        this.wchat = wchat == null ? null : wchat.trim();
    }

    public String getWchatToken() {
        return wchatToken;
    }

    public void setWchatToken(String wchatToken) {
        this.wchatToken = wchatToken == null ? null : wchatToken.trim();
    }

    public String getWchatOpenId() {
        return wchatOpenId;
    }

    public void setWchatOpenId(String wchatOpenId) {
        this.wchatOpenId = wchatOpenId == null ? null : wchatOpenId.trim();
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSynFlag() {
		return synFlag;
	}

	public void setSynFlag(String synFlag) {
		this.synFlag = synFlag;
	}
}