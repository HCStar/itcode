package cn.itcode.onlineSystem.service.impl;

import cn.itcode.onlineSystem.constans.CommonConstant;
import cn.itcode.onlineSystem.dao.UserMapper;
import cn.itcode.onlineSystem.entity.LoginTicket;
import cn.itcode.onlineSystem.entity.User;
import cn.itcode.onlineSystem.service.UserService;
import cn.itcode.onlineSystem.util.HelperUtil;
import cn.itcode.onlineSystem.util.RedisKeyUtil;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

//import org.springframework.security.core.GrantedAuthority;
//import org.thymeleaf.TemplateEngine;
//import org.thymeleaf.context.Context;

@Service
public class UserServiceImpl implements UserService {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    UserMapper userMapper;
//    @Autowired
//    TemplateEngine templateEngine;
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public User findUserById(int id) {
        User user = getCache(id);
        if (user==null){
            user =  initCache(id);
        }
        return user;
    }

    //注册功能包括了空账号异常，
    //以及提交表单时用户名，密码，邮箱为空时用map对象打包错误信息返回提示浏览器
    //如果提交数据无错误并且用户名，邮箱未有注册，map将为null。设置新用户相关参数并且发送激活邮件
    @Override
    public Map<String, Object> register(User user) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        //空值处理
        if (user == null) {
            throw new IllegalAccessException("参数不能为空");
        }
        //输入表单的用户名，密码，邮箱是否为空，为空则有问题
        if (Strings.isNullOrEmpty(user.getUsername()) || Strings.isNullOrEmpty(user.getPassword())) {
            map.put("errMsg", "账户或密码不能为空");
            return map;
        }

        if (Strings.isNullOrEmpty(user.getEmail())) {
            map.put("errMsg", "邮箱不能为空");
            return map;
        }
        //判断用户或邮箱是否已存在
        User u = userMapper.selectByName(user.getUsername());
        if (u != null) {
            map.put("errMsg", "该账号已存在");
            return map;
        }
        u = userMapper.selectByEmail(user.getEmail());
        if (u != null) {
            map.put("errMsg", "该邮箱已注册");
            return map;
        }
        //上述验证都通过，可以进行用户注册
        //生成六位的随机salt
        user.setSalt(HelperUtil.generateUID().substring(0, 6));
        //对密码+salt
        user.setPassword(HelperUtil.md5(user.getPassword() + user.getSalt()));
        //刚开始用户都为普通用户 0
        user.setType(0);
//        //发送随机激活码
//        user.setActivationCode(HelperUtil.generateUID());
        //用户创建时间
        user.setCreateTime(new Date());
        //用户添加到库中
        userMapper.insertUser(user);
        return map;
    }

    //激活方法，包括判断了是否激活，重复激活，激活失败。并在为激活+激活码正确情况下激活用户
    //返回激活状态
