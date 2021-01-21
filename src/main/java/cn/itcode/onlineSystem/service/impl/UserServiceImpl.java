package cn.itcode.onlineSystem.service.impl;

import cn.itcode.onlineSystem.constans.CommonConstant;
import cn.itcode.onlineSystem.dao.UserMapper;
import cn.itcode.onlineSystem.entity.Account;
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

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public User findUserById(String id) {
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
        if (Strings.isNullOrEmpty(user.getUserName()) || Strings.isNullOrEmpty(user.getPassword())) {
            map.put("errMsg", "账户或密码不能为空");
            return map;
        }

        if (Strings.isNullOrEmpty(user.getEmail())) {
            map.put("errMsg", "邮箱不能为空");
            return map;
        }
        //判断用户或邮箱是否已存在
        User u = userMapper.selectByName(user.getUserName());
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
        user.setUserType(0);
        //用户创建时间
        user.setCreateTime(new Date());
        //用户添加到库中
        userMapper.insertUser(user);
        return map;
    }


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
        //验证密码
        password = HelperUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(password)){
            map.put("errMsg", "输入的密码错误");
            return map;
        }
        //生成登录凭证，表示在线.实际为生成了一行LoginTicket表数据
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getUserId());
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
    public Map<String, Object> updatePassword(String password, String newPassword, String id) {
        Map<String, Object> map = new HashMap<>();
        User user = userMapper.selectByID(id);
        password = HelperUtil.md5(password + user.getSalt());
        if(!user.getPassword().equals(password)){
           map.put("passwordMsg", "输入的密码错误");
           return map;
        }else {
            newPassword = HelperUtil.md5(newPassword + user.getSalt());
            clearCache(user.getUserId());
            userMapper.updatePassword(id, newPassword);
        }
        return map;
    }


    //通过用户名找user
    public User findUserByName(String username){
        return userMapper.selectByName(username);
    }

    //通过用id找user
    public User selectByUserId(String userId){
        return userMapper.selectByID(userId);
    }

    @Override
    public User updateUser(String username) {
        return userMapper.updateUser(username);
    }

    //用于security查某个user的权限
    public Collection<? extends GrantedAuthority> getAuthorities(String userId) {
        User user = this.findUserById(userId);
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (user.getUserType()){
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
    private User getCache(String userId){
        String redisKey = RedisKeyUtil.getUserKey(userId);
        return (User) redisTemplate.opsForValue().get(redisKey);
    }
    //2.缓存中取不到，则在缓存中初始化数据
    private User initCache(String userId){
        User user = userMapper.selectByID(userId);
        String redisKey = RedisKeyUtil.getUserKey(userId);
        //缓存存在一小时
        redisTemplate.opsForValue().set(redisKey,user,3600, TimeUnit.SECONDS);
        return user;
    }
    //3.数据变更时清除缓存数据
    private void clearCache(String userId){
        String redisKey = RedisKeyUtil.getUserKey(userId);
        redisTemplate.delete(redisKey);
    }


    //根据账户id获取账户对象
    @Override
    public Account getAccount(String accountid) {
        return userMapper.getAccount(accountid);
    }

    //修改账户信息
    @Override
    public Boolean modifyAccount(Account account) {
        return userMapper.updateAccount(account);
    }

    //启用账户
    @Override
    public String enabled(String accountid) {
        Map map = new HashMap();
        Account account = userMapper.getAccount(accountid);
        boolean b = userMapper.updateAccount(account);
        if(b == true){
            account.setStatus(CommonConstant.ACTIVATION_SUCCESS);
            map.put("msg", "账户已启用");
        }
        else {
            account.setStatus(CommonConstant.ACTIVATION_FAILURE);
            map.put("msg", "账户启用失败,请重试");
        }
        return map.toString();
    }

    //冻结账户
    @Override
    public String locking(String accountid) {
        Map msg = new HashMap();
        Account account = userMapper.getAccount(accountid);
        //获取并修改账户对象的状态属性，设置为启用
        account.setStatus(CommonConstant.ACTIVATION_REPEAT);
        boolean b = userMapper.updateAccount(account);
        if( b == true){
            msg.put("msg", "账户已被冻结");
        }
        return msg.toString();
    }

    //管理员删除账户
    @Override
    public String delAccount(String accountid) {
        Map map = new HashMap();
        Account account = userMapper.getAccount(accountid);
        account.setStatus(CommonConstant.ACTIVATION_FAILURE);
        boolean b = userMapper.delAccount(account);
        if (b == true){
            map.put("msg", "删除账户成功");
        }
        return map.toString();
    }

    //管理员开户
    @Override
    public String addAccount(Account account) {
        Map map = new HashMap();
        boolean b = userMapper.addAccount(account);
        account.setStatus(CommonConstant.ACTIVATION_SUCCESS);
        if(b == true){
            map.put("msg", "开户成功");
        }
        return map.toString();
    }
}
