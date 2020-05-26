package io.github.jiema.communtiy.controller;

import io.github.jiema.communtiy.dto.NotificationDTO;
import io.github.jiema.communtiy.enums.NotificationTypeEnum;
import io.github.jiema.communtiy.model.User;
import io.github.jiema.communtiy.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    /**
     * 当用户点击某一个最新回复时，获取它的id
     * 并检测用户的合法行，最后返回到该页面
     *
     * @param id      最新回复的id值
     * @param request
     * @return
     */
    @GetMapping("/notification/{id}")
    public String profile(@PathVariable(name = "id") Long id,
                          HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return "redirect:/";
        }

        NotificationDTO notificationDTO = notificationService.read(id, user);
        if (NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType() || NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()) {
            return "redirect:/question/" + notificationDTO.getOuterid();
        } else {
            return "redirect:/";
        }
    }
}
