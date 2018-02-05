package com.igp.handles.admin.utils.MailUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by shanky on 29/1/18.
 */
public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
    public boolean sendGenericMail(String action , String subject , String body,String recipient){
        boolean result=false;

        try
        {
            URL url = new URL("http://admin.indiangiftsportal.com/myshop/sendEmail.php");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            String postData = "";
            // CURLOPT_POST
            con.setRequestMethod("POST");

            // CURLOPT_FOLLOWLOCATION
            con.setInstanceFollowRedirects(true);

            postData+="action="+action+"&sub="+subject+"&to="+recipient+"&body="+ URLEncoder.encode(body,"UTF-8");

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
}
