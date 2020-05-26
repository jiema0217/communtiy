package io.github.jiema.communtiy.controller;

import io.github.jiema.communtiy.dto.PaginationDTO;
import io.github.jiema.communtiy.model.User;
import io.github.jiema.communtiy.service.NotificationService;
import io.github.jiema.communtiy.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Resource
    private QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    /**
     * 用户的个人界面，存储用户的所有提问和其他用户的回复、评论等交互功能
     *
     * @param action  用户的提问或回复信息
     * @param page    显示起始页
     * @param size    分页的大小
     * @param request
     * @param model
     * @return
     */
    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action,
                          @RequestParam(name = "page", defaultValue = "1") Integer page,
                          @RequestParam(name = "size", defaultValue = "5") Integer size,
                          HttpServletRequest request,
                          Model model) {
        //检查用户是否登录
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }
        //action=="questions",说明用户点击的是提问信息，否则是最新回复信息
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PaginationDTO paginationDTO = questionService.list(user.getAccountId(), page, size);
            model.addAttribute("pagination", paginationDTO);
        } else if ("replies".equals(action)) {
            PaginationDTO paginationDTO = notificationService.list(user.getAccountId(), page, size);
            model.addAttribute("section", "replies");
            model.addAttribute("sectionName", "最新回复");
            model.addAttribute("pagination", paginationDTO);

        }

        return "profile";
    }
}
