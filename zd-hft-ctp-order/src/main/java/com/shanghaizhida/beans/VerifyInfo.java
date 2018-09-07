package com.shanghaizhida.beans;

/**
 * 密保认证类
 *
 * Created by liwei on 2018/3/5.
 */
public class VerifyInfo implements NetParent {

    // 用户帐号
    public String UserId = "";

    // 交易密码
    public String UserPwd = "";

    // 问题类型 1：密保认证；2：手机验证码认证
    public String type = "";

    // 密保问题编号
    public String QA = "";

    // 密保问题答案
    public String answer = "";

    // 手机号
    public String mobile = "";

    // 手机验证码
    public String verifyCode = "";

    // 是否需要记住该设备 1：是；0 or other：不是
    public String saveMac = "";

    @Override
    public String MyToString() {
        String temp = this.UserId + "@" + this.UserPwd
                + "@" + this.type + "@" + this.QA + "@" + this.answer
                + "@" + this.mobile + "@" + this.verifyCode + "@" + this.saveMac;

        return temp;
    }

    @Override
    public void MyReadString(String temp) {
        String[] arrClass = temp.split("@");
        this.UserId = arrClass[0];
        this.UserPwd = arrClass[1];
        this.type = arrClass[2];
        this.QA = arrClass[3];
        this.answer = arrClass[4];
        this.mobile = arrClass[5];
        this.verifyCode = arrClass[6];
        if (arrClass.length > 7) {
            this.saveMac = arrClass[7];
        }
    }

    @Override
    public String MyPropToString() {
        String temp = "UserId@UserPwd@type@QA@answer@mobile@verifyCode@saveMac";
        return temp;
    }
}
