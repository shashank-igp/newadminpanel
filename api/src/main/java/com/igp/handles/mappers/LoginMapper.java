package com.igp.handles.mappers;

import com.igp.handles.models.login.UserModel;
import com.igp.handles.utils.LoginUtil;

public class LoginMapper
{
    public UserModel getUserDetail(String userName, String password)
    {
        LoginUtil loginUtil = new LoginUtil();
        UserModel  userModel = loginUtil.getUserDetail(userName,password);
        return userModel;
    }
}
