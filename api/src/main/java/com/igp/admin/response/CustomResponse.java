package com.igp.admin.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.Response;

/**
 * Created by suditi on 18/1/18.
 */
public class CustomResponse {
    private static final Logger logger = LogManager.getLogger(CustomResponse.class);

    public static Response customResponse(ResponseModel responseModel) {
        ObjectMapper objectMapper = new ObjectMapper();
        String response = null;
        try {
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            response = objectMapper.writeValueAsString(responseModel);
        } catch (JsonProcessingException jsonException) {
            logger.error("Error in processing Json", jsonException);
        }
        return Response.status(responseModel.getHttpStatus()).entity(response).build();
    }
}
