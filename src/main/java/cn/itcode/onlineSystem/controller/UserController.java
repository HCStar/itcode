package cn.itcode.onlineSystem.controller;

import cn.itcode.onlineSystem.annotation.LoginRequired;
import cn.itcode.onlineSystem.entity.Account;
import cn.itcode.onlineSystem.entity.User;
import cn.itcode.onlineSystem.service.UserService;
import cn.itcode.onlineSystem.util.HostHolder;
import cn.itcode.onlineSystem.util.ResponseUtil;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    //个人设置页面修改密码功能
    @LoginRequired
    @RequestMapping(path = "/setting", method = RequestMethod.POST)
    public ResponseUtil updatePassword(@RequestParam String password, String newPassword, String confirmPassword,String userName){
//        JSONObject resultMsg = new JSONObject();
        if(StringUtils.isBlank(password)){
           return ResponseUtil.error("请输入密码");
        }
        if(StringUtils.isBlank(newPassword)){
            return ResponseUtil.error("请输入新密码");
        }
        if(StringUtils.isBlank(confirmPassword)){
           return ResponseUtil.error("请输入确认密码");
        }

        Map<String, Object> map = userService.updatePassword(password, newPassword, userName);
        if(map == null || map.isEmpty()){
            return ResponseUtil.suc("密码修改成功");
        }else {
            return ResponseUtil.error("修改密码失败");
        }
    }

    //个人信息主页
    @LoginRequired
    @RequestMapping(value = "/profile/{username}", method = RequestMethod.POST)
    public ResponseUtil getProfilePage(@PathVariable("username") String username){
        User user = userService.findUserByName(username);
        if(user == null){
            throw new RuntimeException("该用户不存在");
        }
        return ResponseUtil.suc(user);
    }

    //修改个人信息
    @LoginRequired
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    public ResponseUtil updateUser(@RequestBody User user){
        String username = user.getUserName();
        User user1 = userService.findUserByName(username);
        if(user1 == null){
            throw new RuntimeException("该用户不存在");
        }
        userService.updateUser(username);
        return ResponseUtil.suc("修改成功");
    }

    //开启用户
    @LoginRequired
    @RequestMapping(path = "/enable", method = RequestMethod.POST)
    public ResponseUtil activaAccount(@RequestParam String acctId){
        Map map = userService.enabled(acctId);
        return ResponseUtil.suc(map.get("msg"));
    }

    //冻结账户
    @LoginRequired
    @RequestMapping(path = "/lock", method = RequestMethod.POST)
    public ResponseUtil lockAccount(@RequestParam String acctId){
        if(Strings.isNullOrEmpty(acctId)){
            return ResponseUtil.error("请输入账号");
        }
        Map map = userService.locking(acctId);
        return ResponseUtil.suc(map.get("msg"));
    }

    //删除账户
    @LoginRequired
    @RequestMapping(path = "/delete", method = RequestMethod.POST)
    public ResponseUtil delAccount(@RequestParam String acctId){
        if(Strings.isNullOrEmpty(acctId)){
            return ResponseUtil.error("请输入账号");
        }
        Map map = userService.delAccount(acctId);
        return ResponseUtil.suc(map.get("msg"));
    }

    //开户
    @LoginRequired
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ResponseUtil addAcount(@RequestBody Account account){
       try {
           Map map = userService.addAccount(account);
           if (map == null || map.isEmpty()){
               return ResponseUtil.suc("添加账户成功");
           }else {
               return ResponseUtil.error(map.get("msg").toString());
           }
       }catch (Exception e){
           e.printStackTrace();
       }
       return null;
    }

    //查询所有账户
    @LoginRequired
    @RequestMapping(path = "/search", method = RequestMethod.POST)
    public ResponseUtil searchAll(){
        List<Account> accounts = userService.searchAll();
        return ResponseUtil.suc(accounts);
    }
}
