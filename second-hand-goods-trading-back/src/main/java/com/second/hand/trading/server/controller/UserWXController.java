package com.second.hand.trading.server.controller;

import com.second.hand.trading.server.model.UserModel;
import com.second.hand.trading.server.service.UserService;
import com.second.hand.trading.server.service.UserWXService;
import com.second.hand.trading.server.utils.JWTUtils;
import com.second.hand.trading.server.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static com.second.hand.trading.server.enums.ErrorMsg.FAIL_WX_TOKEN;

@CrossOrigin
@RestController
@RequestMapping("/wxuser")
public class UserWXController {
    @Autowired
    private UserWXService userWXService;
    @Autowired
    private UserService userService;
    /**
     * 微信登录
     * @param userModel
     * @return
     */
    @PostMapping("/wxlogin")
    public ResultVo login(@RequestBody UserModel userModel,
                            HttpServletResponse response){
        System.out.println("userModel" + userModel);
//        System.out.println("userModel.getWxOpenid()" + userModel.getWxOpenid());
        userModel.setSignInTime(new Timestamp(System.currentTimeMillis()));
        if (userModel.getAvatar() == null || "".equals(userModel.getAvatar())) {
            userModel.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
        }
        try {
            String openid = userWXService.getWeChatOpenId(userModel.getCode());
            userModel.setWxOpenid(openid);
            UserModel userModel_ = userWXService.getWeChatUser(userModel);
            // 3. 生成JWT Token
            String token = JWTUtils.generateToken(openid);
            System.out.println("userModel_:"+userModel_);
            userModel_.setToken(token);
            Cookie cookie = new Cookie("shUserId", String.valueOf(userModel_.getId()));
//        cookie.setMaxAge(60 * 60 * 24 * 30);
            cookie.setPath("/");
            cookie.setHttpOnly(false);
            response.addCookie(cookie);
            return ResultVo.success(userModel_);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResultVo.fail(FAIL_WX_TOKEN);
    }

}
