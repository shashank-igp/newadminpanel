package com.igp.admin.utils.httpRequest;

import com.igp.admin.models.marketPlace.HeaderKeyValueModel;
import com.igp.config.ServerProperties;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by suditi on 12/1/18.
 */
public class HttpRequestUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);

    public String sendCurlRequest(String requestMsg, String urll, List<HeaderKeyValueModel> headerKeyValueModelList) throws IOException {
        String response="",encoding="";
        InputStream in;
        URL url = new URL(urll);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        String postData = requestMsg;

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty(ServerProperties.getPropertyValue("ALLOWED_HEADERS"), ServerProperties.getPropertyValue("ALLOWED_AUTH_KEYS").split(",")[0]);
        if (!headerKeyValueModelList.isEmpty() || headerKeyValueModelList != null) {
            for (HeaderKeyValueModel header : headerKeyValueModelList) {
                con.setRequestProperty(header.getKey(), header.getValue());
                logger.debug("Header added is Key : " + header.getKey() + " Value : " + header.getValue());
            }
        }
        con.setInstanceFollowRedirects(true);
        con.setRequestProperty("Content-length", String.valueOf(postData.length()));

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
