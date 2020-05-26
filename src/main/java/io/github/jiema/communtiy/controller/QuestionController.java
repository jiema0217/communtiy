package io.github.jiema.communtiy.controller;

import io.github.jiema.communtiy.dto.CommentDTO;
import io.github.jiema.communtiy.dto.QuestionDTO;
import io.github.jiema.communtiy.enums.CommentTYpeEnum;
import io.github.jiema.communtiy.service.CommentService;
import io.github.jiema.communtiy.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class QuestionController {
    @Resource
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;

    /**
     * 访问用户的问题信息
     * 并对阅读数进行增加
     *
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id,
                           Model model) {
        QuestionDTO questionDTO = questionService.getById(id);
        List<QuestionDTO> relatedQuestion = questionService.selectRelated(questionDTO);
        List<CommentDTO> comments = commentService.listByTargetId(id, CommentTYpeEnum.QUESTION);
        //增加阅读数
        questionService.incView(id);
        model.addAttribute("question", questionDTO);
        model.addAttribute("comments", comments);
        model.addAttribute("relatedQuestion", relatedQuestion);
        return "question";
    }
}
