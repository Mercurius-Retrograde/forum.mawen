package com.example.forum.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {


    QUESTION_NOT_FOUND(2001,"该条帖子不存在，请检查后重试！"),
    TARGET_PARAM_NOT_FOUND(2002,"未选中任何问题进行回复,可能该条问题已被删除了。"),
    NO_LOGIN(2003,"用户未登录，请先登录！"),
    SYS_ERROR(2004,"服务器出问题了，请稍后重试..."),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"该条评论不存在，请检查后重试！"),
    COMTENT_IS_EMPTY(2007,"输入内容不能为空."),
    READ_NOTIFICATION_FAIL(2008,"非法操作！"),
    NOTIFICATION_NOT_FOUND(2009,"该通知不翼而飞~")
    ;
    @Override
    public String getMessage() {
        return message;
    }

    private String message;
    private Integer code;

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
