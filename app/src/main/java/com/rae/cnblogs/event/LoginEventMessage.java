package com.rae.cnblogs.event;

/**
 * Created by ChenRui on 2017/1/20 0020 14:34.
 */
public class LoginEventMessage {
    public String userName;
    public String password;
    public String verifyCode;

    public LoginEventMessage(String userName, String password) {
        this.password = password;
        this.userName = userName;
    }
}
