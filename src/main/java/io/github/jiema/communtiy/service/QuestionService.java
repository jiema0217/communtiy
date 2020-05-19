package io.github.jiema.communtiy.service;

import io.github.jiema.communtiy.dto.PaginationDTO;
import io.github.jiema.communtiy.dto.QuestionDTO;
import io.github.jiema.communtiy.exception.CustomizeErrorCode;
import io.github.jiema.communtiy.exception.CustomizeException;
import io.github.jiema.communtiy.mapper.QuestionExtMapper;
import io.github.jiema.communtiy.mapper.QuestionMapper;
import io.github.jiema.communtiy.mapper.UserMapper;
import io.github.jiema.communtiy.model.Question;
import io.github.jiema.communtiy.model.QuestionExample;
import io.github.jiema.communtiy.model.User;
import io.github.jiema.communtiy.model.UserExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {
    @Resource
    private QuestionMapper questionMapper;

    @Resource
    private QuestionExtMapper questionExtMapper;

    @Resource
    private UserMapper userMapper;

    public PaginationDTO list(Integer page, Integer size) {
        Integer totalCount = (int) questionMapper.countByExample(new QuestionExample());
        Integer totalPage = totalCount / size + (totalCount % size != 0 ? 1 : 0);
        if (page < 1) page = 1;
        if (page > totalPage) page = totalPage;
        Integer offset = size * (page - 1);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(new QuestionExample(), new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questions) {
            UserExample example = new UserExample();
            example.createCriteria().andAccountIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(example);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(users.get(0));
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOS(questionDTOList);
        paginationDTO.setPagination(totalPage, page);
        return paginationDTO;
    }

    public PaginationDTO list(String userId, Integer page, Integer size) {
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int) questionMapper.countByExample(questionExample);
        Integer totalPage = totalCount / size + (totalCount % size != 0 ? 1 : 0);
        if (page < 1) page = 1;
        if (page > totalPage) page = totalPage;
        Integer offset = size * (page - 1);
        QuestionExample example1 = new QuestionExample();
        example1.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithBLOBsWithRowbounds(example1, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questions) {
            UserExample userExample = new UserExample();
            userExample.createCriteria().andAccountIdEqualTo(question.getCreator());
            List<User> users = userMapper.selectByExample(userExample);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(users.get(0));
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setQuestionDTOS(questionDTOList);
        paginationDTO.setPagination(totalPage, page);
        return paginationDTO;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if (question == null) throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question, questionDTO);
        UserExample example = new UserExample();
        example.createCriteria().andAccountIdEqualTo(question.getCreator());
        List<User> users = userMapper.selectByExample(example);
        questionDTO.setUser(users.get(0));
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if (question.getId() == null) {
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insertSelective(question);
        } else {
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, example);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        questionExtMapper.incView(question);
    }
}