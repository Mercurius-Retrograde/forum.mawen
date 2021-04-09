package com.example.forum.exception;

public class CustomizeException extends RuntimeException{
    //继承runtimeException为不在上一层进行try catch提高正常使用的性能
    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
