package com.mooc.house.web.controller;

import com.mooc.house.biz.service.AgencyService;
import com.mooc.house.biz.service.UserService;
import com.mooc.house.common.constants.CommonConstants;
import com.mooc.house.common.model.User;
import com.mooc.house.common.result.ResultMsg;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("msg")
    public List<User> accountsRegister() {
        return userService.getUsers();
    }

    @RequestMapping("index")
    public String index() {
        return "homepage/index";
    }

    @Autowired
    private AgencyService agencyService;

    /**
     * 注册提交:1.注册验证 2 发送邮件 3验证失败重定向到注册页面
     * 注册页获取:根据user对象为依据判断是否注册页获取请求
     *  用户认证
     *
     * @param user
     * @param modelMap
     * @return
     */
    @RequestMapping("accounts/register")
    public String Register(User user, ModelMap modelMap) {
        //当用户z账号为空或者姓名为空，则认为是注册页请求
        if (user != null || StringUtils.isNotEmpty(user.getName())) {
            modelMap.put("agencyList", agencyService.getAllAgency());
            return "/user/accounts/register";//跳转到注册业
        }
        // 用户验证
        ResultMsg resultMsg = UserHelper.validate(user);
        if (resultMsg.isSuccess() && userService.addAccount(user)) {//验证成功，添加用户
            modelMap.put("email", user.getEmail());
            return "/user/accounts/registerSubmit";//返回模板注册成功页的模板
        } else {
            return "redirect:/accounts/register?" + resultMsg.asUrlParams();//失败重新填
        }
    }
    /**
     *用于用户邮箱激活
     */
    @RequestMapping("accounts/verify")
    public String verify(String key) {
        boolean result = userService.enable(key);
        if (result) {
            return "redirect:/index?" + ResultMsg.successMsg("激活成功").asUrlParams();
        } else {
            return "redirect:/accounts/register?" + ResultMsg.errorMsg("激活失败,请确认链接是否过期");
        }
    }
    // ----------------------------登录流程------------------------------------

    /**
     * 登录接口
     */
    @RequestMapping("/accounts/signin")
    public String signin(HttpServletRequest req) {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String target = req.getParameter("target");
        if (username == null || password == null) {//登录页请求与
            req.setAttribute("target", target);
            return "/user/accounts/signin";
        }
        User user = userService.auth(username, password);
        if (user == null) {
            return "redirect:/accounts/signin?" + "target=" + target + "&username=" + username + "&"
                    + ResultMsg.errorMsg("用户名或密码错误").asUrlParams();
        } else {
            HttpSession session = req.getSession(true);
            session.setAttribute(CommonConstants.USER_ATTRIBUTE, user);
            // session.setAttribute(CommonConstants.PLAIN_USER_ATTRIBUTE, user);
            return StringUtils.isNoneBlank(target) ? "redirect:" + target : "redirect:/index";
        }
    }

    /**
     * 登出操作
     *
     * @param request
     * @return
     */
    @RequestMapping("accounts/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.invalidate();//把session注销。里面变量都删除
        return "redirect:/index";
    }
}
