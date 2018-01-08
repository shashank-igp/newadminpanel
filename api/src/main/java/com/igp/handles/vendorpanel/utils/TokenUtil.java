package com.igp.handles.vendorpanel.utils;

import com.igp.config.instance.Database;
import com.igp.handles.vendorpanel.models.login.TokenModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TokenUtil
{
    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    public void saveToken(String token, long userId , boolean expired)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try
        {
            connection = Database.INSTANCE.getReadWriteConnection();
            String statement = "insert into igp_user_token (token,user_id,expired) values(?,?,?) ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, token);
            preparedStatement.setLong(2, userId);
            preparedStatement.setInt(3, expired== true ? 1 : 0);
            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {
            logger.error("Exception in saveToken:", e);
        }
        finally
        {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
    }

    public TokenModel getTokenModel(String token)
    {
        TokenModel tokenModel = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try
        {
            connection = Database.INSTANCE.getReadOnlyConnection();
            String statement = "select * from igp_user_token where token=? and expired=?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, token);
            preparedStatement.setInt(2, 0);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                tokenModel = new TokenModel.Builder()
                    .expired(resultSet.getBoolean("expired"))
                    .userId(resultSet.getLong("user_id"))
                    .token(resultSet.getString("token"))
                    .build();
            }
        }
        catch(Exception e)
        {
            logger.error("Exception in getTokenModel:", e);
        }
        finally
        {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return tokenModel;
    }

    public void expireToken(String token)
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try
        {
            connection = Database.INSTANCE.getReadWriteConnection();
            String statement = "update igp_user_token set expired=? where token = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, 0);
            preparedStatement.setString(2, token);
            preparedStatement.executeUpdate();
        }
        catch(Exception e)
        {
            logger.error("Exception in expireToken:", e);
        }
        finally
        {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
    }
}
