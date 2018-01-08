package com.igp.handles.vendorpanel.mappers;

import com.igp.handles.vendorpanel.models.login.TokenModel;
import com.igp.handles.vendorpanel.utils.TokenUtil;

public class TokenMapper
{
    public void saveToken(String token, long userId , boolean expired)
    {
        TokenUtil tokenUtil = new TokenUtil();
        tokenUtil.saveToken(token,userId,expired);
    }

    public TokenModel getTokenModel(String token)
    {
        TokenUtil tokenUtil = new TokenUtil();
        return tokenUtil.getTokenModel(token);
    }

    public void expireToken(String token)
    {
        TokenUtil tokenUtil = new TokenUtil();
        tokenUtil.expireToken(token);
    }
}
