package com.igp.handles.admin.utils.MailUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shanky on 29/1/18.
 */
public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);
    public boolean sendGenericMail(String action , String subject , String body){
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
//            postData+="action=HandelVendorPanel&associd="+fkAssociateId+"&subject="+subject+"&body="+ URLEncoder
//                .encode(body,"UTF-8");
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

        }
        catch (Exception e)
        {
            logger.error("Exception ", e);
        }

        return result;
    }
}
