package com.example.mavendemo.error;

/**
 * 枚举类型 实现 CommonError 接口的三个方法
 */
public enum EmBusinessError implements CommonError {
    // 声明枚举实例，同时传入构造参数，相当于构造对象
    // 通用错误类型10001（这里不能使用0开头，比如0001，否则int类型会自动转为个位数。
    PARAMETER_VALIDATION_ERROR(10001,"参数不合法"),
    // 未知错误类型10002
    UNKNOWN_ERROR(10002,"未知错误"),
    // 用户相关错误类型2000*
    USER_NOT_EXIST(20001,"用户不存在"),
    USER_LOGIN_FAIL(20002,"手机号或密码错误"),

    USER_NOT_LOGIN(20003,"用户还未登录"),

    // 交易信息错误3000*
    STOCK_NOT_ENOUGH(30001,"库存不足")
    ;// 记住要用分号结束

    private int errCode;
    private String errMsg;
    // enum的私有构造方法，防止被外部调用
    private EmBusinessError(int errCode,String errMsg){
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    @Override
    public int getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }

}
