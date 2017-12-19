package com.igp.handles.utils;

import com.igp.config.instance.Database;
import com.igp.handles.models.login.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class LoginUtil
{
    private static final Logger logger = LoggerFactory.getLogger(LoginUtil.class);

    public UserModel getUserDetail(String userName, String password)
    {
        UserModel userModel = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try
        {
            connection = Database.INSTANCE.getReadOnlyConnection();
            String statement = "select * from igp_users where  name = ? and password = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                userModel = new UserModel.Builder()
                    .id(resultSet.getLong("user_id"))
                    .name(resultSet.getString("name"))
                    .password(resultSet.getString("password"))
                    .fkAssociateId(resultSet.getString("fkAssociateId"))
                    .associateName(resultSet.getString("associateName"))
                    .phoneNumber(resultSet.getString("phone_number"))
                    .expires(resultSet.getDate("expires"))
                    .accountExpired(resultSet.getBoolean("account_expired"))
                    .credentialExpired(resultSet.getBoolean("credentials_expired"))
                    .accountLocked(resultSet.getBoolean("account_locked"))
                    .accountEnabled(resultSet.getBoolean("account_enabled"))
                    .build();
            }
        }
        catch (Exception e)
        {
            logger.error("Exception in connection", e);
        }
        finally
        {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return userModel;
    }
}
