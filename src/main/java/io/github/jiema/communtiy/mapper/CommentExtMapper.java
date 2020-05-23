package io.github.jiema.communtiy.mapper;

import io.github.jiema.communtiy.model.Comment;


public interface CommentExtMapper {
    int incCommentCount(Comment record);
}