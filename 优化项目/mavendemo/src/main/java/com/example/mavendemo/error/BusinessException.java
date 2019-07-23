package com.example.mavendemo.error;

/**
 * 设计模式：包装器业务异常类实现
 * 精髓在于定义接口，接口中定义set方法，实现类使用枚举实例调用set方法可以实现自定义异常
 *
 * 使用的位置：UserController.java
 */
public class BusinessException extends Exception implements CommonError {

    private CommonError commonError;

    // 异常构造方法，接收枚举类型实例，用于构造业务异常
    public BusinessException(CommonError commonError){
        super(); //千万记住要调用super()方法，理由就是对Exception会有一些自身初始化的机制在里面
        this.commonError = commonError;
    }
    // 异常构造方法，接收枚举类型实例和异常信息，用于自定义业务异常
    public BusinessException(CommonError commonError,String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrCode() {
        return this.commonError.getErrCode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}
