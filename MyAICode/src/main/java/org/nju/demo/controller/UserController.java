package org.nju.demo.controller;

import org.nju.demo.config.Constants;
import org.nju.demo.entity.AUser;
import org.nju.demo.entity.AUser;
import org.nju.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpSession session;

    @RequestMapping("/index")
    public String viewLogin(){
        return "login";
    }

    @RequestMapping ("/view/register")
    public String viewRegister(){
        return "register";
    }

    //userInfo change to user
    @RequestMapping("/view/info")
    public String viewInfo(Model model){
        AUser user = (AUser) session.getAttribute("user");
        model.addAttribute("user",user);
        return "user_info";
    }

    @RequestMapping("/view/password")
    public String viewPassword(){
        return "password_modify";
    }

    @RequestMapping("/login")
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password){
        AUser user = new AUser();
        user.setUsername(username);
        user.setPassword(password);
        if (userService.tryLogin(user)){
            //添加的不是刚才创建的虚拟用户，而是数据库中匹配的真实用户
            session.setAttribute("user",userService.getUserByUsername(username));
            session.removeAttribute("loginMsg");
            return "redirect:/view/projects";
        }
        else{
            session.setAttribute("loginMsg","账号不存在或密码错误");
            return "redirect:/index";
        }
    }

    @RequestMapping("/register")
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password){
        if (userService.isExist(username)) {
            session.setAttribute("RegisterMsg","该账号已存在");
            return "redirect:/view/register";
        }
        AUser user = new AUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setTelephone(Constants.UserInfo.TELEPHONE);
        user.setEmail(Constants.UserInfo.EMAIL);
        userService.addUser(user);
        session.removeAttribute("RegisterMsg");
        return "redirect:/index";
    }

    @RequestMapping("/logout")
    public String logout(){
        session.removeAttribute("user");
        return "redirect:/index";
    }

    @RequestMapping("/user/update")
    public String updateUser(@RequestParam("password") String password){
        AUser user = (AUser) session.getAttribute("user");
        user.setPassword(password);
        userService.updateUser(user);
        session.removeAttribute("user");
        return "redirect:/index";
    }

    @RequestMapping("/user/infoUpdate")
    public String updateUserInfo(@RequestParam("telephone") String telephone,
                                 @RequestParam("email") String email){
        AUser user = (AUser) session.getAttribute("user");
        user.setTelephone(telephone);
        user.setEmail(email);
        userService.updateUser(user);
        return "redirect:/view/projects";
    }
}