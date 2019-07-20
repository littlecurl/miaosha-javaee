package com.example.mavendemo.controller;

import com.example.mavendemo.error.BusinessException;
import com.example.mavendemo.error.EmBusinessError;
import com.example.mavendemo.response.CommonReturnType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    public static final String CONTENT_TYPE_FORMED="application/x-www-form-urlencoded";

    /*
        // 第一版代码
        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.OK)
        @ResponseBody
        public Object handlerException(HttpServletRequest request, Exception ex){
            BusinessException businessException = (BusinessException) ex;
            CommonReturnType commonReturnType = new CommonReturnType();
            commonReturnType.setStatus("fail");
            Map<String,Object> responseData = new HashMap<>();
            responseData.put("errCode",businessException.getErrCode());
            responseData.put("errMsg",businessException.getErrMsg());
            commonReturnType.setData(responseData);
            return commonReturnType;
        }
*/

/*
    // 第二版代码，对上面代码的美化（如果抛出的异常不是BusinessException，即：不是已知的异常）
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex) {
        if(ex instanceof BusinessException){
            BusinessException businessException = (BusinessException) ex;
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
            return CommonReturnType.create(responseData,"fail");
        }else{
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
            return CommonReturnType.create(responseData,"fail");
        }
    }
*/

    // 第三版代码，再次对上面的代码进行美化（提取出公共的代码，达到最终的效果）
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public Object handlerException(HttpServletRequest request, Exception ex) {
        Map<String, Object> responseData = new HashMap<>();
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;
            responseData.put("errCode", businessException.getErrCode());
            responseData.put("errMsg", businessException.getErrMsg());
        } else {
            responseData.put("errCode", EmBusinessError.UNKNOWN_ERROR.getErrCode());
            responseData.put("errMsg", EmBusinessError.UNKNOWN_ERROR.getErrMsg());
        }
        return CommonReturnType.create(responseData, "fail");
    }

}
