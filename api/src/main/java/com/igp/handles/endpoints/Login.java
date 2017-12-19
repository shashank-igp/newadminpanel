package com.igp.handles.endpoints;

import com.igp.handles.mappers.LoginMapper;
import com.igp.handles.mappers.TokenMapper;
import com.igp.handles.models.login.UserModel;
import com.igp.handles.response.HandleServiceResponse;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Login
{
    @POST
    @Path("/v1/handels/login")
    public HandleServiceResponse login(@Context HttpServletResponse response, @QueryParam("username") String userName,
                                       @QueryParam("password") String password)
    {
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        LoginMapper loginMapper = new LoginMapper();
        UserModel userModel = loginMapper.getUserDetail(userName, password);
        if(userModel != null)
        {
            TokenMapper tokenMapper = new TokenMapper();
            String token =  UUID.randomUUID().toString();
            tokenMapper.saveToken(token, userModel.getId(),false);
            //create token , insert in DB and set it in responseheader
            response.addHeader("token",token);
            response.addHeader("fkAssociateId",userModel.getFkAssociateId());
            response.addHeader("associateName",userModel.getAssociateName());
        }
        handleServiceResponse.setResult(userModel);
        return handleServiceResponse;
    }

    @POST
    @Path("/v1/handels/doLogOut")
    public HandleServiceResponse logout(@Context HttpServletResponse response, @QueryParam("token") String token)
    {
        HandleServiceResponse handleServiceResponse = new HandleServiceResponse();
        TokenMapper tokenMapper = new TokenMapper();
        tokenMapper.expireToken(token);
        response.addHeader("token","");
        handleServiceResponse.setResult(true);
        return handleServiceResponse;
    }
}
