package io.github.jiema.communtiy.controller;

import io.github.jiema.communtiy.dto.AccesstokenDTO;
import io.github.jiema.communtiy.dto.GithubUser;
import io.github.jiema.communtiy.model.User;
import io.github.jiema.communtiy.provider.GithubProvider;
import io.github.jiema.communtiy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;


@Controller
public class AuthorizeController {
    @Autowired
    private GithubProvider githubProvider;
    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.clietn.secret}")
    private String clientSecret;
    @Value("${github.redirect_uri}")
    private String redirectUri;
    @Autowired
    private UserService userService;

    /**
     * 用户登录校验
     * 通过页面传递过来的code和state去github获取用户的access——token值
     * 在用该值登录到github上，返回的用户数据如果在服务器的数据库存在，则更新Token
     * 否则存入数据中
     *
     * @param code
     * @param state
     * @param response
     * @return index.html
     */
    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code,
                           @RequestParam("state") String state,
                           HttpServletResponse response) {
        AccesstokenDTO accesstokenDTO = new AccesstokenDTO();
        accesstokenDTO.setClient_id(clientId);
        accesstokenDTO.setClient_secret(clientSecret);
        accesstokenDTO.setCode(code);
        accesstokenDTO.setRedirect_uri(redirectUri);
        accesstokenDTO.setState(state);
        //获取access_token的值
        String accessToken = githubProvider.getAccessToken(accesstokenDTO);
        //获取GIthub用户信息
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null && githubUser.getId() != null) {
            User user = new User();
            //生成随机ID
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(githubUser.getId());
            user.setAvatarUrl(githubUser.getAvatarUrl());
            //添加到数据库中
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token", token));
            return "redirect:/";
        } else {
            return "redirect:/";
        }
    }

    /**
     * 用户退出登录页面
     * 进行清除服务器的session和用户浏览器的cookie的信息
     *
     * @param request
     * @param response
     * @return index.html
     */
    @GetMapping("/logout")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response) {
        request.getSession().removeAttribute("user");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
