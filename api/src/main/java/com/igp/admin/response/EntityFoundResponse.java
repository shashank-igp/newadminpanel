package com.igp.admin.response;

import javax.ws.rs.core.Response;

/**
 * Created by suditi on 18/1/18.
 */
public class EntityFoundResponse {
    public static Response entityFoundResponseBuilder(Object data) {
        Status status = Status.Success;
        Response.Status httpStatus = Response.Status.OK;
        ResponseModel responseModel = new ResponseModel.ResponseBuilder(status, httpStatus)
            .data(data)
            .build();
        return CustomResponse.customResponse(responseModel);
    }
}
