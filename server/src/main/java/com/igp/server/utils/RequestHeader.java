package com.igp.server.utils;

import com.igp.config.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestHeader extends HttpServletRequestWrapper {

    private static final Logger logger = LoggerFactory.getLogger(RequestHeader.class);

    /**
     * construct a wrapper for this request
     *
     * @param request
     */
    public RequestHeader(HttpServletRequest request) {
        super(request);
    }

    private Map<String, String> headerMap = new HashMap<String, String>();

    /**
     * add a header with given name and value
     *
     * @param name
     * @param value
     */
    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = super.getHeader(name);
        if (headerMap.containsKey(name)) {
            headerValue = headerMap.get(name);
        }
        return headerValue;
    }

    /**
     * get the Header names
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        List<String> names = Collections.list(super.getHeaderNames());
        names.addAll(headerMap.keySet().stream().collect(Collectors.toList()));
        logger.debug("Printing headers: {}", headerMap);
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        List<String> values = Collections.list(super.getHeaders(name));
        if (headerMap.containsKey(name)) {
            values.add(headerMap.get(name));
        }
        return Collections.enumeration(values);
    }

    public Boolean verifyAuthorizationKey(HttpServletRequest httpServletRequest) {
        Boolean authKeyFlag = Boolean.FALSE;
        List<String> authKeysSupported = Environment.getSupportedAuthKeys();
        String allowedHeaders = Environment.getAllowedHeaders();
        logger.debug(allowedHeaders);
        try {
            String incomingAuthKey = httpServletRequest.getHeader(allowedHeaders);
            logger.debug(incomingAuthKey);
            if (incomingAuthKey != null && incomingAuthKey != "") {
                for (String key : authKeysSupported) {
                    logger.debug(key);
                    if (incomingAuthKey.equalsIgnoreCase(key)) {
                        authKeyFlag = Boolean.TRUE;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.debug("Exception while authorizing: " + e);
        }
        return authKeyFlag;
    }

    public Map<String, Boolean> regionLang(HttpServletRequest request) {
        Map<String, Boolean> status = new HashMap<>();
        
        //Set these to FALSE after getting the exhaustive list of countries
        //For now, allow all regions. FE ensures that these values will not be empty
        status.put("region", Boolean.TRUE);
        status.put("lang", Boolean.TRUE);
        
        List<String> definedRegion = Environment.getDefinedRegion();
        List<String> definedLang = Environment.getDefinedLang();
        Map<String, String[]> allQueryParams = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : allQueryParams.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            switch (key) {
                case "region":
                    if (value.length == 1) {
                        for (String x : value) {
                            if (definedRegion.contains(x)) {
                                status.put("region", Boolean.TRUE);
                            }
                        }
                    }
                    break;
            }
            if (key.equals("lang")) {
                if (value.length == 1) {
                    for (String x : value) {
                        if (definedLang.contains(x)) {
                            status.put("lang", Boolean.TRUE);

                        }
                    }
                }
            }
        }
        return status;
    }
}
