package com.igp.handles.vendorpanel.utils.Order;

import com.igp.config.instance.Database;
import com.igp.handles.admin.utils.Order.OrderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shal on 9/8/17.
 */
public class OrderStatusUpdateUtil {

    private static final Logger logger = LoggerFactory.getLogger(OrderStatusUpdateUtil.class);

    public static Boolean updateStatusForOrderUtil(int orderId,String fkAssociateId,String status,String orderProductIds,
        String recipientInfo,String rejectionMessage,String commentsDelivered,String recipientName,int rejectionType
        ,String ipAddress,String userAgent){
        Boolean result=false;
        String productIds="",stringToUpdate="";
        int deliveryAttemptFlag=0;
        com.igp.handles.admin.utils.Order.OrderUtil orderUtil=new OrderUtil();
        try
        {
            productIds=getProductsIds(orderProductIds);
            String[] productIdArray=productIds.split(",");
            switch (status)
            {
                case "Confirmed":
                    result=markConfirmedOrdersProducts(orderId,fkAssociateId,status,orderProductIds);
                    if (result){
                        stringToUpdate="Products Confirmed : "+productIds+" for orderId "+orderId+" using Vendor Upload Panel ";
                        updateOrderToStatusOrPartiallyStatus(orderId,status);
                        updateOrderHistory( stringToUpdate,orderId ,orderProductIds,fkAssociateId);
                        for(int i=0;i<productIdArray.length;i++){
                            orderUtil.insertIntoHandelOrderHistory(orderId,Integer.parseInt(productIdArray[i]),
                                Integer.parseInt(fkAssociateId),stringToUpdate,ipAddress,userAgent,
                                "status_change","Confirmed");
                        }
                    }
                    else {
                        result=false;
                    }
                    break;

                case "OutForDelivery":
                    result=markOutForDelivery(orderId,fkAssociateId,"Shipped",orderProductIds);
                    if (result) {
                        stringToUpdate="Products OutForDelivery : "+productIds+" for orderId "+orderId+" using Vendor Upload Panel ";
                        sendEmailForOrder(orderId, status, fkAssociateId, productIds);
                        updateOrderToStatusOrPartiallyStatus(orderId, "Shipped");
                        updateOrderHistory(stringToUpdate, orderId, orderProductIds, fkAssociateId);
                        for(int i=0;i<productIdArray.length;i++){
                            orderUtil.insertIntoHandelOrderHistory(orderId,Integer.parseInt(productIdArray[i]),
                                Integer.parseInt(fkAssociateId),stringToUpdate,ipAddress,userAgent,
                                "status_change","OutForDelivery");
                        }
                    }
                    else {
                        result=false;
                    }

                    break;
                case "Delivered":
                    deliveryAttemptFlag=0;
                    result=markDelivered(orderId,fkAssociateId,status,orderProductIds,deliveryAttemptFlag);
                    if (result){
                        stringToUpdate="Products delivered : "+productIds+" for orderId "+orderId+" using Vendor Upload Panel ";
                        updateHandelsHistory(orderId, recipientInfo, rejectionMessage, recipientInfo,rejectionMessage,
                            status,orderProductIds,commentsDelivered,recipientName,rejectionType,fkAssociateId);
                        sendEmailForOrder(orderId,status,fkAssociateId,productIds);
                        updateOrderHistory( stringToUpdate,orderId ,orderProductIds,fkAssociateId);
                        if(commentsDelivered==null||commentsDelivered.equals("")){
                            commentsDelivered=" none ";
                        }
                        if(recipientName==null||recipientName.equals("")){
                            recipientName=" none ";
                        }
                        stringToUpdate+=" with Recipient Information "+recipientName+" and comments =  "+commentsDelivered;
                        for(int i=0;i<productIdArray.length;i++){
                            orderUtil.insertIntoHandelOrderHistory(orderId,Integer.parseInt(productIdArray[i]),
                                Integer.parseInt(fkAssociateId),stringToUpdate,ipAddress,userAgent,
                                "status_change","Delivered");
                        }
                    }
                    break;
                case "Rejected":
                    // orderDao.updateOrderStatus(scopeId, fkAssociateId,
                    // orderId, "Rejected");
                    result=markRejected(orderId,fkAssociateId,status,orderProductIds);
                    if (result){
                        stringToUpdate="Products Rejected : "+productIds+" for orderId "+orderId+" using Vendor Upload Panel ";
                        updateOrderHistory( stringToUpdate,orderId ,orderProductIds,fkAssociateId);
                        updateHandelsHistory(orderId, recipientInfo, rejectionMessage, recipientInfo,rejectionMessage,status,
                            orderProductIds,commentsDelivered,recipientName,rejectionType,fkAssociateId);
                        for(int i=0;i<productIdArray.length;i++){
                            orderUtil.insertIntoHandelOrderHistory(orderId,Integer.parseInt(productIdArray[i]),
                                Integer.parseInt(fkAssociateId),stringToUpdate,ipAddress,userAgent,
                                "status_change","Rejected");
                        }
                    }
                    break;
                case "AttemptedDelivery":
                    deliveryAttemptFlag=1;
                    result=markDelivered(orderId,fkAssociateId,status,orderProductIds,deliveryAttemptFlag);
                    if(result){
                        stringToUpdate="Try To Attempt delivery of : "+productIds+" for orderId "+orderId+" using Vendor Upload Panel ";
                        updateOrderHistory( stringToUpdate,orderId ,orderProductIds,fkAssociateId);
                        if(commentsDelivered==null||commentsDelivered.equals("")){
                            commentsDelivered=" none ";
                        }
                        if(recipientName==null||recipientName.equals("")){
                            recipientName=" none ";
                        }
                        stringToUpdate+=" with Recipient Information "+recipientName+" and comments =  "+commentsDelivered;
                        for(int i=0;i<productIdArray.length;i++){
                            orderUtil.insertIntoHandelOrderHistory(orderId,Integer.parseInt(productIdArray[i]),
                                Integer.parseInt(fkAssociateId),stringToUpdate,ipAddress,userAgent,
                                "status_change","attempted");
                        }
                    }
                    break;
                default:
                    try {
                        throw new Exception("Invalid value of status");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
        catch (Exception exception){
            logger.error("Exception while updating", exception);
        }

        return result;
    }

    public  static Boolean  markConfirmedOrdersProducts(int orderId, String fkAssociateId, String status, String orderProductIds){
        Connection connection = null;
        String statement;
        ResultSet resultSet = null;
        Boolean ifUpdationSucessfull=false;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "update orders_products as op inner join trackorders as tk on op.orders_products_id=tk.orders_products_id and  op.orders_id=tk.orders_id  set tk.releaseDate=now() ,op.orders_product_status= ? where  op.orders_id=? and op.fk_associate_id in(?) and op.orders_products_id in ( "+orderProductIds+" ) ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,status);
            preparedStatement.setInt(2,orderId);
            preparedStatement.setString(3,fkAssociateId);
            //  preparedStatement.setString(4,orderProductIds);
            logger.debug("sql query "+preparedStatement);
            Integer rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated>0){
                ifUpdationSucessfull=true;
            }
            else {
                ifUpdationSucessfull=false;
            }

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return ifUpdationSucessfull;
    }


    public static Boolean   markOutForDelivery(int orderId, String fkAssociateId, String status, String orderProductIds){
        Connection connection = null;
        String statement;
        Boolean ifUpdationSucessfull=false;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "update orders_products as op inner join trackorders as tk on op.orders_products_id=tk.orders_products_id and  op.orders_id=tk.orders_id  set tk.outForDeliveryDate=now() ,op.orders_product_status= ? , op.delivery_status = ? where  op.orders_id=? and op.fk_associate_id in(?) and op.orders_products_id in ( "+orderProductIds+") ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,status);
            preparedStatement.setInt(2,0);
            preparedStatement.setInt(3,orderId);
            preparedStatement.setString(4,fkAssociateId);
            //     preparedStatement.setString(4,orderProductIds);
            logger.debug("sql query "+preparedStatement);
            Integer rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated>0){
                ifUpdationSucessfull=true;
            }
            else {
                ifUpdationSucessfull=false;
            }

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return ifUpdationSucessfull;


    }
    public static Boolean markDelivered(int orderId, String fkAssociateId, String status, String orderProductIds,int deliveryAttemptFlag){

        Connection connection = null;
        String statement;
        Boolean ifUpdationSucessfull=false;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "update orders_products as op inner join trackorders as tk on op.orders_products_id=tk.orders_products_id and  op.orders_id=tk.orders_id  set tk.deliveredDate=now() , op.orders_product_status = ? ,op.delivery_status= 1,op.delivery_attempt = case when op.delivery_attempt = 2 then 3 else  "+ deliveryAttemptFlag +" end where  op.orders_id=? and op.fk_associate_id in(?) and op.orders_products_id in ( "+orderProductIds+") ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,"Shipped");
            preparedStatement.setInt(2,orderId);
            preparedStatement.setString(3,fkAssociateId);
            logger.debug("sql query "+preparedStatement);
            Integer rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated>0){
                ifUpdationSucessfull=true;

            }
            else {
                ifUpdationSucessfull=false;
            }

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return ifUpdationSucessfull;
    }

