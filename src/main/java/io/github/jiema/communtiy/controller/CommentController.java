package io.github.jiema.communtiy.controller;

import io.github.jiema.communtiy.dto.CommentCreateDTO;
import io.github.jiema.communtiy.dto.CommentDTO;
import io.github.jiema.communtiy.dto.ResultDTO;
import io.github.jiema.communtiy.enums.CommentTYpeEnum;
import io.github.jiema.communtiy.exception.CustomizeErrorCode;
import io.github.jiema.communtiy.model.Comment;
import io.github.jiema.communtiy.model.User;
import io.github.jiema.communtiy.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 评论功能
     * 当用户进行评论事，来校验用户信息和评论内容
     *
     * @param commentDTO
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/comment")
    public Object post(@RequestBody CommentCreateDTO commentDTO,
                       HttpServletRequest request) {
        //通过session获取用户，检验用户是否能评论
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        //判断comment是否为空
        if (commentDTO == null || StringUtils.isBlank(commentDTO.getContent()))
            return ResultDTO.errorOf(CustomizeErrorCode.CONTENT_IS_EMPTY);
        //设置评论信息的其他内容，便于存储与查询
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setCommentator(user.getAccountId());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        //添加新的留言
        commentService.insert(comment, user);
        return ResultDTO.okof();
    }

    /**
     * 二级评论
     *
     * @param id 获取要评论的id值
     * @return
     */
    @ResponseBody
    @GetMapping("/comment/{id}")
    public ResultDTO comments(@PathVariable("id") Long id) {
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTYpeEnum.COMMENT);
        return ResultDTO.okof(commentDTOS);
    }
}
