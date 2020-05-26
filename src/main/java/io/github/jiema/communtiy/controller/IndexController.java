package io.github.jiema.communtiy.controller;

import io.github.jiema.communtiy.dto.PaginationDTO;
import io.github.jiema.communtiy.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
public class IndexController {

    @Resource
    private QuestionService questionService;

    /**
     * 访问的主页面
     * 当用户防止网站时，则给数据进行分页操作
     *
     * @param page   页面的起始页
     * @param size   分页分数量
     * @param search 是否查询
     * @param model
     * @return
     */
    @GetMapping("/")
    public String index(@RequestParam(name = "page", defaultValue = "1") Integer page,
                        @RequestParam(name = "size", defaultValue = "5") Integer size,
                        @RequestParam(name = "search", required = false) String search,
                        Model model) {
        //返回首页内容或者查询内容
        PaginationDTO pagination = questionService.listAndSearch(search, page, size);
        model.addAttribute("pagination", pagination);
        model.addAttribute("search", search);
        return "index";
    }
}
