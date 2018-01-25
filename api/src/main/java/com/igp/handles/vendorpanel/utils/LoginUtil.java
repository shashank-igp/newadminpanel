package com.igp.handles.vendorpanel.utils;

import com.igp.config.instance.Database;
import com.igp.handles.vendorpanel.models.login.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
            String statement = "select * from associate_user au join associate a on au.fk_associate_login_id=a.associate_id "
                + " where  au.associate_user_name = ? and au.associate_user_pass = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next())
            {
                userModel = new UserModel.Builder()
                    .id(resultSet.getLong("au.associate_user_id"))
                    .name(resultSet.getString("au.associate_user_name"))
                    .fkAssociateId(resultSet.getString("au.fk_associate_login_id"))
                    .accountEnabled(resultSet.getInt("au.associate_user_status"))
                    .associateName(resultSet.getString("a.associate_name"))
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