//    @Override
//    public int activation(int userId, String code) {
//        User user = userMapper.selectByID(userId);
//        //用户已经激活过
//        if(user.getStatus() == 1){
//            return CommonConstant.ACTIVATION_REPEAT;
//
//        }
//        //如果激活码验证无误
//        else if(user.getActivationCode().equals(code)){
//            userMapper.updateStatus(userId, 1);
//            return CommonConstant.ACTIVATION_SUCCESS;
//        }else {
//            return CommonConstant.ACTIVATION_FAILURE;
//        }
//    }

    //由于登录的时候，失败的原因有多个：
    //因此设置map对象打包错误信息返回给controller给浏览器
    //返回类型map带错误信息，形参为用户名，密码和登录凭证还有几秒过期
    @Override
    public Map<String, Object> login(String username, String password, int expiredSeconds) {
        Map<String, Object> map = new HashMap<>();
        //用户名和密码判断
        if(StringUtils.isBlank(username) || StringUtils.isBlank(password)){
            map.put("errMsg", "账户或密码为空");
            return map;
        }
        //验证用户名是否存在
        User user = userMapper.selectByName(username);
        if(user == null){
            map.put("errMsg", "账号不存在");
            return map;
        }
        //验证账户是否激活
//        if(user.getStatus() == 0){
//            map.put("usernameMsg", "账号未激活");
//            return map;
//        }
        //验证密码
        password = HelperUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("errMsg", "输入的密码错误");
            return map;
        }
        //生成登录凭证，表示在线.实际为生成了一行LoginTicket表数据
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(HelperUtil.generateUID());
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds*1000));
        String redisKey = RedisKeyUtil.getTicketKey(loginTicket.getTicket());
        //将登陆信息存入redis
        redisTemplate.opsForValue().set(redisKey, loginTicket);
        //给浏览器发登录凭证，以便给浏览器cookie保存
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    //登出
    @Override
    public void logout(String ticket) {
        String redisKey = RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket)redisTemplate.opsForValue().get(redisKey);
        loginTicket.setStatus(1);
        //设置登出后登陆凭证的过期时间
        redisTemplate.opsForValue().set(redisKey, loginTicket, 3600, TimeUnit.SECONDS);
    }

    //通过ticket找login ticket
    @Override
    public LoginTicket findLoginTicket(String ticket) {
        String redisKey =  RedisKeyUtil.getTicketKey(ticket);
        LoginTicket loginTicket = (LoginTicket)redisTemplate.opsForValue().get(redisKey);
        return loginTicket;
    }

    //个人设置更改密码功能
    @Override
    public Map<String, Object> updatePassword(String password, String newPassword, int id) {
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.selectByID(id);
        password = HelperUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(password)){
           map.put("passwordMsg", "输入的密码错误");
           return map;
        }else {
            newPassword = HelperUtil.md5(newPassword + user.getSalt());
            clearCache(user.getId());
            userMapper.updatePassword(id, newPassword);
        }
        return map;
    }


    //通过用户名找user
    public User findUserByName(String username){
        return userMapper.selectByName(username);
    }

    @Override
    public User updateUser(String username) {
        return userMapper.updateUser(username);
    }

    //用于security查某个user的权限
    public Collection<? extends GrantedAuthority> getAuthorities(int userId) {
        User user = this.findUserById(userId);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getType()){
                    case 1:
                        return CommonConstant.AUTHORITY_ADMIN;
                    default:
                        return CommonConstant.AUTHORITY_USER;
                }
            }
        });
        return list;
    }

    //使用redis缓存User信息步骤
    //1.优先从缓存中取值
    private User getCache(int userId){
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }
    //2.缓存中取不到，则在缓存中初始化数据
    private User initCache(int userId){
        User user = userMapper.selectByID(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        //缓存存在一小时
        redisTemplate.opsForValue().set(redisKey,user,3600, TimeUnit.SECONDS);
        return user;
    }
    //3.数据变更时清除缓存数据
    private void clearCache(int userId){
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }

//    //根据账户id获取账户对象
//    @Override
//    public AccountPojo getAccount(int accountid) {
//        return null;
//    }
//
//    //修改账户信息
//    @Override
//    public Boolean modifyAccount(AccountPojo account) {
//        return userMapper.updateAccount(account);
//    }
//
//    //从session中重新获取对象account
//    @Override
//    public void reflush(AccountPojo account) {
//        userMapper.reflush(account);
//    }
//
//    //获取管理员对象
//    @Override
//    public AdminPojo getAdmin(String username) {
//        return userMapper.getAdmin(username);
//    }
//
//    //启用账户
//    @Override
//    public void enabled(int id) {
//        AccountPojo accountPojo = userMapper.getAccount(id);
//        //获取并修改账户对象的状态属性，设置为启用
//        StatusPojo status = userMapper.getStatus("启用");
//        accountPojo.setStatus(status);
//        userMapper.updateAccount(accountPojo);
//    }
//
//    //冻结账户
//    @Override
//    public void locking(int id) {
//        AccountPojo account = userMapper.getAccount(id);
//        //获取并修改账户对象的状态属性，设置为启用
//        StatusPojo status = userMapper.getStatus("启用");
//        account.setStatus(status);
//        userMapper.updateAccount(account);
//    }
//
//    //管理员删除账户
//    @Override
//    public boolean delAccount(int id) {
//        AccountPojo account = userMapper.getAccount(id);
//        userMapper.delAccount(account);
//        return false;
//    }
//
//    //管理员开户
//    @Override
//    public boolean addAccount(AccountPojo account) {
//        StatusPojo status = userMapper.getStatus("启用");
//        account.setStatus(status);
//        return userMapper.addAccount(account);
//    }
    
}
