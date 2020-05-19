package io.github.jiema.communtiy.controller;

import io.github.jiema.communtiy.dto.CommentDTO;
import io.github.jiema.communtiy.dto.ResultDTO;
import io.github.jiema.communtiy.exception.CustomizeErrorCode;
import io.github.jiema.communtiy.model.Comment;
import io.github.jiema.communtiy.model.User;
import io.github.jiema.communtiy.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


import javax.servlet.http.HttpServletRequest;


@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @PostMapping("/comment")
    public Object post(@RequestBody CommentDTO commentDTO,
                       HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);

        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getAccountId());
        comment.setLikeCount(0L);
        commentService.insert(comment);
        return ResultDTO.okof();
    }
}
