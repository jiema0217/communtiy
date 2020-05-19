package io.github.jiema.communtiy.service;

import io.github.jiema.communtiy.enums.CommentTYpeEnum;
import io.github.jiema.communtiy.exception.CustomizeErrorCode;
import io.github.jiema.communtiy.exception.CustomizeException;
import io.github.jiema.communtiy.mapper.CommentMapper;
import io.github.jiema.communtiy.mapper.QuestionExtMapper;
import io.github.jiema.communtiy.mapper.QuestionMapper;
import io.github.jiema.communtiy.model.Comment;
import io.github.jiema.communtiy.model.Question;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CommentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private QuestionExtMapper questionExtMapper;

    public void insert(Comment comment) {
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARAM_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTYpeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTYpeEnum.COMMENT.getType()) {
            Comment dbcomment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbcomment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
        } else {
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            commentMapper.insert(comment);
            questionExtMapper.incCommentCount(question);
        }
    }
}
