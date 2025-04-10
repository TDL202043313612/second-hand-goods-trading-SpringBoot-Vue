package com.second.hand.trading.server.dao;


import com.second.hand.trading.server.model.UserModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserWXDao {
//    /**
//     * 根据openid查询用户
//     * @param openid
//     * @return
//     */
//    @Select("select * from user where openid = #{openid}")
//    User getByOpenid(String openid);

//    /**
//     * 插入数据
//     * @param user
//     */
//    void insert(User user);

    UserModel getWXUserToOpenid(@Param("wxOpenid") String wxOpenid);
    /**
     * 插入微信用户数据
     * @param userModel
     */
    void insertWXUser(UserModel userModel);

    /**
     * 更新微信用户数据
     * @param userModel
     */
    void updateWXUser(UserModel userModel);

}
