package com.second.hand.trading.server.service;

import com.second.hand.trading.server.model.UserModel;

public interface UserWXService {
    /**
     * 微信登录接口
     * @param code
     * @return
     */
    public String getWeChatOpenId(String code) throws Exception;


    /**
     * 获取用户信息
     * @param userModel
     * @return
     */
    public UserModel getWeChatUser(UserModel userModel);


}
