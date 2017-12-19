package com.igp.handles.response;

/**
 * Created by shanky on 8/7/17.
 */
public class HandleServiceResponse {
    /**
     * ERR_INVALID_REQUEST
     */
    public static final String	ERR_INVALID_REQUEST		= "ERR_INVALID_REQUEST";

    /**
     * ERR_INVALID_REQUEST
     */
    public static final String	ERR_UNAUTHORIZED_REQUEST	= "ERR_UNAUTHORIZED_REQUEST";

    /**
     * ERR_METHOD_NOT_FOUND
     */
    public static final String	ERR_METHOD_NOT_FOUND	= "ERR_METHOD_NOT_FOUND";
    /**
     * ERR_INTERNAL_ERROR
     */
    public static final String	ERR_INTERNAL_ERROR		= "ERR_INTERNAL_ERROR";
    /**
     * ERR_INVALID_METHOD in case of GET and POST methods
     */
    public static final String	ERR_INVALID_METHOD		= "ERR_INVALID_METHOD";

    /**
     * NO_ERROR default in all cases
     */
    public static final String	NO_ERROR				= "NO_ERROR";

    private boolean				error					= false;
    private String				errorCode				= NO_ERROR;
    private String				errorMessage			= null;
    private Object[]			errorParams				= new Object[0];
    private Object				result					= null;

    /**
     * @return boolean
     */
    public boolean isError()
    {
        return error;
    }

    /**
     * @param error
     */
    public void setError(boolean error)
    {
        this.error = error;
    }

    /**
     * @return String
     */
    public String getErrorCode()
    {
        return errorCode;
    }

    /**
     * @param errorCode
     */
    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    /**
     * @return String
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }

    /**
     * @param errorMessage
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    /**
     * @return Object[]
     */
    public Object[] getErrorParams()
    {
        return errorParams;
    }

    /**
     * @param errorParams
     */
    public void setErrorParams(Object[] errorParams)
    {
        this.errorParams = errorParams;
    }

    /**
     * @return Object
     */
    public Object getResult()
    {
        return result;
    }

    /**
     * @param result
     */
    public void setResult(Object result)
    {
        this.result = result;
    }
}
