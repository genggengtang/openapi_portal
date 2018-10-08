package com.exception;

/**
 * Created by geran on 16/7/13.
 */
public class OpenApiException extends RuntimeException {

    private Integer code;

    public OpenApiException(Integer code, String msg, Exception cause) {
        super(msg, cause);
        this.code = code;
    }

    public OpenApiException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public OpenApiException(String msg) {
        super(msg);
    }

    public Integer getCode() {
        return code;
    }

}
