package com.igp.handles.vendorpanel.mappers;

import com.igp.handles.vendorpanel.models.login.UserModel;
import com.igp.handles.vendorpanel.utils.LoginUtil;

public class LoginMapper
{
    public UserModel getUserDetail(String userName, String password)
    {
        LoginUtil loginUtil = new LoginUtil();
        UserModel  userModel = loginUtil.getUserDetail(userName,password);
        return userModel;
    }
}
