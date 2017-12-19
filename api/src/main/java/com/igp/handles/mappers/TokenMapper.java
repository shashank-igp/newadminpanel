package com.igp.handles.mappers;

import com.igp.handles.models.login.TokenModel;
import com.igp.handles.utils.TokenUtil;

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
