package cn.itcode.onlineSystem.controller;

import cn.itcode.onlineSystem.annotation.LoginRequired;
import cn.itcode.onlineSystem.entity.User;
import cn.itcode.onlineSystem.service.UserService;
import cn.itcode.onlineSystem.util.HostHolder;
import cn.itcode.onlineSystem.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    //登陆之后查看个人设置页面
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }

    //个人设置页面修改密码功能
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.POST)
    public String updatePassword(Model model, String password, String newPassword, String confirmPassword){
        if(StringUtils.isBlank(password)){
            model.addAttribute("passwordMsg", "请输入密码");
            return "/site/setting";
        }
        if(StringUtils.isBlank(newPassword)){
            model.addAttribute("passwordMsg", "请输入新密码");
            return "/site/setting";
        }
        if(StringUtils.isBlank(confirmPassword)){
            model.addAttribute("passwordMsg", "请输入确认密码");
            return "/site/setting";
        }
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(password, newPassword, user.getId());
        if(map == null || map.isEmpty()){
            //传给templates改密码成功
            model.addAttribute("msg", "密码修改成功");
            //跳到个人设置页面
            model.addAttribute("target", "user/setting");
            return "/site/operate-result";
        }else {
            //修改密码失败，跳到原来的设置页面
            model.addAttribute("passwordMsg","输入的原始密码错误");
            return "/site/setting";
        }
    }

    //个人信息主页
    @RequestMapping(path = "profile/{userId}", method = RequestMethod.POST)
    public User getProfilePage(@PathVariable("userId") int userId, Model model){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在");
        }
        model.addAttribute("user", user);
        return user;
    }

    //修改个人信息
    @LoginRequired
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String updateUser(@RequestBody User user){
        String username = user.getUsername();
        User user1 = userService.findUserByName(username);
        if(user1 == null){
            throw new RuntimeException("该用户不存在");
        }
        User updateUser = userService.updateUser(username);
        return ResponseUtil.suc(updateUser).toString();
    }

    //
}
