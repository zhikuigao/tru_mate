package com.jws.app.operater.data;

import com.jws.app.operater.model.UserLogin;

public interface UserLoginMapper {
    UserLogin queryLoginInfo(UserLogin record);
}