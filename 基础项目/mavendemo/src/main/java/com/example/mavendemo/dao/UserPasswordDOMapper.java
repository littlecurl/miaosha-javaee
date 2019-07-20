package com.example.mavendemo.dao;

import com.example.mavendemo.dataobject.UserPasswordDO;

public interface UserPasswordDOMapper {
    int deleteByPrimaryKey(Integer id);
    int insert(UserPasswordDO record);
    int insertSelective(UserPasswordDO record);


    UserPasswordDO selectByPrimaryKey(Integer id);
    UserPasswordDO selectByUserId(Integer id);


    int updateByPrimaryKeySelective(UserPasswordDO record);
    int updateByPrimaryKey(UserPasswordDO record);
}