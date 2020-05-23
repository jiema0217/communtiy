package io.github.jiema.communtiy.mapper;

import io.github.jiema.communtiy.model.Question;

import java.util.List;

public interface QuestionExtMapper {

    int incView(Question record);

    int incCommentCount(Question record);

    List<Question> selectRelated(Question question);
}