    public static void updateOrderToStatusOrPartiallyStatus(int orderId, String orderStatus){

        Connection connection = null;
        String statement;
        ResultSet resultSet = null;
        // Boolean ifUpdationSucessfull=false;
        PreparedStatement preparedStatement = null;
        int a=0,b=0,c=0,value=0,max=0;
        HashMap<String,Integer> statusValue = new HashMap<String,Integer>()
        {
            {
                put("Processed",1);
                put("Confirmed",2);
                put("Shipped",3);
            }
        };
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement="select op.orders_product_status from orders o inner join orders_products op on o.orders_id=op.orders_id where o.orders_id= ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderId);
            logger.debug("sql query "+preparedStatement);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                value=statusValue.get(resultSet.getString("op.orders_product_status"));

                if(value==1)
                    a=1;
                    // marking if status is processed
                else if(value==2)
                    b=1;
                    // marking if status is confirmed
                else if(value==3)
                    c=1;
                // marking if status is shipped

                if (max<value)
                    max=value;
                // checking which status has more priority
                // (e.g. one is shipped other is confirmed then we mark according to shipped)


            }
            if((a+b+c)==1) {
                // checking if all the status are same
                if (max==3)
                    updateOrderStatus("Dispatched", orderId);
                    // If status is shipped, we mark it as dispatched.
                else if(max==2)
                    updateOrderStatus("Confirmed", orderId);
            }

