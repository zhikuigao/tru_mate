package com.jws.app.operater.data;

import com.jws.app.operater.model.EmailVerify;

public interface EmailVerifyMapper {

    int insert(EmailVerify record);
    /**
     * 根据邮箱，查询最新的验证码信息
     * @param email
     * @return
     */
    EmailVerify  queryNewestVerifyByEmail(String email);
}