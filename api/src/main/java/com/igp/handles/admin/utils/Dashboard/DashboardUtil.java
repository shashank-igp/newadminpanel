package com.igp.handles.admin.utils.Dashboard;

import com.igp.config.instance.Database;
import com.igp.handles.vendorpanel.models.Vendor.OrderDetailsPerOrderProduct;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by shanky on 16/1/18.
 */
public class DashboardUtil {
    private static final Logger logger = LoggerFactory.getLogger(DashboardUtil.class);
    public List<OrderDetailsPerOrderProduct> getHandelsOrderList( Date date,int flag){
        Connection connection = null;
        String statement,pStatus= flag==0 ? ",'Shipped'":""  , dateComapareSymbol= flag==0 ? ">=":"=";
        ResultSet resultSet = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        PreparedStatement preparedStatement = null;

        List<OrderDetailsPerOrderProduct> listOfOrderIdAsPerVendor=new ArrayList<>();
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "select * from orders o join orders_products op on o.orders_id = op.orders_id join "
                + " order_product_extra_info opei on opei.order_product_id=op.orders_products_id join products p on p.products_id = op.products_id "
                + " where date_format(opei.delivery_date,'%Y-%m-%d') "+dateComapareSymbol+" ? and p.fk_associate_id = ? ";
            preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1,new SimpleDateFormat("yyyy-MM-dd").format(date));
            preparedStatement.setInt(2,72);

            logger.debug("STATEMENT CHECK: " + preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Date deliveryDate=resultSet.getString("opei.delivery_date")== null ? null : DateUtils
                    .truncate(dateFormat.parse(resultSet.getString("opei.delivery_date")), Calendar.DAY_OF_MONTH);

                OrderDetailsPerOrderProduct orderDetailsPerOrderProduct =new OrderDetailsPerOrderProduct();
                orderDetailsPerOrderProduct.setOrdersId(resultSet.getLong("o.orders_id"));

                orderDetailsPerOrderProduct.setDeliveryDate(deliveryDate);

                orderDetailsPerOrderProduct.setOrderProductStatus(resultSet.getString("op.orders_product_status"));
                orderDetailsPerOrderProduct.setDeliveryStatus(resultSet.getBoolean("op.delivery_status"));
                orderDetailsPerOrderProduct.setDeliveryTime(resultSet.getString("opei.delivery_time"));
                orderDetailsPerOrderProduct.setShippingType(resultSet.getString("opei.delivery_type"));
                orderDetailsPerOrderProduct.setSlaCode(resultSet.getInt("op.sla_code"));
                orderDetailsPerOrderProduct.setOrdersProductsId(resultSet.getLong("op.orders_products_id"));
                orderDetailsPerOrderProduct.setVendorId(resultSet.getInt("op.fk_associate_id"));
                listOfOrderIdAsPerVendor.add(orderDetailsPerOrderProduct);
            }

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return listOfOrderIdAsPerVendor;
    }
}
