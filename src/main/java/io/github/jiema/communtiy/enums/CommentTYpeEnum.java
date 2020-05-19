package io.github.jiema.communtiy.enums;

public enum CommentTYpeEnum {
    QUESTION(1),
    COMMENT(2);
    private Integer type;

    CommentTYpeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExist(Integer type) {
        for (CommentTYpeEnum commentTYpeEnum : CommentTYpeEnum.values()) {
            if (commentTYpeEnum.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }
}
