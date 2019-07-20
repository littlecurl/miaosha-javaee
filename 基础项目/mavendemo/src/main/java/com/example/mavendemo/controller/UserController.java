package com.example.mavendemo.controller;

import com.example.mavendemo.controller.viewobject.UserVO;
import com.example.mavendemo.error.BusinessException;
import com.example.mavendemo.error.EmBusinessError;
import com.example.mavendemo.response.CommonReturnType;
import com.example.mavendemo.service.UserService;
import com.example.mavendemo.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.security.MD5Encoder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

@Controller("user")
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", allowedHeaders = "*") // 解决跨域请求
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    // 用户登录接口
    @RequestMapping(value = "/login",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType login(@RequestParam(name="telphone")String telphone,
                                  @RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 参数校验
        if(StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        // 用户登录服务,加密密码
        UserModel userModel = userService.validateLogin(telphone,this.EncodeByMd5(password));

        // 将登陆凭证加入到用户登录成功的session里
        this.httpServletRequest.getSession().setAttribute("IS_LOGIN",true);
        this.httpServletRequest.getSession().setAttribute("LOGIN_USER",userModel);

        // 返回给前端正确的信息
        return CommonReturnType.create(null);
    }

    // 用户注册接口
    @RequestMapping(value = "/register",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType register(@RequestParam(name="telphone")String telphone,
                                     @RequestParam(name="otpCode")String otpCode,
                                     @RequestParam(name="name")String name,
                                     @RequestParam(name="gender")Integer gender,
                                     @RequestParam(name="age")Integer age,
                                     @RequestParam(name="password")String password) throws BusinessException, UnsupportedEncodingException, NoSuchAlgorithmException {
        // 验证用户收到的验证码和后台生成的验证码是否符合
        String inSessionOtpCode = (String)this.httpServletRequest.getSession().getAttribute(telphone);
        // 使用类库里的equals()方法的理由是其内部已经为我们实现了判空处理，可以Ctrl+单击进去看看
        if(!com.alibaba.druid.util.StringUtils.equals(otpCode,inSessionOtpCode)){
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR,"短信验证码错误");
        }
        // 用户注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        // 这里为啥要这样？直接在@RequestParam的时候使用Byte不行吗？
        // 我嫌老师这样麻烦，而且我也给整错了，索性直接把所有的gender都改为Integer类型
        // userModel.setGender(new Byte(String.valueOf(gender.intValue())));
        userModel.setGender(gender);
        userModel.setAge(age);
        userModel.setTelphone(telphone);
        userModel.setRegisterMode("byphone");
//        JDK自带的MD5实现方式不可用，老师说JDK中的MD5只支持16位的MD5，我们需要64位的
//        userModel.setEncryptPassword(MD5Encoder.encode(password.getBytes()));
        userModel.setEncryptPassword(this.EncodeByMd5(password));
        userService.register(userModel);
        return CommonReturnType.create(null);
    }

    // 64位的MD5
    public String EncodeByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64en = new BASE64Encoder();
        String newstr = base64en.encode(md5.digest(str.getBytes("utf-8")));
        return newstr;
    }

    @RequestMapping(value = "/getotp",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    @ResponseBody
    public CommonReturnType getOtp(@RequestParam(name="telphone")String telphone){
       // 生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(99999);
        randomInt += 10000;
        String optCode = String.valueOf(randomInt);
        // 绑定当前用户手机号和otpCode
        httpServletRequest.getSession().setAttribute(telphone,optCode);
        // 通过短信通道发送给用户，省略

        System.out.println("telphone: "+telphone+" otpCode: "+optCode);
        return CommonReturnType.create(null);
    }

    /**
     * 由于方法加了@ResponseBody注解，所以会将返回值解析为json格式
     * 具体可参考：【博客园】http://www.cnblogs.com/qiankun-site/p/5774325.html
     */
    @RequestMapping("/get")
    @ResponseBody
    public CommonReturnType getUser(@RequestParam(name = "id") Integer id) throws BusinessException {
        //调用Service服务获取对应id用户的信息并返回给前端
        UserModel userModel = userService.getUserById(id);
        // 抛出自定义异常
        if (userModel == null) {
            throw new BusinessException(EmBusinessError.USER_NOT_EXIST);
        }
        UserVO userVO = convertFromModel(userModel);
        return CommonReturnType.create(userVO);
    }

    // 提供给上方代码使用
    private UserVO convertFromModel(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

    // 下面没代码了，注释代码被放到BaseController.java基类中了
    // 这样所有Controller都可以使用了
    // 之所以保留注释代码，就是要体现一下代码迭代的过程和面向对象抽象基类代码的思维。
/*
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
    // 对上面代码的美化（如果抛出的异常不是BusinessException）
/*
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

    // 再次对上面的代码进行美化（提取出公共的代码，达到最终的效果）
/*
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
*/
}
