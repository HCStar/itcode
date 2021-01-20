package cn.itcode.onlineSystem.service;

import cn.itcode.onlineSystem.entity.Account;
import cn.itcode.onlineSystem.entity.LoginTicket;
import cn.itcode.onlineSystem.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public interface UserService {
    //获取账户对象
    public Account getAccount(String accountid);
    //修改账户
    public Boolean modifyAccount(Account account);

    //启用账户
    public String enabled(String id);
    //冻结账户
    public String locking(String id);
    //删除账户
    public String delAccount(String id);
    //管理员开户
    public String addAccount(Account account);

    //根据id查找用户
    public User findUserById(int id);

    public Map<String,Object> register(User user) throws IllegalAccessException;

//    public int activation(int userId,String code);

    public Map<String,Object> login(String username,String password,int expiredSeconds);

    public void logout(String ticket);

    public LoginTicket findLoginTicket(String ticket);

    public Map<String,Object> updatePassword(String password,String newPassword,int id);

    public User findUserByName(String username);

    public User updateUser(String username);

    public Collection<? extends GrantedAuthority> getAuthorities(int userId);
}
