package com.second.hand.trading.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.second.hand.trading.server.dao.UserWXDao;
import com.second.hand.trading.server.model.UserModel;
import com.second.hand.trading.server.service.UserWXService;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
@Service
public class UserWXServiceImpl implements UserWXService {

    //微信服务接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Value("${wx.appid}")
    private String wxAppId;

    @Value("${wx.secret}")
    private String wxSecret;

    @Resource
    private UserWXDao userWXDao;


    public String getWeChatOpenId(String code) throws Exception {
        String url = String.format(
        "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                wxAppId, wxSecret, code
        );
//        System.out.println("请求URL: " + url); // 打印完整URL
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(url);
        String response = EntityUtils.toString(client.execute(request).getEntity());
        JSONObject json = JSONObject.parseObject(response);
//        System.out.println("response: "+response);
//        System.out.println("openid: "+json.getString("openid"));
        return json.getString("openid"); // 可能包含session_key
    }


    public UserModel getWeChatUser(UserModel userModel) {
        UserModel userModel_=userWXDao.getWXUserToOpenid(userModel.getWxOpenid());
        if (userModel_ == null){
            userWXDao.insertWXUser(userModel);
            userModel_=userWXDao.getWXUserToOpenid(userModel.getWxOpenid());
        }else {
            System.out.println("---------------");
            userWXDao.updateWXUser(userModel);
            userModel_=userWXDao.getWXUserToOpenid(userModel.getWxOpenid());
        }
        System.out.println("getWeChatUser: "+userModel_);
        return userModel_;
    }


}
