package com.example.forum.exception;

public class CustomizeException extends RuntimeException{
    //继承runtimeException为不在上一层进行try catch提高正常使用的性能
    private String message;

    public CustomizeException(ICustomizeErrorCode errorCode){
        this.message = errorCode.getMessage();
    }
    public CustomizeException(String message){
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
