package com.igp.permit.mappers;

import com.igp.permit.utils.PermissionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by suditi on 18/7/18.
 */
public class PermissionMapper {
    private static final Logger logger = LoggerFactory.getLogger(PermissionMapper.class);

    public boolean checkPermission(HttpServletRequest httpServletRequest) {
        boolean result = false;
        PermissionUtil permissionUtil  = new PermissionUtil();
        try {
            result = permissionUtil.checkPermission(httpServletRequest);
        } catch (Exception e) {
            //  logger.debug("error occured while checkpermissionmapper");
        }
        return  result;
    }
}