            else {
                // checking if the status of different products in an order mismatch
                if (max==3)
                    updateOrderStatus("Partially Dispatched", orderId);
                    // If status is shipped, we mark it as Partially dispatched.
                else if(max==2)
                    updateOrderStatus("Partially Confirmed", orderId);
            }

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }



    }


    public static Boolean markRejected(int orderId, String fkAssociateId, String status, String orderProductIds){

        Connection connection = null;
        String statement;
        Boolean ifUpdationSucessfull=false;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "update orders_products as op inner join trackorders as tk on op.orders_products_id=tk.orders_products_id and  op.orders_id=tk.orders_id  set op.orders_product_status= ? where  op.orders_id=? and op.fk_associate_id in(?) and op.orders_products_id in (  "+orderProductIds+" ) ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,status);
            preparedStatement.setInt(2,orderId);
            preparedStatement.setString(3,fkAssociateId);
            // preparedStatement.setString(4,orderProductIds);
            logger.debug("sql query "+preparedStatement);
            Integer rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated>0){
                ifUpdationSucessfull=true;
            }
            else {
                ifUpdationSucessfull=false;
            }

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return ifUpdationSucessfull;
    }

    public  static  void updateOrderStatus(String status,int orderId){

        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement="update orders set orders_status= ? where orders_id=?;";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1,status);
            preparedStatement.setInt(2,orderId);
            logger.debug("sql query "+preparedStatement);
            int rows=preparedStatement.executeUpdate();

        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }



    }
    public  static  void updateHandelsHistory(int orderId, String info, String message, String recipientInfo,
        String rejectionMessage, String status, String orderProductIds,
        String commentsDelivered,String recipientName,int rejectionType,
        String fkAssociateId ){

        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        Map<Integer,String> rejectionTypeToReasonMap=new HashMap<>();
        rejectionTypeToReasonMap.put(1,"Delivery location not serviceable");
        rejectionTypeToReasonMap.put(2,"Product not available");
        rejectionTypeToReasonMap.put(3,"Capacity full");
        rejectionTypeToReasonMap.put(4,"Delivery not possible on given date");
        rejectionTypeToReasonMap.put(5,"Fixed time or Midnight delivery not possible");
        rejectionTypeToReasonMap.put(6,"Duplicate order");
        rejectionTypeToReasonMap.put(7,"Other");


        try{

            List<String> orderProductsLists = Arrays.asList(orderProductIds.split(","));
            StringBuilder sb=new StringBuilder("VALUES");
            for (int i = 0; i < orderProductsLists.size(); i++) {
                if (status.equals("Rejected")){

                    if (i==orderProductsLists.size()-1){
                        sb.append("(" + orderProductsLists.get(i) + "," + orderId + "," + rejectionType + ",'" + rejectionMessage + "',now())");
                    }
                    else {
                        sb.append("(" + orderProductsLists.get(i) + "," + orderId + "," + rejectionType + ",'" + rejectionMessage + "',now()),");
                    }
                }else
                {
                    if (i==orderProductsLists.size()-1){
                        sb.append("(" + orderProductsLists.get(i) + "," + orderId + ",'" + recipientName + "','" + commentsDelivered + "','" + recipientInfo + "',now())");
                    }
                    else {
                        sb.append("(" + orderProductsLists.get(i) + "," + orderId + ",'" + recipientName + "','" + commentsDelivered + "','" + recipientInfo + "',now()),");
                    }
                }
            }
            connection = Database.INSTANCE.getReadWriteConnection();
            if (status.equals("Delivered")) {
                statement=" INSERT INTO `handels_history` (`orders_products_id`,`orders_id`, `recipient_name`, `comments`, `recipient_info`, `insertTime`) "+sb.toString()+" ON DUPLICATE KEY UPDATE recipient_name='"+recipientName+"',comments='"+commentsDelivered+"' ,orders_id="+orderId+" ,recipient_info= '"+recipientInfo+"' ,insertTime= now();";
            }
            else { // i.e Rejected
                statement="INSERT INTO `handels_history` (`orders_products_id`,`orders_id`, `rejection_type` , `rejection_message`, `insertTime`) "+sb.toString()+" ON DUPLICATE KEY UPDATE orders_id="+orderId+",rejection_type="+rejectionType+",rejection_message='"+rejectionMessage+"' ,insertTime= now();";

                sendEmailToHandelsTeamToTakeAction(orderId,fkAssociateId,rejectionTypeToReasonMap.get(rejectionType),rejectionMessage);
            }
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query "+preparedStatement);
            int rows=preparedStatement.executeUpdate();
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }


    }

    public static void updateOrderHistory(String stringToUpdate, int orderId, String ordersProductsIds, String fkAssociateId){
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "INSERT IGNORE into orders_history (fk_associate_login_id,fk_associate_user_id,fk_orders_id,orders_history_comment,orders_history_time,fk_associate_id) VALUES ('system','vendorpandel',?,?,NOW(),?) ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderId);
            preparedStatement.setString(2,stringToUpdate);
            preparedStatement.setString(3,fkAssociateId);
            logger.debug("sql query "+preparedStatement);
            Integer rowsUpdated = preparedStatement.executeUpdate();


        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }

    }



    public static void sendEmailForOrder(int orderId,String status,String fkAssociateId,String productIds){

        try
        {
            URL url = new URL("http://admin.indiangiftsportal.com/myshop/sendEmail.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String postData = null;
            // CURLOPT_POST
            con.setRequestMethod("POST");

            // CURLOPT_FOLLOWLOCATION
            con.setInstanceFollowRedirects(true);
            if (status.equals("OutForDelivery")){
                postData = "action=Dispatched&orderid="+orderId+"&associd="+fkAssociateId+"&productIds="+productIds;
            }

            else if (status.equals("Delivered")){
                postData = "action=Delivered&orderid="+orderId+"&associd="+fkAssociateId+"&productIds="+productIds;
            }
            // URLEncoder.encode(emailBodyWithActualContents, "UTF-8");


            //String postData = "to="+recipient+"&sub=ForgotPassword&body=sadasdasdasdasd";
            con.setRequestProperty("Content-length", String.valueOf(postData.length()));

            con.setDoOutput(true);
            con.setDoInput(true);

            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(postData);
            output.close();

            // "Post data send ... waiting for reply");
            int code = con.getResponseCode(); // 200 = HTTP_OK
            logger.debug("EmailCURL: "+status+" :: "+orderId+" :: :: Response Code - "+code);
        }
        catch (Exception e)
        {
            logger.error("Exception ", e);
        }
    }
    public static void sendEmailToHandelsTeamToTakeAction(int orderId,String fkAssociateId,String regarding,String message){
        try
        {
            URL url = new URL("http://admin.indiangiftsportal.com/myshop/sendEmail.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String postData = "",body="",subject="";
            // CURLOPT_POST
            con.setRequestMethod("POST");
            con.setInstanceFollowRedirects(true);
            if(orderId==0){
                regarding="Vendor Wants to Change Component/Pincode Related Things";
                subject+="Vendor Panel : Component or Pincode Related changes";
            }else {
                body+="OrderId :-  "+orderId+"  \n\n";
                subject+="Vendor Panel : Order Related Issue orderId "+orderId;
            }
            body+="Regarding :- "+regarding+"\n\n";
            body+="Message from Vendor :-   "+message+"  \n\n";
            postData+="action=HandelVendorPanel&associd="+fkAssociateId+"&subject="+subject+"&body="+ URLEncoder.encode(body,"UTF-8");



            //String postData = "to="+recipient+"&sub=ForgotPassword&body=sadasdasdasdasd";
            con.setRequestProperty("Content-length", String.valueOf(postData.length()));

            con.setDoOutput(true);
            con.setDoInput(true);

            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(postData);
            output.close();

            // "Post data send ... waiting for reply");
            int code = con.getResponseCode(); // 200 = HTTP_OK
        }
        catch (Exception e)
        {
            logger.error("Exception ", e);
        }
    }
    public static String getProductsIds(String orderProductIds){
        String productIds="";
        Connection connection = null;
        String statement;
        String stringToUpdate="";
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "SELECT products_id from orders_products where orders_products_id in ( "+orderProductIds+" ) ";
            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query "+preparedStatement);
            resultSet=preparedStatement.executeQuery();
            while (resultSet.next()){
                productIds+=resultSet.getString("products_id")+",";
            }
            if(productIds.length()>0){
                productIds=productIds.substring(0,productIds.length()-1);
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return productIds;
    }

    //RejectionType >> 0=no rejection,1=Delivery location not serviceable,2=Product not available,3=Capacity full,
    // 4=Customers instruction,5=Delivery not possible on given date,6=Fixed time or Midnight not possible,
    // 7=Duplicate order,8=Other,9=Alternate Number/Address issues
}

