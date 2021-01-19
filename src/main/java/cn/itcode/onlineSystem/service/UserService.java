package cn.itcode.onlineSystem.service;

import cn.itcode.onlineSystem.entity.LoginTicket;
import cn.itcode.onlineSystem.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;

@Service
public interface UserService {
//    //获取账户对象
//    public AccountPojo getAccount(String username);
//    //获取账户对象
//    public AccountPojo getAccount(int accountid);
//    //修改账户
//    public Boolean modifyAccount(AccountPojo account);

//    //根据username获取管理员对象
//    public AdminPojo getAdmin(String username);
//
//    //启用账户
//    public void enabled(int id);
//    //冻结账户
//    public void locking(int id);
//    //删除账户
//    public boolean delAccount(int id);
//    //管理员开户
//    public boolean addAccount(AccountPojo account);

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
