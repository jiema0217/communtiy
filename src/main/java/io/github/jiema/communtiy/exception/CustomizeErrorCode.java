package io.github.jiema.communtiy.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {
    QUESTION_NOT_FOUND(2001, "你找的问题不存在!!!"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何评论进行问题"),
    NO_LOGIN(20003, "未登录,不能进行评论"),
    SYS_ERROR(2004, "服务器已经远洋，请稍等！！！"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006, "你回复的评论不存在");
    private String message;
    private Integer code;


    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
