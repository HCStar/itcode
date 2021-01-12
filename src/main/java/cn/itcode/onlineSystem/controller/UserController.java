package cn.itcode.onlineSystem.controller;

import cn.itcode.onlineSystem.entity.AccountPojo;
import cn.itcode.onlineSystem.service.UserService;
import io.swagger.models.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @RequestMapping(path = "/profile/{id}",method = RequestMethod.GET)
    public String login(@PathVariable AccountPojo account, Model model){
        String username = account.getUsername();
        String password = account.getPassword();
        AccountPojo verifyAccount = userService.getAccount(username);


        return null;
    }
}
