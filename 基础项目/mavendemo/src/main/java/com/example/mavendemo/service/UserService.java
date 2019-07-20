package com.example.mavendemo.service;

import com.example.mavendemo.error.BusinessException;
import com.example.mavendemo.service.model.UserModel;

public interface UserService {
    UserModel getUserById(Integer id);
    void register(UserModel userModel) throws BusinessException;

    /**
     * 参数解释
     * @param telphone 用户注册手机
     * @param encryptPassword Controller层会对用户传进来的密码进行加密，然后作为参数，调用Service层
     * @throws BusinessException
     */
    UserModel validateLogin(String telphone,String encryptPassword) throws BusinessException;
}
