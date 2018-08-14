package com.igp.permit.endpoints;

import com.igp.permit.mappers.PermissionMapper;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by suditi on 18/7/18.
 */
public class Permission {

    public boolean checkPermission(HttpServletRequest httpServletRequest) {
        boolean result = false;
        PermissionMapper permissionMapper = new PermissionMapper();
        try {
            result = permissionMapper.checkPermission(httpServletRequest);

            result =true;
        } catch (Exception var7) {
            //  logger.debug("error occured while while checkpermissionmapper");
        }
        return  result;
    }
}
