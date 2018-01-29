package com.igp.admin.utils.httpRequest;

import com.igp.config.ServerProperties;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by suditi on 12/1/18.
 */
public class HttpRequestUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

    public String sendCurlRequest(String requestMsg, String urll) throws IOException {
        String response,encoding;
        InputStream in;
        URL url = new URL(urll);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        String postData=requestMsg;

        con.setRequestProperty("Content-Type","application/json");
        con.setRequestProperty(ServerProperties.getPropertyValue("ALLOWED_HEADERS"),ServerProperties.getPropertyValue("ALLOWED_AUTH_KEYS").split(",")[0]);
        con.setDoOutput(true);
        con.setDoInput(true);
        DataOutputStream output = new DataOutputStream(con.getOutputStream());
        output.writeBytes(postData);
        output.close();

        in = con.getInputStream();
        encoding = con.getContentEncoding();
        encoding = encoding == null ? "UTF-8" : encoding;
        response = IOUtils.toString(in, encoding);
        return response;
    }
}
