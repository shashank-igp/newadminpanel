package com.igp.permit.utils;

import com.igp.config.instance.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by suditi on 18/7/18.
 */
public class PermissionUtil {
    private static final Logger logger = LoggerFactory.getLogger(PermissionUtil.class);

    public boolean checkPermission(HttpServletRequest httpServletRequest) {
        boolean result = false;
        Connection connection = null;
        String statement = "";
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "select permission from newigp_permissions where fk_associate_id=? and " +
                "fk_user_id=? and module_id=? and page_id=? ";

            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,5); // this comes in request
            preparedStatement.setInt(2,1); // this comes in request
            preparedStatement.setInt(3,1); // this comes in request
            preparedStatement.setInt(4,1); // this comes in request

            logger.debug("preparedStatement for checkpermission -> " + preparedStatement);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                String userPermission = resultSet.getString(1);
                String reqdPermission = "w"; // this comes in request
                if(PermissionConstantsUtil.getPermission().get(reqdPermission)<=PermissionConstantsUtil.getPermission().get(userPermission)) {
                    httpServletRequest.setAttribute("permission", userPermission);
                    result = true;
                }
            }

        } catch (Exception e) {
            logger.debug("error occured while checking permission of user." + e);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return result;
    }

}
