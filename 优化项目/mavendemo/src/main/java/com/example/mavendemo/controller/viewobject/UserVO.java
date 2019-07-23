package com.example.mavendemo.controller.viewobject;

/**
 * 此文件定义了前端展示需要的信息
 * 保护一些UserModel中的隐私数据
 *
 * 至此，数据总共经历了三层
 *      1、dataobject   与数据库表一一映射   UserDO、UserPasswordDO
 *      2、service.model 整合不同表的数据    UserModel
 *      3、controller.viewobject    UI展示数据 UserVO
 */
public class UserVO {
    private Integer id;
    private String name;
    private Byte gender;
    private Integer age;
    private String telphone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public Integer getAge() { return age; }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }
}
