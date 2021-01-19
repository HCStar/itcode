package cn.itcode.onlineSystem.controller;

import cn.itcode.onlineSystem.constans.CommonConstant;
import cn.itcode.onlineSystem.entity.User;
import cn.itcode.onlineSystem.service.UserService;
import cn.itcode.onlineSystem.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 登录接口
 */
@RestController
@RequestMapping("/login")
public class LoginController implements CommonConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    //项目路径
    @Value("${server.servlet.context-path}")
    private String contextPath;

    //注册提交数据功能
    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user){
        try {
            Map<String, Object> map = userService.register(user);
            if(map == null || map.isEmpty()){
                model.addAttribute("msg","您的账户尚未激活");
                return ResponseUtil.error("您的账户尚未激活").toString();
            }
            //注册失败，跳转到原来的页面
            else {
                //失败了传失败信息，跳到到原来的页面
//                model.addAttribute("usernameMsg", map.get("usernameMsg"));
//                model.addAttribute("passwordMsg", map.get("passwordMsg"));
//                model.addAttribute("emailMsg", map.get("emailMsg"));
                return ResponseUtil.error(map.get("errMsg").toString()).toString();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    //激活页面
//    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.POST)
//    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code){
//        int result = userService.activation(userId, code);
//        if(result == ACTIVATION_SUCCESS){
//            //激活成功，跳到登录页面
//            model.addAttribute("msg","账号激活成功");
//            //跳到登录页面
//            model.addAttribute("target", "/login");
//        }else if(result == ACTIVATION_REPEAT){
//            model.addAttribute("msg", "该账户已经激活过了!");
//            //跳到登录页面
//            model.addAttribute("target", "/login");
//        }else {
//            model.addAttribute("msg", "激活失败！");
//            //跳到登录页面
//            model.addAttribute("target", "/register");
//        }
//        return null;
//    }

    //登录功能
    //传入的数据有用户名，密码，验证码，记住我，model，存在session的正确验证码,用于传给cookie的登录凭证
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, boolean remeberMe, Model model, HttpServletResponse response){
        //检查账号、密码
        //获取过期时间
        int expiredSeconds = remeberMe ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        //如果map里面有ticket项，说明登录成功
        if(map.containsKey("ticket")){
            //登陆成功，把成功凭证发给浏览器cookie
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            //保存时间
            cookie.setMaxAge(60);
            //生效的地方
            cookie.setPath(contextPath);
            response.addCookie(cookie);
            //登录成功，重定向回到index页面
            return ResponseUtil.suc("登录成功").toString();
        }else {
//            model.addAttribute("usernameMsg",map.get("usernameMsg"));
//            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            //回到登录页面
//            return "site/login";
          return ResponseUtil.error(map.get("errMsg").toString()).toString();
        }
    }

    //退出登录
    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(@CookieValue("ticket") String ticket){
        //得到浏览器的cookie
        userService.logout(ticket);
        SecurityContextHolder.clearContext();
        //重定向到登录页面
        return "redirect:/login";
    }
}
