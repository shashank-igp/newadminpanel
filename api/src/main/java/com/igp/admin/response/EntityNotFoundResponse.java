package com.igp.admin.response;

import com.igp.config.ResponseProperties;

import javax.ws.rs.core.Response;

/**
 * Created by piyush on 7/12/15.
 */
public class EntityNotFoundResponse {

    public static Response entityNotFoundResponseBuilder(Object data) {
        Status status = Status.Error;
        Response.Status httpStatus = Response.Status.OK;
        String message = ResponseProperties.getEntityNotFound();
        ResponseModel responseModel = new ResponseModel.ResponseBuilder(status, httpStatus)
            .message(message)
            .data(data)
            .build();
        return CustomResponse.customResponse(responseModel);
    }
}
