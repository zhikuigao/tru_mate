package com.jws.app.operater.data;

import com.jws.app.operater.model.UserLogin;

public interface UserLoginMapper {

    int insert(UserLogin record);

    int updateLogin(UserLogin record);
}