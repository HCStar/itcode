package cn.itcode.onlineSystem.controller;

import cn.itcode.onlineSystem.annotation.LoginRequired;
import cn.itcode.onlineSystem.entity.Account;
import cn.itcode.onlineSystem.entity.User;
import cn.itcode.onlineSystem.service.UserService;
import cn.itcode.onlineSystem.util.HostHolder;
import cn.itcode.onlineSystem.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String updatePassword(String password, String newPassword, String confirmPassword){
        JSONObject resultMsg = new JSONObject();
        if(StringUtils.isBlank(password)){
            resultMsg.put("msg", "请输入密码");
            return resultMsg.toJSONString();
        }
        if(StringUtils.isBlank(newPassword)){
            resultMsg.put("msg", "请输入新密码");
            return resultMsg.toJSONString();
        }
        if(StringUtils.isBlank(confirmPassword)){
            resultMsg.put("msg", "请输入确认密码");
            return resultMsg.toJSONString();
        }
        User user = hostHolder.getUser();
        Map<String, Object> map = userService.updatePassword(password, newPassword, user.getUSER_ID());
        if(map == null || map.isEmpty()){
            //传给templates改密码成功
            resultMsg.put("msg", "密码修改成功");
            return resultMsg.toJSONString();
        }else {
            //修改密码失败，跳到原来的设置页面
            resultMsg.put("msg", "修改密码失败");
            return ResponseUtil.suc(resultMsg.toJSONString()).toString();
        }
    }

    //个人信息主页
    @RequestMapping(path = "profile/{userId}", method = RequestMethod.POST)
    public String getProfilePage(@PathVariable("userId") int userId){
        User user = userService.findUserById(userId);
        if(user == null){
            throw new RuntimeException("该用户不存在");
        }
        return ResponseUtil.suc(user).toString();
    }

    //修改个人信息
    @LoginRequired
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public String updateUser(@RequestBody User user){
        String username = user.getUSERNAME();
        User user1 = userService.findUserByName(username);
        if(user1 == null){
            throw new RuntimeException("该用户不存在");
        }
        User updateUser = userService.updateUser(username);
        return ResponseUtil.suc(updateUser).toString();
    }

    //开启用户
    @LoginRequired
    @RequestMapping(path = "", method = RequestMethod.POST)
    public String activaAccount(String accountNum){
        String msg = userService.enabled(accountNum);
        return ResponseUtil.suc(msg).toString();
    }

    //冻结账户
    @LoginRequired
    @RequestMapping(path = "", method = RequestMethod.POST)
    public String lockAccount(String accountNum){
        JSONObject resultMsg = new JSONObject();
        if(Strings.isNullOrEmpty(accountNum)){
            resultMsg.put("msg", "请输入账号");
        }
        String s = userService.locking(accountNum);
        resultMsg.put("msg", s);
        return ResponseUtil.suc(resultMsg.toJSONString()).toString();
    }

    //删除账户
    @LoginRequired
    @RequestMapping(path = "", method = RequestMethod.POST)
    public String delAccount(String accountNum){
        JSONObject resultMsg = new JSONObject();
        if(Strings.isNullOrEmpty(accountNum)){
            resultMsg.put("msg", "请输入账号");
        }
        String s = userService.delAccount(accountNum);
        resultMsg.put("msg", s);
        return ResponseUtil.suc(resultMsg.toJSONString()).toString();
    }

    //开户
    @LoginRequired
    @RequestMapping(path = "/", method = RequestMethod.POST)
    public String addAcount(Account account){
        String msg = userService.addAccount(account);
        return ResponseUtil.suc(msg).toString();
    }
}
