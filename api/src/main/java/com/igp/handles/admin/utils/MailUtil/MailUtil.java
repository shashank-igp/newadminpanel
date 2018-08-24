package com.igp.handles.admin.utils.MailUtil;

import com.igp.config.instance.Database;
import com.igp.handles.admin.models.Mail.MailTemplateModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by shanky on 29/1/18.
 */
public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
    public boolean sendGenericMail(String action , String subject , String body,String recipient,boolean noReply){
        boolean result=false;

        try
        {
            URL url = new URL("http://admin.indiangiftsportal.com/myshop/sendEmail.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String postData = "",noReplyString="";
            // CURLOPT_POST
            con.setRequestMethod("POST");

            // CURLOPT_FOLLOWLOCATION
            con.setInstanceFollowRedirects(true);

            if(noReply){
                noReplyString="&noreply=1";
            }

            postData+="action="+action+"&sub="+subject+noReplyString+"&to="+recipient+"&body="+ URLEncoder.encode(body,"UTF-8");

            con.setRequestProperty("Content-length", String.valueOf(postData.length()));

            con.setDoOutput(true);
            con.setDoInput(true);

            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(postData);
            output.close();

            // "Post data send ... waiting for reply");
            int code = con.getResponseCode(); // 200 = HTTP_OK
            if(code==200){
                result=true;
            }
            logger.debug("Post data for sentMail "+postData);
        }
        catch (Exception exception)
        {
            logger.error("Exception while sending Mail ", exception);
        }

        return result;
    }
    public boolean sendGenericSms(String body,String recipient){
        boolean result=false;
        try{
            String postData = "username="
                + URLEncoder.encode("indiangift", "UTF-8") + "&password="
                + URLEncoder.encode("k0m1a9v8", "UTF-8") + "&type="
                + URLEncoder.encode("0", "UTF-8") + "&dlr="
                + URLEncoder.encode("1", "UTF-8") + "&destination="
                + URLEncoder.encode(recipient, "UTF-8") + "&source="
                + URLEncoder.encode("IGPCOM", "UTF-8") + "&message="
                + URLEncoder.encode(body, "UTF-8");
            URL url = new URL("http://sms6.routesms.com:8080/bulksms/bulksms?"+postData);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-length", String.valueOf(postData.length()));
            con.setInstanceFollowRedirects(true);

            con.setDoOutput(true);
            con.setDoInput(true);

            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes(postData);
            output.close();

            // "Post data send ... waiting for reply");
            int code = con.getResponseCode(); // 200 = HTTP_OK
            if(code==200){
                result=true;
            }
        }catch (Exception exception){
            logger.error("Exception while sending Sms", exception);
        }
        return result;
    }
    public MailTemplateModel getMailTemplateFromDb(Integer templateType) {

        MailTemplateModel mailTemplateModel = new MailTemplateModel();
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "SELECT * from newigp_mail_template  WHERE mail_id = ?";
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, templateType.toString());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                mailTemplateModel.setContent(resultSet.getString("content"));
                mailTemplateModel.setEmail_body(resultSet.getString("email_body"));
                mailTemplateModel.setEmail_header(resultSet.getString("email_header"));
                mailTemplateModel.setEmail_footer(resultSet.getString("email_footer"));
                logger.debug("Email Template is found and is sent for processing");
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return mailTemplateModel;
    }

}
