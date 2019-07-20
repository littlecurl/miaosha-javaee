package com.example.mavendemo.service.impl;

import com.example.mavendemo.dao.UserDOMapper;
import com.example.mavendemo.dao.UserPasswordDOMapper;
import com.example.mavendemo.dataobject.UserDO;
import com.example.mavendemo.dataobject.UserPasswordDO;
import com.example.mavendemo.error.BusinessException;
import com.example.mavendemo.error.EmBusinessError;
import com.example.mavendemo.service.UserService;
import com.example.mavendemo.service.model.UserModel;
import com.example.mavendemo.validator.ValidationResult;
import com.example.mavendemo.validator.ValidatorImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private UserPasswordDOMapper userPasswordDOMapper;
    @Autowired
    private ValidatorImpl validator;


    @Override
    public UserModel getUserById(Integer id){
        // 用户信息用主键进行查询
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        if (userDO == null){
            return null;
        }
        // 密码用查询出来的用户的UserId进行查询
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        return convertFromDataObject(userDO,userPasswordDO);
    }

    // 注册
    @Override
    @Transactional
    public void register(UserModel userModel)throws BusinessException {
        // 先判空处理
        if(userModel == null){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        // 继续判空处理
        // StringUtils类来自Apache Common Lang包
//        if(StringUtils.isEmpty(userModel.getName())
//            || userModel.getGender() == null
//            || userModel.getAge() == null
//            || StringUtils.isEmpty(userModel.getTelphone())){
//            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
//        }
        // 优化后的校验规则
        ValidationResult result = validator.validate(userModel);
        if(result.isHasError()){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }


        // 如果入参合法，则开始将Model转为DO,进行持久化数据
        // 这里的insertSelective(..)方法在Mapper中，是一个比单纯的insert(..)方法优秀的方式
        // 优秀在：如果是用户传来空数据，就不会insert而是使用数据库默认值
        // 所以这里老师又一次强调了数据库设计中一些细节的重要性
        UserDO userDO = convertFromModel(userModel);
        // 如果手机号重复插入，就会在这里报异常
        try {
            userDOMapper.insertSelective(userDO);
        }catch(DuplicateKeyException ex){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"手机号重复注册");
        }
        // 从user_info中取出id号，设置到userModel，再通过下面的convertPasswordFromModel转发给user_password的user_id
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertPasswordFromModel(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
        return;
    }

    // 登录
    @Override
    public UserModel validateLogin(String telphone, String encryptPassword) throws BusinessException {
        // 通过手机号获取用户密码
        // 先获取userDO
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if(userDO == null){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        // 在获取userPasswordDO
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());
        // 组装成userModel
        UserModel userModel = convertFromDataObject(userDO,userPasswordDO);
        // 比对传进来的密码和数据库中的密码是否相等
        if(!StringUtils.equals(encryptPassword,userModel.getEncryptPassword())){
            throw new BusinessException(EmBusinessError.USER_LOGIN_FAIL);
        }
        // 密码相等，返回登录成功的用户信息给Controller层
        return userModel;
    }

    // Model ---> DO
    private UserDO convertFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel,userDO);
        return userDO;
    }

    // Model ---> PasswordDO
    private UserPasswordDO convertPasswordFromModel(UserModel userModel){
        if(userModel == null){
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setEncryptPassword(userModel.getEncryptPassword());
        userPasswordDO.setUserId(userModel.getId());
        return userPasswordDO;
    }

    // 两个DO ---> 一个Model
    // 自定义方法，供其它方法做返回值使用，整合两张表的模型数据
    private UserModel convertFromDataObject(UserDO userDO,UserPasswordDO userPasswordDO){
        if (userDO == null){
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO,userModel);
        if (userPasswordDO != null) {
            userModel.setEncryptPassword(userPasswordDO.getEncryptPassword());
        }
        return  userModel;
    }
}
