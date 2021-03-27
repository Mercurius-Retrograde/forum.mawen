package com.example.forum.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {


    QUESTION_NOT_FOUND("该条帖子不存在，请检查后重试！");

    @Override
    public String getMessage() {
        return message;
    }

    private String message;

    CustomizeErrorCode(String message) {
        this.message = message;
    }
}
