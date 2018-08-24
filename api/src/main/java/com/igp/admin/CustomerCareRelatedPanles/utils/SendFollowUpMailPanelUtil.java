package com.igp.admin.CustomerCareRelatedPanles.utils;

import com.igp.config.instance.Database;
import com.igp.handles.vendorpanel.models.Order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by shanky on 23/8/18.
 */
public class SendFollowUpMailPanelUtil {
    private static final Logger logger = LoggerFactory.getLogger(SendFollowUpMailPanelUtil.class);
    public Order getOrderDetailsBasedOnAwbNumber(String awb){
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Order order=null;
        try {
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "select o.* from orders o join  orders_products op  on o.orders_id = op.orders_id where op.orders_awbnumber_associatewise = ? limit 1";
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, awb);
            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                order=new Order.Builder()
                    .orderId(resultSet.getInt("o.orders_id"))
                    .deliveryName(resultSet.getString("o.delivery_name"))
                    .deliveryStreetAddress(resultSet.getString("o.delivery_street_address"))
                    .deliveryCity(resultSet.getString("o.delivery_city"))
                    .deliveryPostcode(resultSet.getString("o.delivery_postcode"))
                    .deliveryState(resultSet.getString("o.delivery_state"))
                    .deliveryCountry(resultSet.getString("o.delivery_country"))
                    .deliveryEmail(resultSet.getString("o.delivery_email_address"))
                    .deliveryMobile(resultSet.getString("o.delivery_mobile"))
                    .build();
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return order;
    }
}
