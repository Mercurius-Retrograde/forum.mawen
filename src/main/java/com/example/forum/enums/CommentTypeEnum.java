package com.example.forum.enums;


public enum  CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);
    private Integer type;

    public int getType() {
        return type;
    }

    CommentTypeEnum(Integer type){
        this.type = type;
    }

    public static boolean isExit(Long type) {
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()){
            if(commentTypeEnum.getType() == type){
                return  true;
            }else
                return false;
        }
        return false;
    }

}
